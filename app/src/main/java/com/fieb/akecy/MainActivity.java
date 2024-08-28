package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.controller.LoginController;

public class MainActivity extends AppCompatActivity {

    private LoginController loginController;
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

        loginController = new LoginController();

        entrarButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String senha = senhaEditText.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, insira os seus dados", Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, insira o seu email", Toast.LENGTH_SHORT).show();
            } else if (senha.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, insira a sua senha", Toast.LENGTH_SHORT).show();
            } else {
                loginController.validarLogin(MainActivity.this, email, senha, new LoginController.OnLoginValidatedListener() {
                    @Override
                    public void onLoginSuccess() {
                        Log.d("MainActivity", "Login bem-sucedido! Iniciando próxima activity...");
                        Toast.makeText(MainActivity.this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                        telaInicio();
                    }

                    @Override
                    public void onLoginFailure() {
                        Toast.makeText(MainActivity.this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void telaInicio() {
        Log.d("MainActivity", "telaInicio() chamado");
        Intent intent = new Intent(this, NovosActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void telaCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void telaEsqueceuSenha() {
        Intent intent = new Intent(this, EsqueceuSenhaActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
