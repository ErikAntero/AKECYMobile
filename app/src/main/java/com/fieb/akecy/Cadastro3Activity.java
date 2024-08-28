package com.fieb.akecy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import androidx.appcompat.app.AppCompatActivity;

public class Cadastro3Activity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText senhaEditText, senha2EditText;
    private ImageView checkMinLength, checkUppercase, checkSymbol, checkNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro3);


        dbHelper = new DatabaseHelper(this);

        senhaEditText = findViewById(R.id.cadastro3_senha);
        senha2EditText = findViewById(R.id.cadastro3_senha2);

        checkMinLength = findViewById(R.id.checkMinLength);
        checkUppercase = findViewById(R.id.checkUppercase);
        checkSymbol = findViewById(R.id.checkSymbol);
        checkNumber = findViewById(R.id.checkNumber);

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
                realizarCadastro(); // Chamar o método aqui
                Toast.makeText(Cadastro3Activity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Cadastro3Activity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

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

    private void realizarCadastro() {
        // ... (Validação da senha - mantenha a mesma)

        if (validatePassword()) {
            // Obter dados das telas anteriores
            Intent intent = getIntent();
            String nome = intent.getStringExtra("nome");
            String email = intent.getStringExtra("email");
            String telefone = intent.getStringExtra("telefone");
            String dataNascString = intent.getStringExtra("dataNasc");
            String cpf = intent.getStringExtra("cpf");
            String senha = senhaEditText.getText().toString();

            // Converter data de nascimento de String para LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataNasc = LocalDate.parse(dataNascString, formatter);

            // Inserir usuário no banco de dados
            dbHelper.inserirUsuario(nome, email, telefone, dataNasc, cpf, senha, new DatabaseHelper.OnUserInsertionListener() {
                @Override
                public void onUserInsertionComplete(boolean sucesso) {
                    if (sucesso) {
                        Toast.makeText(Cadastro3Activity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        telaActivityMain(); // Navega para a MainActivity
                        finish(); // Encerra a Cadastro3Activity
                    } else {
                        Toast.makeText(Cadastro3Activity.this, "Erro ao cadastrar. Tente novamente.", Toast.LENGTH_SHORT).show();
                        // Adicione logs aqui para identificar a causa do erro na inserção
                        Log.e("Cadastro3Activity", "Erro ao inserir usuário no banco de dados");
                    }
                }
            });
        }
    }

    private void telaActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}