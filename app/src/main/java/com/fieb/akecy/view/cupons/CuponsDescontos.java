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

public class CuponsDescontos extends AppCompatActivity {

    ImageView icNovos, icCupons, icPesquisar, icFavoritos, icConta;
    TextView recentes, cashback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuponsdescontos);

        icNovos = findViewById(R.id.ic_novos);
        icCupons = findViewById(R.id.ic_cupons);
        icPesquisar = findViewById(R.id.ic_pesquisar);
        icFavoritos = findViewById(R.id.ic_favoritos);
        icConta = findViewById(R.id.ic_conta);
        recentes = findViewById(R.id.cupons_descontos_recentes);
        cashback = findViewById(R.id.cupons_descontos_cashback);

        icNovos.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsDescontos.this, Novos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icPesquisar.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsDescontos.this, Pesquisar.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icFavoritos.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsDescontos.this, Favoritos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icConta.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsDescontos.this, Conta.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        recentes.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsDescontos.this, Cupons.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        cashback.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsDescontos.this, CuponsCashback.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}