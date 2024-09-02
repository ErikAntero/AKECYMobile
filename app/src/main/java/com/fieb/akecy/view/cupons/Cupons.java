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

public class Cupons extends AppCompatActivity {

    ImageView icNovos, icCupons, icPesquisar, icFavoritos, icConta;
    TextView cashback, descontos;
    LinearLayout containerCupons;

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
        containerCupons = findViewById(R.id.container_cupons);

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

        exibirCupons();
    }

    private void exibirCupons() {
        List<Cupom> cupons = buscarCupons();

        for (Cupom cupom : cupons) {
            View cupomView = getLayoutInflater().inflate(R.layout.cupom_item, null);
            TextView descontoTextView = cupomView.findViewById(R.id.desconto);
            TextView descricaoTextView = cupomView.findViewById(R.id.descricao);
            TextView cashbackTextView = cupomView.findViewById(R.id.cashback);
            TextView codigoTextView = cupomView.findViewById(R.id.codigo);

            descontoTextView.setText(cupom.getDesconto());
            descricaoTextView.setText(cupom.getDescricao());
            cashbackTextView.setText(cupom.getCashback() != null ? cupom.getCashback() : "");
            codigoTextView.setText(cupom.getCodigo());

            containerCupons.addView(cupomView);
        }
    }

    private List<Cupom> buscarCupons() {
        List<Cupom> cupons = new ArrayList<>();
        try (Connection conn = ConexaoSQL.conectar(this)) {
            if (conn != null) {
                String query = "SELECT * FROM Cupom ORDER BY idCupom DESC";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        Cupom cupom = new Cupom();
                        cupom.setIdCupom(rs.getInt("idCupom"));
                        cupom.setDesconto(rs.getString("desconto"));
                        cupom.setDescricao(rs.getString("descricao"));
                        cupom.setCashback(rs.getString("cashback"));
                        cupom.setCodigo(rs.getString("codigo"));
                        cupons.add(cupom);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao buscar cupons", Toast.LENGTH_SHORT).show();
        }
        return cupons;
    }
}