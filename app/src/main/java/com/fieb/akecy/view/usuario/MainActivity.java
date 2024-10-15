package com.fieb.akecy.view.usuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.controller.LoginController;
import com.fieb.akecy.view.novos.Novos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                            int idUsuario = obterIdUsuarioPorEmail(email);

                            if (idUsuario != -1) {
                                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("userId", idUsuario);
                                editor.putString("userEmail", email);
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                Toast.makeText(MainActivity.this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                                telaInicio();
                            } else {
                                Toast.makeText(MainActivity.this, "Erro ao obter dados do usuário", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Email não encontrado", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "Conta inativa", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Erro ao realizar o login", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            telaInicio();
        }
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

    private int obterIdUsuarioPorEmail(String email) {
        try (Connection conn = ConexaoSQL.conectar(this)) {
            if (conn != null) {
                String query = "SELECT idUsuario FROM Usuario WHERE email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, email);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("idUsuario");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao obter dados do usuário", Toast.LENGTH_SHORT).show();
        }
        return -1;
    }
}