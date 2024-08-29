package com.fieb.akecy.view.novos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.view.pesquisar.Pesquisar;
import com.fieb.akecy.view.cupons.Cupons;
import com.fieb.akecy.view.conta.Conta;
import com.fieb.akecy.view.favoritos.Favoritos;

public class Novos extends AppCompatActivity {

    ImageView icNovos, icCupons, icPesquisar, icFavoritos, icConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novos);

        icNovos = findViewById(R.id.ic_novos);
        icCupons = findViewById(R.id.ic_promocoes);
        icPesquisar = findViewById(R.id.ic_pesquisar);
        icFavoritos = findViewById(R.id.ic_favoritos);
        icConta = findViewById(R.id.ic_conta);

        icCupons.setOnClickListener(v -> {
            Intent intent = new Intent(Novos.this, Cupons.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icPesquisar.setOnClickListener(v -> {
            Intent intent = new Intent(Novos.this, Pesquisar.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icFavoritos.setOnClickListener(v -> {
            Intent intent = new Intent(Novos.this, Favoritos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icConta.setOnClickListener(v -> {
            Intent intent = new Intent(Novos.this, Conta.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}