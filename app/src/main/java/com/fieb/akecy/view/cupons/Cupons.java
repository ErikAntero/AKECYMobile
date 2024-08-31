package com.fieb.akecy.view.cupons;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.view.conta.Conta;
import com.fieb.akecy.view.favoritos.Favoritos;
import com.fieb.akecy.view.novos.Novos;
import com.fieb.akecy.view.pesquisar.Pesquisar;

public class Cupons extends AppCompatActivity {

    ImageView icNovos, icCupons, icPesquisar, icFavoritos, icConta;
    TextView cashback, descontos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cupons);

        icNovos = findViewById(R.id.ic_novos);
        icCupons = findViewById(R.id.ic_cupons);
        icPesquisar = findViewById(R.id.ic_pesquisar);
        icFavoritos = findViewById(R.id.ic_favoritos);
        icConta = findViewById(R.id.ic_conta);
        cashback = findViewById(R.id.cupons_cashback);
        descontos = findViewById(R.id.cupons_descontos);



        icNovos.setOnClickListener(v -> {
            Intent intent = new Intent(Cupons.this, Novos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icPesquisar.setOnClickListener(v -> {
            Intent intent = new Intent(Cupons.this, Pesquisar.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icFavoritos.setOnClickListener(v -> {
            Intent intent = new Intent(Cupons.this, Favoritos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icConta.setOnClickListener(v -> {
            Intent intent = new Intent(Cupons.this, Conta.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        cashback.setOnClickListener(v -> {
            Intent intent = new Intent(Cupons.this, CuponsCashback.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        descontos.setOnClickListener(v -> {
            Intent intent = new Intent(Cupons.this, CuponsDescontos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}