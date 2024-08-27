package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FavoritosActivity extends AppCompatActivity {

    ImageView icNovos, icPromocoes, icPesquisar, icFavoritos, icConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoritos);

        icNovos = findViewById(R.id.ic_novos);
        icPromocoes = findViewById(R.id.ic_promocoes);
        icPesquisar = findViewById(R.id.ic_pesquisar);
        icFavoritos = findViewById(R.id.ic_favoritos);
        icConta = findViewById(R.id.ic_conta);

        icNovos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritosActivity.this, NovosActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        icPromocoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritosActivity.this, PromocoesActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        icPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritosActivity.this, PesquisarActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        icConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritosActivity.this, ContaActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }
}