package com.fieb.akecy.view.favoritos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieb.akecy.R;
import com.fieb.akecy.adapter.ProdutoAdapter;
import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.view.conta.Conta;
import com.fieb.akecy.view.cupons.Cupons;
import com.fieb.akecy.view.novos.Novos;
import com.fieb.akecy.view.pesquisar.Pesquisar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Favoritos extends AppCompatActivity {

    ImageView icNovos, icCupons, icPesquisar, icFavoritos, icConta;

    RecyclerView recyclerViewProdutos;
    ProdutoAdapter produtoAdapter;
    List<com.fieb.akecy.model.Produto> listaProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoritos);

        icNovos = findViewById(R.id.ic_novos);
        icCupons = findViewById(R.id.ic_promocoes);
        icPesquisar = findViewById(R.id.ic_pesquisar);
        icFavoritos = findViewById(R.id.ic_favoritos);
        icConta = findViewById(R.id.ic_conta);

        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos); // Certifique-se de que você tem esse RecyclerView no seu layout
        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
        listaProdutos = new ArrayList<>();
        produtoAdapter = new ProdutoAdapter(listaProdutos);
        recyclerViewProdutos.setAdapter(produtoAdapter);

        carregarProdutosFavoritos();

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

    private void carregarProdutosFavoritos() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        int idUsuario = sharedPreferences.getInt("userId", -1);

        if (idUsuario != -1) {
            try (Connection conn = ConexaoSQL.conectar(this)) {
                if (conn != null) {
                    String query = "SELECT p.* FROM Produto p " +
                            "INNER JOIN Favorito f ON p.idProduto = f.idProduto " +
                            "WHERE f.idUsuario = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, idUsuario);
                        try (ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                com.fieb.akecy.model.Produto produto = new com.fieb.akecy.model.Produto();
                                produto.setIdProduto(rs.getInt("idProduto"));
                                produto.setNome(rs.getString("nome"));
                                produto.setDescricao(rs.getString("descricao"));
                                produto.setFoto1(rs.getBytes("foto1"));
                                produto.setPreco(rs.getString("preco"));

                                listaProdutos.add(produto);
                            }

                            produtoAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(this, "Erro de conexão com o banco de dados", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao carregar produtos favoritos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Usuário não logado", Toast.LENGTH_SHORT).show();
        }

        produtoAdapter.notifyDataSetChanged();

        TextView nenhumFavoritadoTextView = findViewById(R.id.nenhumFavoritadoTextView);

        if (listaProdutos.isEmpty()) {
            nenhumFavoritadoTextView.setVisibility(View.VISIBLE);
        } else {
            nenhumFavoritadoTextView.setVisibility(View.GONE);
        }
    }
}