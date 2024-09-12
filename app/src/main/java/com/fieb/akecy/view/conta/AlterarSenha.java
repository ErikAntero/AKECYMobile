package com.fieb.akecy.view.conta;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.api.ConexaoSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class AlterarSenha extends AppCompatActivity {

    private EditText senhaAtualEditText, novaSenhaEditText, confirmarNovaSenhaEditText;
    private ImageView checkMinLength, checkUppercase, checkSymbol, checkNumber;
    private LinearLayout checklistMinimo, checklistMaiuscula, checklistSimbolo, checklistNumero;
    private TextView erroTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_senha);

        senhaAtualEditText = findViewById(R.id.senha_atual_as);
        novaSenhaEditText = findViewById(R.id.nova_senha_as);
        confirmarNovaSenhaEditText = findViewById(R.id.confirmar_nova_senha_as);
        erroTextView = findViewById(R.id.erro_as);
        Button salvarButton = findViewById(R.id.salvar_as);
        ImageButton voltarButton = findViewById(R.id.voltar_as);

        checkMinLength = findViewById(R.id.checklist_minimo_imagem_as);
        checkUppercase = findViewById(R.id.checklist_maiuscula_imagem_as);
        checkSymbol = findViewById(R.id.checklist_simbolo_imagem_as);
        checkNumber = findViewById(R.id.checklist_numero_imagem_as);

        checklistMinimo = findViewById(R.id.checklist_minimo_as);
        checklistMaiuscula = findViewById(R.id.checklist_maiuscula_as);
        checklistSimbolo = findViewById(R.id.checklist_simbolo_as);
        checklistNumero = findViewById(R.id.checklist_numero_as);

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

        confirmarNovaSenhaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkPasswordsMatch();
            }
        });

        salvarButton.setOnClickListener(v -> {
            if (validatePassword()) {
                String senhaAtual = senhaAtualEditText.getText().toString();
                String novaSenha = novaSenhaEditText.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                String userEmail = sharedPreferences.getString("userEmail", "");

                if (verificarSenhaAtual(userEmail, senhaAtual)) {
                    if (atualizarSenha(userEmail, novaSenha)) {
                        Toast.makeText(AlterarSenha.this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AlterarSenha.this, Conta.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AlterarSenha.this, "Erro ao alterar a senha", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AlterarSenha.this, "Senha atual incorreta", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AlterarSenha.this, "Por favor, verifique os campos.", Toast.LENGTH_SHORT).show();
            }
        });

        voltarButton.setOnClickListener(v -> {
            Intent intent = new Intent(AlterarSenha.this, Conta.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean verificarSenhaAtual(String email, String senhaAtual) {
        try (Connection conn = ConexaoSQL.conectar(this)) {
            if (conn != null) {
                String query = "SELECT senha FROM Usuario WHERE email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, email);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String senhaCodificada = rs.getString("senha");
                            String senhaDecodificada = new String(Base64.getDecoder().decode(senhaCodificada));
                            return senhaDecodificada.equals(senhaAtual);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao verificar senha atual", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean atualizarSenha(String email, String novaSenha) {
        try (Connection conn = ConexaoSQL.conectar(this)) {
            if (conn != null) {
                String senhaCodificada = Base64.getEncoder().encodeToString(novaSenha.getBytes());
                String query = "UPDATE Usuario SET senha = ? WHERE email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, senhaCodificada);
                    stmt.setString(2, email);
                    int linhasAfetadas = stmt.executeUpdate();
                    return linhasAfetadas > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao atualizar senha", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    private void checkPasswordsMatch() {
        String senha1 = novaSenhaEditText.getText().toString();
        String senha2 = confirmarNovaSenhaEditText.getText().toString();

        if (senha1.equals(senha2)) {
            erroTextView.setText("");
        } else {
            erroTextView.setText("As senhas não coincidem");
        }
    }

    private void checkPasswordRequirements() {
        String senha = novaSenhaEditText.getText().toString();

        if (senha.length() >= 7) {
            checkMinLength.setImageResource(R.drawable.ic_check_green);
            checklistMinimo.setVisibility(View.VISIBLE);
        } else {
            checkMinLength.setImageResource(R.drawable.ic_check_white);
            checklistMinimo.setVisibility(View.VISIBLE);
        }

        if (senha.matches(".*[A-Z].*")) {
            checkUppercase.setImageResource(R.drawable.ic_check_green);
            checklistMaiuscula.setVisibility(View.VISIBLE);
        } else {
            checkUppercase.setImageResource(R.drawable.ic_check_white);
            checklistMaiuscula.setVisibility(View.VISIBLE);
        }

        if (senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            checkSymbol.setImageResource(R.drawable.ic_check_green);
            checklistSimbolo.setVisibility(View.VISIBLE);
        } else {
            checkSymbol.setImageResource(R.drawable.ic_check_white);
            checklistSimbolo.setVisibility(View.VISIBLE);
        }

        if (senha.matches(".*\\d.*")) {
            checkNumber.setImageResource(R.drawable.ic_check_green);
            checklistNumero.setVisibility(View.VISIBLE);
        } else {
            checkNumber.setImageResource(R.drawable.ic_check_white);
            checklistNumero.setVisibility(View.VISIBLE);
        }
    }

    private boolean validatePassword() {
        String senha1 = novaSenhaEditText.getText().toString();
        String senha2 = confirmarNovaSenhaEditText.getText().toString();

        return senha1.equals(senha2) && senha1.length() >= 7 && senha1.matches(".*[a-z].*")
                && senha1.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")
                && senha1.matches(".*\\d.*");
    }
}