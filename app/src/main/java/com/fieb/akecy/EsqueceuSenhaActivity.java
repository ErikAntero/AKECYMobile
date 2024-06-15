package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class EsqueceuSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esqueceusenha);

        ImageButton voltarButton = findViewById(R.id.esque_voltar);

        voltarButton.setOnClickListener(v -> telaLogin());
    }

    private void telaLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
