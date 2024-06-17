package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Cadastro2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro2);

        Button cadastrarButton = findViewById(R.id.cadastro2_btnCadastrar);
        cadastrarButton.setOnClickListener(v -> telaActivityMain());

        Button voltarButton = findViewById(R.id.cadastro2_btnVoltar);
        voltarButton.setOnClickListener(v -> telaCadastro());
    }

    private void telaActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void telaCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
