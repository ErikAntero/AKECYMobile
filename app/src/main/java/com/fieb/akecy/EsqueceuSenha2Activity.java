package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class EsqueceuSenha2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esqueceusenha2);

        ImageButton voltarButton = findViewById(R.id.esque2_voltar);
        voltarButton.setOnClickListener(v -> telaEsqueceuSenha());

        Button continuarButton = findViewById(R.id.esque2_btnContinuar);
        continuarButton.setOnClickListener(v -> telaEsqueceuSenha3());
    }

    private void telaEsqueceuSenha() {
        Intent intent = new Intent(this, EsqueceuSenhaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void telaEsqueceuSenha3() {
        Intent intent = new Intent(this, EsqueceuSenha3Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
