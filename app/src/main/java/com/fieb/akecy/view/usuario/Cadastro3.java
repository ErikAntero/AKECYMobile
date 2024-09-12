package com.fieb.akecy.view.usuario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.fieb.akecy.model.Usuario;
import com.fieb.akecy.controller.UsuarioController;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;

public class Cadastro3 extends AppCompatActivity {

    private EditText senhaEditText, senha2EditText;
    private ImageView checkMinLength, checkUppercase, checkSymbol, checkNumber;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro3);

        senhaEditText = findViewById(R.id.cadastro3_senha);
        senha2EditText = findViewById(R.id.cadastro3_senha2);

        checkMinLength = findViewById(R.id.checkMinLength);
        checkUppercase = findViewById(R.id.checkUppercase);
        checkSymbol = findViewById(R.id.checkSymbol);
        checkNumber = findViewById(R.id.checklist_numero_imagem_as);

        Button cadastrarButton = findViewById(R.id.cadastro3_btnCadastrar);
        Button btnVoltar = findViewById(R.id.cadastro3_btnVoltar);

        btnVoltar.setOnClickListener(v -> onBackPressed());

        senhaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkPasswordRequirements();
            }
        });

        senha2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkPasswordsMatch();
            }
        });

        cadastrarButton.setOnClickListener(v -> {
            if (validatePassword()) {
                usuario.setSenha(senhaEditText.getText().toString());

                UsuarioController usuarioController = new UsuarioController();
                boolean cadastroSucedido = usuarioController.cadastrarUsuario(Cadastro3.this, usuario);

                if (cadastroSucedido) {
                    Toast.makeText(Cadastro3.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Cadastro3.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                } else {
                    Toast.makeText(Cadastro3.this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuario = (Usuario) bundle.getSerializable("usuario");
        } else {
            Toast.makeText(Cadastro3.this, "Erro ao recuperar dados do usuário", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkPasswordsMatch() {
        String senha1 = senhaEditText.getText().toString();
        String senha2 = senha2EditText.getText().toString();

        TextView erroSenha2TextView = findViewById(R.id.cadastro3_erro_senha2);

        if (senha1.equals(senha2)) {
            erroSenha2TextView.setText("");
        } else {
            erroSenha2TextView.setText("As senhas não coincidem");
        }
    }

    private void checkPasswordRequirements() {
        String senha = senhaEditText.getText().toString();

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
        String senha1 = senhaEditText.getText().toString();
        String senha2 = senha2EditText.getText().toString();

        return senha1.equals(senha2) && senha1.length() >= 7 && senha1.matches(".*[a-z].*")
                && senha1.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")
                && senha1.matches(".*\\d.*");
    }

}
