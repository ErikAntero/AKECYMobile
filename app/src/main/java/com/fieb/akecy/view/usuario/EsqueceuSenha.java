package com.fieb.akecy.view.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.controller.EsqueceuSenhaController;

public class EsqueceuSenha extends AppCompatActivity {

    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esqueceu_senha);

        emailEditText = findViewById(R.id.esque_email);
        ImageButton voltarButton = findViewById(R.id.esque_voltar);
        Button continuarButton = findViewById(R.id.esque_btnContinuar);

        voltarButton.setOnClickListener(v -> telaActivityMain());

        continuarButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (validateEmail(email)) {
                EsqueceuSenhaController controller = new EsqueceuSenhaController();
                if (controller.verificarEmail(EsqueceuSenha.this, email)) {
                    Intent intent = new Intent(EsqueceuSenha.this, EsqueceuSenha2.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } else {
                    Toast.makeText(EsqueceuSenha.this, "Email não encontrado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EsqueceuSenha.this, "Por favor, insira um email válido.", Toast.LENGTH_SHORT).show();
            }
        });

        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            emailEditText.setError("Campo obrigatório");
            return false;
        } else if (!email.contains("@")) {
            emailEditText.setError("Email deve conter @");
            return false;
        } else if (email.indexOf("@") == email.length() - 1) {
            emailEditText.setError("Email deve ter algo após @");
            return false;
        } else if (!email.substring(email.indexOf("@")).contains(".")) {
            emailEditText.setError("Email deve conter um domínio, por exemplo: .com");
            return false;
        } else if (email.indexOf(".") == email.length() - 1) {
            emailEditText.setError("Email deve ter algo após .");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email inválido");
            return false;
        } else {
            return true;
        }
    }

    private void telaActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
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

            if (editText.getId() == R.id.esque_email) {
                validateEmail(s.toString().trim());
            }

            isFormatting = false;
        }
    }
}
