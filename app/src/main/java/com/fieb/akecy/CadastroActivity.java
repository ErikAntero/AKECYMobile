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

        Button avancarButton = findViewById(R.id.cadastro_btnAvancar);
        avancarButton.setOnClickListener(v -> telaCadastro2());

        Button loginButton = findViewById(R.id.cadastro_btnLogin);
        loginButton.setOnClickListener(v -> telaActivityMain());
    }

    private void telaCadastro2() {
        Intent intent = new Intent(this, Cadastro2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void telaActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
