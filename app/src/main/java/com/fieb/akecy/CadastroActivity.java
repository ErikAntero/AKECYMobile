package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        Button cadastroBtnLogin = findViewById(R.id.cadastro_btnLogin);
        Button cadastroBtnAvancar = findViewById(R.id.cadastro_btnAvancar);

        cadastroBtnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
            startActivity(intent);
        });

        cadastroBtnAvancar.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroActivity.this, Cadastro2Activity.class);
            startActivity(intent);
        });
    }
}
