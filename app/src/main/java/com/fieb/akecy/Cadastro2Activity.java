package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Cadastro2Activity extends AppCompatActivity {

    private EditText editTextDataDeNascimento, editTextCpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro2);

        editTextDataDeNascimento = findViewById(R.id.cadastro2_dataDeNascimento);
        editTextCpf = findViewById(R.id.cadastro2_cpf);
        Button btnAvancar = findViewById(R.id.cadastro2_btnAvancar);
        Button btnVoltar = findViewById(R.id.cadastro2_btnVoltar);

        editTextDataDeNascimento.setKeyListener(DigitsKeyListener.getInstance("0123456789/"));
        editTextCpf.setKeyListener(DigitsKeyListener.getInstance("0123456789."));

        editTextDataDeNascimento.addTextChangedListener(new DateTextWatcher(editTextDataDeNascimento));
        editTextCpf.addTextChangedListener(new CpfTextWatcher(editTextCpf));

        btnAvancar.setOnClickListener(v -> {
            if (validateDataDeNascimento() && validateCpf()) {
                telaCadastro3();
            } else {
                Toast.makeText(Cadastro2Activity.this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();
            }
        });

        btnVoltar.setOnClickListener(v -> onBackPressed());
    }

    private void telaCadastro3() {
        Intent intent = new Intent(this, Cadastro3Activity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private boolean validateDataDeNascimento() {
        String dataDeNascimento = editTextDataDeNascimento.getText().toString().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false);

        try {
            Date date = sdf.parse(dataDeNascimento);
            Calendar cal = Calendar.getInstance();
            assert date != null;
            cal.setTime(date);

            int month = cal.get(Calendar.MONTH) + 1;

            if (month > 12) {
                editTextDataDeNascimento.setError("Mês inválido");
                return false;
            }

            Calendar minCal = Calendar.getInstance();
            minCal.set(1900, Calendar.JANUARY, 1);

            Calendar today = Calendar.getInstance();
            today.add(Calendar.YEAR, -18);

            if (cal.before(minCal)) {
                editTextDataDeNascimento.setError("Data de nascimento inválida");
                return false;
            }

            if (cal.after(today)) {
                editTextDataDeNascimento.setError("Menor de idade");
                return false;
            }

            return true;
        } catch (ParseException e) {
            editTextDataDeNascimento.setError("Formato de data inválido");
            return false;
        }
    }

    private boolean validateCpf() {
        String cpf = editTextCpf.getText().toString().trim();
        if (cpf.isEmpty()) {
            editTextCpf.setError("Campo obrigatório");
            return false;
        } else if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            editTextCpf.setError("CPF inválido. Formato: XXX.XXX.XXX-XX");
            return false;
        } else {
            return true;
        }
    }

    private static class DateTextWatcher implements TextWatcher {

        private final EditText editText;
        private boolean isFormatting;

        public DateTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isFormatting) {
                return;
            }

            isFormatting = true;
            String formatted = formatDate(s.toString());
            editText.setText(formatted);
            editText.setSelection(formatted.length());
            isFormatting = false;
        }

        private String formatDate(String date) {
            date = date.replaceAll("\\D", "");

            StringBuilder formatted = new StringBuilder();
            int length = date.length();

            if (length > 2) {
                formatted.append(date.substring(0, 2)).append("/");
                if (length > 4) {
                    formatted.append(date.substring(2, 4)).append("/");
                    if (length > 8) {
                        formatted.append(date.substring(4, 8));
                    } else {
                        formatted.append(date.substring(4, length));
                    }
                } else {
                    formatted.append(date.substring(2, length));
                }
            } else {
                formatted.append(date);
            }

            return formatted.toString();
        }
    }

    private static class CpfTextWatcher implements TextWatcher {

        private final EditText editText;
        private boolean isFormatting;

        public CpfTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isFormatting) {
                return;
            }

            isFormatting = true;
            String formatted = formatCpf(s.toString());
            editText.setText(formatted);
            editText.setSelection(formatted.length());
            isFormatting = false;
        }

        private String formatCpf(String cpf) {
            cpf = cpf.replaceAll("\\D", "");

            StringBuilder formatted = new StringBuilder();
            int length = cpf.length();

            if (length > 3) {
                formatted.append(cpf.substring(0, 3)).append(".");
                if (length > 6) {
                    formatted.append(cpf.substring(3, 6)).append(".");
                    if (length > 9) {
                        formatted.append(cpf.substring(6, 9)).append("-");
                        formatted.append(cpf.substring(9, Math.min(11, length)));
                    } else {
                        formatted.append(cpf.substring(6, length));
                    }
                } else {
                    formatted.append(cpf.substring(3, length));
                }
            } else {
                formatted.append(cpf);
            }

            return formatted.toString();
        }
    }
}
