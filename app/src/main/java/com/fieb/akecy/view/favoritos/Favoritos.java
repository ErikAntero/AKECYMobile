package com.fieb.akecy.view.favoritos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.view.novos.Novos;
import com.fieb.akecy.view.pesquisar.Pesquisar;
import com.fieb.akecy.view.cupons.Cupons;
import com.fieb.akecy.view.conta.Conta;

public class Favoritos extends AppCompatActivity {

    ImageView icNovos, icCupons, icPesquisar, icFavoritos, icConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoritos);

        icNovos = findViewById(R.id.ic_novos);
        icCupons = findViewById(R.id.ic_promocoes);
        icPesquisar = findViewById(R.id.ic_pesquisar);
        icFavoritos = findViewById(R.id.ic_favoritos);
        icConta = findViewById(R.id.ic_conta);

        icNovos.setOnClickListener(v -> {
            Intent intent = new Intent(Favoritos.this, Novos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icCupons.setOnClickListener(v -> {
            Intent intent = new Intent(Favoritos.this, Cupons.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icPesquisar.setOnClickListener(v -> {
            Intent intent = new Intent(Favoritos.this, Pesquisar.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icConta.setOnClickListener(v -> {
            Intent intent = new Intent(Favoritos.this, Conta.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}