package com.fieb.akecy.view.conta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.controller.UsuarioController;
import com.fieb.akecy.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditarDados extends AppCompatActivity {

    private boolean isTelefoneValido = true;
    EditText nomeCompletoEd, emailEd, cpfEd, dataNascEd, telefoneEd;
    RadioGroup sexoRadioGroup;
    RadioButton masculinoRadioButton, femininoRadioButton, prefiroNaoInformarRadioButton;
    Button salvar;
    ImageButton voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_dados);

        nomeCompletoEd = findViewById(R.id.nome_completo_ed);
        emailEd = findViewById(R.id.email_ed);
        cpfEd = findViewById(R.id.cpf_ed);
        dataNascEd = findViewById(R.id.dataDeNascimento_ed);
        telefoneEd = findViewById(R.id.telefone_ed);
        sexoRadioGroup = findViewById(R.id.sexo_radio_group);
        masculinoRadioButton = findViewById(R.id.masculino_radio_button);
        femininoRadioButton = findViewById(R.id.feminino_radio_button);
        prefiroNaoInformarRadioButton = findViewById(R.id.prefiro_nao_informar_radio_button);
        salvar = findViewById(R.id.salvar_ed);
        voltar = findViewById(R.id.voltar_ed);

        telefoneEd.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        dataNascEd.setKeyListener(DigitsKeyListener.getInstance("0123456789/"));
        cpfEd.setKeyListener(DigitsKeyListener.getInstance("0123456789."));

        telefoneEd.addTextChangedListener(new EditarDados.MyTextWatcher(telefoneEd));
        dataNascEd.addTextChangedListener(new EditarDados.DateTextWatcher(dataNascEd));
        cpfEd.addTextChangedListener(new EditarDados.CpfTextWatcher(cpfEd));

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");

        UsuarioController usuarioController = new UsuarioController();
        Usuario usuario = usuarioController.buscarUsuarioPorEmail(this, userEmail);

        if (usuario != null) {
            preencherCamposComDadosDoUsuario(usuario);
        } else {
            Toast.makeText(EditarDados.this, "Erro ao recuperar dados do usuário", Toast.LENGTH_SHORT).show();
        }

        salvar.setOnClickListener(v -> {
            if (usuario != null) {
                if (validateAllFields()) {
                    atualizarDadosDoUsuario(usuario);

                    if (atualizarUsuarioNoBancoDeDados(usuario)) {
                        Toast.makeText(EditarDados.this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditarDados.this, "Erro ao atualizar os dados. Tente novamente.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!isTelefoneValido) {
                        Toast.makeText(getApplicationContext(), "Telefone inválido. Formato esperado: (X) XXXXX-XXXX", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditarDados.this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        voltar.setOnClickListener(v -> {
            finish();
        });
    }

    private void preencherCamposComDadosDoUsuario(Usuario usuario) {
        nomeCompletoEd.setText(usuario.getNome());
        emailEd.setText(usuario.getEmail());
        cpfEd.setText(usuario.getCpf());
        telefoneEd.setText(usuario.getTelefone());
        SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatoDesejado = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date dataNasc = formatoOriginal.parse(usuario.getDataNasc());
            String dataNascFormatada = formatoDesejado.format(dataNasc);
            dataNascEd.setText(dataNascFormatada);
        } catch (ParseException e) {
            e.printStackTrace();
            dataNascEd.setText(usuario.getDataNasc());
        }
        if (usuario.getSexo() != null) {
            if (usuario.getSexo().equals("Masculino")) {
                masculinoRadioButton.setChecked(true);
            } else if (usuario.getSexo().equals("Feminino")) {
                femininoRadioButton.setChecked(true);
            } else if (usuario.getSexo().equals("Prefiro não informar")) {
                prefiroNaoInformarRadioButton.setChecked(true);
            }
        }
    }

    private void atualizarDadosDoUsuario(Usuario usuario) {
        usuario.setNome(nomeCompletoEd.getText().toString());
        usuario.setEmail(emailEd.getText().toString());
        usuario.setCpf(cpfEd.getText().toString());
        usuario.setDataNasc(dataNascEd.getText().toString());
        usuario.setTelefone(telefoneEd.getText().toString());

        int selectedRadioButtonId = sexoRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            usuario.setSexo(selectedRadioButton.getText().toString());
        } else {
            usuario.setSexo(null);
        }
    }

    private boolean atualizarUsuarioNoBancoDeDados(Usuario usuario) {
        try (Connection conn = ConexaoSQL.conectar(this)) {
            if (conn != null) {
                String query = "UPDATE Usuario SET nome = ?, cpf = ?, telefone = ?, dataNasc = ?, sexo = ? WHERE email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, usuario.getNome());
                    stmt.setString(2, usuario.getCpf());
                    stmt.setString(3, usuario.getTelefone());
                    stmt.setString(4, usuario.getDataNasc());
                    stmt.setString(5, usuario.getSexo());
                    stmt.setString(6, usuario.getEmail());

                    int linhasAfetadas = stmt.executeUpdate();
                    return linhasAfetadas > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao atualizar os dados no banco de dados", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private class MyTextWatcher implements TextWatcher {

        private final EditText editText;
        private boolean isFormatting;

        public MyTextWatcher(EditText editText) {
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

            if (editText.getId() == R.id.telefone_ed) {
                String formatted = formatPhoneNumber(s.toString());
                editText.removeTextChangedListener(this);
                editText.setText(formatted);
                editText.setSelection(formatted.length());
                editText.addTextChangedListener(this);
            }

            validateTelefone();
            isFormatting = false;
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

        private boolean validateNome() {
            String nome = nomeCompletoEd.getText().toString().trim();
            if (nome.isEmpty()) {
                nomeCompletoEd.setError("Campo obrigatório");
                return false;
            } else {
                return true;
            }
        }

        private boolean validateEmail() {
            String email = emailEd.getText().toString().trim();
            if (email.isEmpty()) {
                emailEd.setError("Campo obrigatório");
                return false;
            } else if (!email.contains("@")) {
                emailEd.setError("Email deve conter @");
                return false;
            } else if (email.indexOf("@") == email.length() - 1) {
                emailEd.setError("Email deve ter algo após @");
                return false;
            } else if (!email.substring(email.indexOf("@")).contains(".")) {
                emailEd.setError("Email deve conter um domínio, por exemplo: .com");
                return false;
            } else if (email.indexOf(".") == email.length() - 1) {
                emailEd.setError("Email deve ter algo após .");
                return false;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEd.setError("Email inválido");
                return false;
            } else {
                return true;
            }
        }

        private boolean validateCpf() {
            String cpf = cpfEd.getText().toString().trim();
            if (cpf.isEmpty()) {
                cpfEd.setError("Campo obrigatório");
                return false;
            } else if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
                cpfEd.setError("CPF inválido. Formato: XXX.XXX.XXX-XX");
                return false;
            } else {
                return true;
            }
        }

        private boolean validateDataDeNascimento() {
            String dataDeNascimento = dataNascEd.getText().toString().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            sdf.setLenient(false);

            try {
                Date date = sdf.parse(dataDeNascimento);
                Calendar cal = Calendar.getInstance();
                assert date != null;
                cal.setTime(date);

                int month = cal.get(Calendar.MONTH) + 1;

                if (dataDeNascimento.isEmpty()) {
                    emailEd.setError("Campo obrigatório");
                    return false;
                }

                if (month > 12) {
                    dataNascEd.setError("Mês inválido");
                    return false;
                }

                Calendar minCal = Calendar.getInstance();
                minCal.set(1900, Calendar.JANUARY, 1);

                Calendar today = Calendar.getInstance();
                today.add(Calendar.YEAR, -18);

                if (cal.before(minCal)) {
                    dataNascEd.setError("Data de nascimento inválida");
                    return false;
                }

                if (cal.after(today)) {
                    dataNascEd.setError("Menor de idade");
                    return false;
                }

                return true;
            } catch (ParseException e) {
                dataNascEd.setError("Formato de data inválido");
                return false;
            }
        }

    private boolean validateTelefone() {
        String telefone = telefoneEd.getText().toString().trim();
        if (telefone.isEmpty()) {
            telefoneEd.setError("Campo obrigatório");
            isTelefoneValido = false;
            return false;
        } else if (telefone.length() < 14) {
            isTelefoneValido = false;
            return false;
        } else {
            isTelefoneValido = true;
            return true;
        }
    }

    private String formatPhoneNumber(String phone) {
        phone = phone.replaceAll("\\D", "");
        if (phone.length() > 11) {
            phone = phone.substring(0, 11);
        }

        StringBuilder formatted = new StringBuilder();
        if (phone.length() > 0) {
            formatted.append("(");
            formatted.append(phone.substring(0, Math.min(2, phone.length())));
        }
        if (phone.length() >= 3) {
            formatted.append(") ");
            formatted.append(phone.charAt(2)); // adiciona o 0 aleatório
        }

        if (phone.length() >= 4) {
            formatted.append(" "); // espaço antes do próximo bloco
            formatted.append(phone.substring(3, Math.min(7, phone.length())));
        }
        if (phone.length() > 7) {
            formatted.append("-");
            formatted.append(phone.substring(7));
        }
        return formatted.toString();
    }

        private boolean validateAllFields() {
            return validateNome() && validateEmail() && validateDataDeNascimento()
                    && validateCpf() && validateTelefone();
        }
    }