package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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
    }

    private void telaInicio() {
        Intent intent = new Intent(this, NovosActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void telaCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void telaEsqueceuSenha() {
        Intent intent = new Intent(this, EsqueceuSenhaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
