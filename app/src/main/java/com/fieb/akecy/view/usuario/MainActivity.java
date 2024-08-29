package com.fieb.akecy.view.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.controller.LoginController;
import com.fieb.akecy.view.novos.Novos;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, senhaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button entrarButton = findViewById(R.id.login_btnEntrar);
        entrarButton.setOnClickListener(v -> telaInicio());

        Button cadastrarButton = findViewById(R.id.login_cadastrar);
        cadastrarButton.setOnClickListener(v -> telaCadastro());

        TextView esqueceuASenhaTextView = findViewById(R.id.login_EsqueceuASenha);
        esqueceuASenhaTextView.setOnClickListener(v -> telaEsqueceuSenha());

        emailEditText = findViewById(R.id.login_email);
        senhaEditText = findViewById(R.id.login_senha);

        entrarButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String senha = senhaEditText.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, insira os seus dados", Toast.LENGTH_SHORT).show();
            } else {
                LoginController loginController = new LoginController();
                LoginController.LoginResult loginResult = loginController.validarLogin(MainActivity.this, email, senha);

                switch (loginResult.status) {
                    case 0:
                        if (loginResult.nivelAcesso.equals("ADMIN")) {
                            Toast.makeText(MainActivity.this, "Nível de acesso ADMIN, por favor, entre pelo sistema web", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                            telaInicio();
                        }
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Email não encontrado", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "Conta bloqueada", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Erro ao realizar o login", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void telaInicio() {
        Log.d("MainActivity", "telaInicio() chamado");
        Intent intent = new Intent(this, Novos.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void telaCadastro() {
        Intent intent = new Intent(this, Cadastro.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void telaEsqueceuSenha() {
        Intent intent = new Intent(this, EsqueceuSenha.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
