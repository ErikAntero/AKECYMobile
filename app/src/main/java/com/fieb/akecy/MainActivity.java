package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText emailEditText, senhaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        emailEditText = findViewById(R.id.login_email);
        senhaEditText = findViewById(R.id.login_senha);

        Button entrarButton = findViewById(R.id.login_btnEntrar);
        entrarButton.setOnClickListener(v -> realizarLogin());

        Button cadastrarButton = findViewById(R.id.login_cadastrar);
        cadastrarButton.setOnClickListener(v -> telaCadastro());

        TextView esqueceuASenhaTextView = findViewById(R.id.login_EsqueceuASenha);
        esqueceuASenhaTextView.setOnClickListener(v -> telaEsqueceuSenha());
    }

    private void realizarLogin() {
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();

        // Verificar login no banco de dados (passando o listener)
        dbHelper.verificarLogin(email, senha, new DatabaseHelper.OnLoginVerificationListener() {
            @Override
            public void onLoginVerificationComplete(boolean loginValido) {
                // Combina as condições: verifica se os campos estão preenchidos E se o login é válido
                if (!email.isEmpty() && !senha.isEmpty() && loginValido) {
                    // Login bem-sucedido, vá para a próxima tela
                    telaInicio();
                } else {
                    // Verificar o motivo da falha no login
                    dbHelper.verificarEmail(email, new DatabaseHelper.OnEmailVerificationListener() {
                        @Override
                        public void onEmailVerificationComplete(boolean emailExiste) {
                            if (!emailExiste) {
                                Toast.makeText(MainActivity.this, "Email não encontrado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Senha incorreta ou conta bloqueada", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }



    private void telaInicio() {
        Intent intent = new Intent(this, NovosActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void telaCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void telaEsqueceuSenha() {
        Intent intent = new Intent(this, EsqueceuSenhaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}