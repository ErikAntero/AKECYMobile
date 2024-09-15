package com.fieb.akecy.view.novos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.adapter.ProdutoAdapter;
import com.fieb.akecy.view.pesquisar.Pesquisar;
import com.fieb.akecy.view.cupons.Cupons;
import com.fieb.akecy.view.conta.Conta;
import com.fieb.akecy.view.favoritos.Favoritos;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fieb.akecy.api.ConexaoSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;


public class Novos extends AppCompatActivity {

    ImageView icNovos, icCupons, icPesquisar, icFavoritos, icConta;
    RecyclerView recyclerViewProdutos;
    ProdutoAdapter produto;
    List<com.fieb.akecy.model.Produto> listaProdutos;

    @Override
    protected void onResume() {
        super.onResume();
        carregarProdutos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novos);

        icNovos = findViewById(R.id.ic_novos);
        icCupons = findViewById(R.id.ic_promocoes);
        icPesquisar = findViewById(R.id.ic_pesquisar);
        icFavoritos = findViewById(R.id.ic_favoritos);
        icConta = findViewById(R.id.ic_conta);

        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);
        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
        listaProdutos = new ArrayList<>();
        produto = new ProdutoAdapter(listaProdutos);
        recyclerViewProdutos.setAdapter(produto);

        carregarProdutos();

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

    @SuppressLint("NotifyDataSetChanged")
    private void carregarProdutos() {
        Connection conn = ConexaoSQL.conectar(this);
        if (conn != null) {
            try {
                String query = "SELECT * FROM Produto WHERE statusProd = 'ATIVO' ORDER BY idProduto DESC";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    com.fieb.akecy.model.Produto produto = new com.fieb.akecy.model.Produto();
                    produto.setIdProduto(rs.getInt("idProduto"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setFoto1(rs.getBytes("foto1"));
                    produto.setPreco(rs.getString("preco"));

                    listaProdutos.add(produto);
                }

                produto.notifyDataSetChanged();

            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao carregar produtos", Toast.LENGTH_SHORT).show();
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}