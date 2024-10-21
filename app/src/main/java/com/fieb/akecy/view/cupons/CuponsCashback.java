package com.fieb.akecy.view.cupons;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.model.Cupom;
import com.fieb.akecy.view.conta.Conta;
import com.fieb.akecy.view.favoritos.Favoritos;
import com.fieb.akecy.view.novos.Novos;
import com.fieb.akecy.view.pesquisar.Pesquisar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CuponsCashback extends AppCompatActivity {

    ImageView icNovos, icCupons, icPesquisar, icFavoritos, icConta;
    TextView recentes, descontos;
    LinearLayout containerCuponsCashback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cupons_cashback);

        icNovos = findViewById(R.id.ic_novos);
        icCupons = findViewById(R.id.ic_cupons);
        icPesquisar = findViewById(R.id.ic_pesquisar);
        icFavoritos = findViewById(R.id.ic_favoritos);
        icConta = findViewById(R.id.ic_conta);
        recentes = findViewById(R.id.cupons_cashback_recentes);
        descontos = findViewById(R.id.cupons_cashback_descontos);
        containerCuponsCashback = findViewById(R.id.container_cupons_cashback);

        icNovos.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsCashback.this, Novos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icCupons.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsCashback.this, Cupons.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icPesquisar.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsCashback.this, Pesquisar.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icFavoritos.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsCashback.this, Favoritos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icConta.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsCashback.this, Conta.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        recentes.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsCashback.this, Cupons.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        descontos.setOnClickListener(v -> {
            Intent intent = new Intent(CuponsCashback.this, CuponsDescontos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        exibirCuponsCashback();
    }

    private void exibirCuponsCashback() {
        List<Cupom> cuponsCashback = buscarCuponsCashback();

        for (Cupom cupom : cuponsCashback) {
            View cupomView = getLayoutInflater().inflate(R.layout.cupom_item, null);
            TextView descontoTextView = cupomView.findViewById(R.id.desconto);
            TextView descricaoTextView = cupomView.findViewById(R.id.descricao);
            TextView cashbackTextView = cupomView.findViewById(R.id.cashback);
            TextView codigoTextView = cupomView.findViewById(R.id.codigo);

            descontoTextView.setText(cupom.getDesconto());
            descricaoTextView.setText(cupom.getDescricao());
            cashbackTextView.setText(cupom.getCashback());
            codigoTextView.setText(cupom.getCodigo());

            containerCuponsCashback.addView(cupomView);
        }
    }

    private List<Cupom> buscarCuponsCashback() {
        List<Cupom> cuponsCashback = new ArrayList<>();
        try (Connection conn = ConexaoSQL.conectar(this)) {
            if (conn != null) {
                String query = "SELECT * FROM Cupom WHERE cashback IS NOT NULL AND statusCupom = 'ATIVO' ORDER BY idCupom DESC";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        Cupom cupom = new Cupom();
                        cupom.setIdCupom(rs.getInt("idCupom"));
                        cupom.setDesconto(rs.getString("desconto"));
                        cupom.setDescricao(rs.getString("descricao"));
                        cupom.setCashback(rs.getString("cashback"));
                        cupom.setCodigo(rs.getString("codigo"));
                        cuponsCashback.add(cupom);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao buscar cupons com cashback", Toast.LENGTH_SHORT).show();
        }
        return cuponsCashback;
    }
}