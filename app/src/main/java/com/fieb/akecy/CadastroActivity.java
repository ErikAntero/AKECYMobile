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

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNome, editTextEmail, editTextTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        editTextNome = findViewById(R.id.cadastro_nome);
        editTextEmail = findViewById(R.id.cadastro_email);
        editTextTelefone = findViewById(R.id.cadastro_telefone);
        Button avancarButton = findViewById(R.id.cadastro_btnAvancar);

        editTextTelefone.setKeyListener(DigitsKeyListener.getInstance("0123456789() -"));

        editTextNome.addTextChangedListener(new MyTextWatcher(editTextNome));
        editTextEmail.addTextChangedListener(new MyTextWatcher(editTextEmail));
        editTextTelefone.addTextChangedListener(new MyTextWatcher(editTextTelefone));

        avancarButton.setOnClickListener(v -> {
            if (validateNome() && validateEmail() && validateTelefone()) {
                telaCadastro2();
            } else {
                Toast.makeText(CadastroActivity.this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();
            }
        });

        Button loginButton = findViewById(R.id.cadastro_btnLogin);
        loginButton.setOnClickListener(v -> telaActivityMain());
    }

    private void telaCadastro2() {
        Intent intent = new Intent(this, Cadastro2Activity.class);
        startActivity(intent);
        intent.putExtra("nome", editTextNome.getText().toString());
        intent.putExtra("email", editTextEmail.getText().toString());
        intent.putExtra("telefone", editTextTelefone.getText().toString());
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void telaActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean validateNome() {
        String nome = editTextNome.getText().toString().trim();
        if (nome.isEmpty()) {
            editTextNome.setError("Campo obrigatório");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail() {
        String email = editTextEmail.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("Campo obrigatório");
            return false;
        } else if (!email.contains("@")) {
            editTextEmail.setError("Email deve conter @");
            return false;
        } else if (email.indexOf("@") == email.length() - 1) {
            editTextEmail.setError("Email deve ter algo após @");
            return false;
        } else if (!email.substring(email.indexOf("@")).contains(".")) {
            editTextEmail.setError("Email deve conter um domínio, por exemplo: .com");
            return false;
        } else if (email.indexOf(".") == email.length() - 1) {
            editTextEmail.setError("Email deve ter algo após .");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email inválido");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateTelefone() {
        String telefone = editTextTelefone.getText().toString().trim();
        if (telefone.isEmpty()) {
            editTextTelefone.setError("Campo obrigatório");
            return false;
        } else if (!telefone.matches("\\(\\d{2}\\) \\d \\d{4}-\\d{4}")) {
            editTextTelefone.setError("Telefone inválido. Formato: (XX) X XXXX-XXXX");
            return false;
        } else {
            return true;
        }
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

            if (editText.getId() == R.id.cadastro_telefone) {
                String formatted = formatPhoneNumber(s.toString());
                editText.removeTextChangedListener(this);
                editText.setText(formatted);
                editText.setSelection(formatted.length());
                editText.addTextChangedListener(this);
            }

            validateNome();
            validateEmail();
            validateTelefone();
            isFormatting = false;
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
                formatted.append(phone.charAt(2));
            }
            if (phone.length() >= 4) {
                formatted.append(" ");
                formatted.append(phone.substring(3, Math.min(7, phone.length())));
            }
            if (phone.length() > 7) {
                formatted.append("-");
                formatted.append(phone.substring(7));
            }
            return formatted.toString();
        }
    }
}
