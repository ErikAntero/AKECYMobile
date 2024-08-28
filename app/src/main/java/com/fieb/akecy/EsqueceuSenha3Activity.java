package com.fieb.akecy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EsqueceuSenha3Activity extends AppCompatActivity {

    private EditText novaSenhaEditText, confirmarSenhaEditText;
    private ImageView checkMinLength, checkUppercase, checkSymbol, checkNumber;
    private TextView erroSenha2TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esqueceusenha3);

        novaSenhaEditText = findViewById(R.id.esque3_senha);
        confirmarSenhaEditText = findViewById(R.id.esque3_senha2);
        erroSenha2TextView = findViewById(R.id.esque3_erro_senha2);

        checkMinLength = findViewById(R.id.checkMinLength);
        checkUppercase = findViewById(R.id.checkUppercase);
        checkSymbol = findViewById(R.id.checkSymbol);
        checkNumber = findViewById(R.id.checkNumber);

        novaSenhaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkPasswordRequirements();
            }
        });

        confirmarSenhaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkPasswordsMatch();
            }
        });

        Button continuarButton = findViewById(R.id.esque3_btnContinuar);
        continuarButton.setOnClickListener(v -> {
            if (validatePassword()) {
                Toast.makeText(EsqueceuSenha3Activity.this, "Senha alter com sucesso!", Toast.LENGTH_SHORT).show();
                telaActivityMain();
            } else {
                Toast.makeText(EsqueceuSenha3Activity.this, "Por favor, verifique os campos.", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btnVoltar = findViewById(R.id.esque3_voltar);
        btnVoltar.setOnClickListener(v -> onBackPressed());
    }

    private void telaActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void checkPasswordsMatch() {
        String senha1 = novaSenhaEditText.getText().toString();
        String senha2 = confirmarSenhaEditText.getText().toString();

        if (senha1.equals(senha2)) {
            erroSenha2TextView.setText("");
        } else {
            erroSenha2TextView.setText("As senhas não coincidem");
        }
    }

    private void checkPasswordRequirements() {
        String senha = novaSenhaEditText.getText().toString();

        if (senha.length() >= 7) {
            checkMinLength.setImageResource(R.drawable.ic_check_green);
            checkMinLength.setVisibility(View.VISIBLE);
        } else {
            checkMinLength.setImageResource(R.drawable.ic_check_white);
            checkMinLength.setVisibility(View.VISIBLE);
        }

        if (senha.matches(".*[A-Z].*")) {
            checkUppercase.setImageResource(R.drawable.ic_check_green);
            checkUppercase.setVisibility(View.VISIBLE);
        } else {
            checkUppercase.setImageResource(R.drawable.ic_check_white);
            checkUppercase.setVisibility(View.VISIBLE);
        }

        if (senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            checkSymbol.setImageResource(R.drawable.ic_check_green);
            checkSymbol.setVisibility(View.VISIBLE);
        } else {
            checkSymbol.setImageResource(R.drawable.ic_check_white);
            checkSymbol.setVisibility(View.VISIBLE);
        }

        if (senha.matches(".*\\d.*")) {
            checkNumber.setImageResource(R.drawable.ic_check_green);
            checkNumber.setVisibility(View.VISIBLE);
        } else {
            checkNumber.setImageResource(R.drawable.ic_check_white);
            checkNumber.setVisibility(View.VISIBLE);
        }
    }

    private boolean validatePassword() {
        String senha1 = novaSenhaEditText.getText().toString();
        String senha2 = confirmarSenhaEditText.getText().toString();

        return senha1.equals(senha2) && senha1.length() >= 7 && senha1.matches(".*[a-z].*")
                && senha1.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")
                && senha1.matches(".*\\d.*");
    }
}
