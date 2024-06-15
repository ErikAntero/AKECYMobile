package com.fieb.akecy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cadastro2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro2);

        Button cadastro2BtnCadastrar = findViewById(R.id.cadastro2_btnCadastrar);
        Button cadastro2BtnVoltar = findViewById(R.id.cadastro2_btnVoltar);

        cadastro2BtnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telaLogin();
            }
        });

        cadastro2BtnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarParaCadastro();
            }
        });
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
