package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class EsqueceuSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esqueceusenha);

        ImageButton voltarButton = findViewById(R.id.esque_voltar);
        voltarButton.setOnClickListener(v -> telaActivityMain());

        Button continuarButton = findViewById(R.id.esque_btnContinuar);
        continuarButton.setOnClickListener(v -> telaEsqueceuSenha2());
    }

    private void telaActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void telaEsqueceuSenha2() {
        Intent intent = new Intent(this, EsqueceuSenha2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
