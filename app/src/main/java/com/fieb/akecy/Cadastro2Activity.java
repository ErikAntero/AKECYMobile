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

        Button cadastro2BtnCadastrar = findViewById(R.id.cadastro2_btnCadastrar);
        Button cadastro2BtnVoltar = findViewById(R.id.cadastro2_btnVoltar);

        cadastro2BtnCadastrar.setOnClickListener(v -> telaLogin());

        cadastro2BtnVoltar.setOnClickListener(v -> voltarParaCadastro());
    }

    private void telaLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void voltarParaCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
        finish();
    }
}
