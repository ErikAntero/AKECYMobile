package com.fieb.akecy.view.pesquisar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieb.akecy.R;
import com.fieb.akecy.adapter.ProdutoAdapter;
import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.model.Produto;
import com.fieb.akecy.view.conta.Conta;
import com.fieb.akecy.view.cupons.Cupons;
import com.fieb.akecy.view.favoritos.Favoritos;
import com.fieb.akecy.view.novos.Novos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Pesquisar extends AppCompatActivity {

    ImageView icNovos, icCupons, icPesquisar, icFavoritos, icConta;
    RecyclerView recyclerViewProdutos;
    ProdutoAdapter produtoAdapter;
    List<Produto> listaProdutos;
    EditText barraPesquisa;
    ConstraintLayout containerCategorias;
    TextView limparCategoria;

    @Override
    protected void onResume() {
        super.onResume();
        carregarProdutos(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pesquisar);

        containerCategorias = findViewById(R.id.container_categorias);
        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);
        icNovos = findViewById(R.id.ic_novos);
        icCupons = findViewById(R.id.ic_promocoes);
        icPesquisar = findViewById(R.id.ic_pesquisar);
        icFavoritos = findViewById(R.id.ic_favoritos);
        icConta = findViewById(R.id.ic_conta);
        recyclerViewProdutos = findViewById(R.id.recyclerViewProdutos);
        barraPesquisa = findViewById(R.id.barra_pesquisa);
        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));
        listaProdutos = new ArrayList<>();
        produtoAdapter = new ProdutoAdapter(listaProdutos);
        recyclerViewProdutos.setAdapter(produtoAdapter);
        limparCategoria = findViewById(R.id.limparCategoria);

        icNovos.setOnClickListener(v -> {
            Intent intent = new Intent(Pesquisar.this, Novos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icPesquisar.setOnClickListener(v -> {
         Intent intent = new Intent(Pesquisar.this, Pesquisar.class);
         startActivity(intent);
         overridePendingTransition(0, 0);
        });

        icCupons.setOnClickListener(v -> {
            Intent intent = new Intent(Pesquisar.this, Cupons.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icFavoritos.setOnClickListener(v -> {
            Intent intent = new Intent(Pesquisar.this, Favoritos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icConta.setOnClickListener(v -> {
            Intent intent = new Intent(Pesquisar.this, Conta.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        TextView categoriaMasculino = findViewById(R.id.categoria_masculino);
        categoriaMasculino.setOnClickListener(v -> filtrarProdutosPorCategoria(1));

        TextView categoriaFeminino = findViewById(R.id.categoria_feminino);
        categoriaFeminino.setOnClickListener(v -> filtrarProdutosPorCategoria(2));

        TextView categoriaInfantil = findViewById(R.id.categoria_infantil);
        categoriaInfantil.setOnClickListener(v -> filtrarProdutosPorCategoria(3));

        TextView categoriaAcessorios = findViewById(R.id.categoria_acessorios);
        categoriaAcessorios.setOnClickListener(v -> filtrarProdutosPorCategoria(4));

        TextView categoriaEquipamentos = findViewById(R.id.categoria_equipamentos);
        categoriaEquipamentos.setOnClickListener(v -> filtrarProdutosPorCategoria(5));

        TextView categoriaBolas = findViewById(R.id.categoria_bolas);
        categoriaBolas.setOnClickListener(v -> filtrarProdutosPorCategoria(6));

        barraPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    containerCategorias.setVisibility(View.VISIBLE);
                    recyclerViewProdutos.setVisibility(View.GONE);
                } else {
                    filtrarProdutos(s.toString());
                    containerCategorias.setVisibility(View.GONE);
                    recyclerViewProdutos.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        limparCategoria.setOnClickListener(v -> {
            Intent intent = new Intent(Pesquisar.this, Pesquisar.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        });

        carregarProdutos(null);
        recyclerViewProdutos.setVisibility(View.GONE);
    }

    private void filtrarProdutos(String query) {
        List<Produto> produtosFiltrados = new ArrayList<>();
        for (Produto produto : listaProdutos) {
            if (produto.getNome().toLowerCase().contains(query.toLowerCase())) {
                produtosFiltrados.add(produto);
            }
        }
        produtoAdapter.setListaProdutos(produtosFiltrados);
        produtoAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void carregarProdutos(Integer idCategoria) {
        Connection conn = ConexaoSQL.conectar(this);
        if (conn != null) {
            try {
                String query = "SELECT * FROM Produto WHERE statusProd = 'ATIVO' ";
                if (idCategoria != null) {
                    query += "AND idCategoria = ? ";
                }
                query += "ORDER BY idProduto DESC";

                PreparedStatement stmt = conn.prepareStatement(query);
                if (idCategoria != null) {
                    stmt.setInt(1, idCategoria);
                }
                ResultSet rs = stmt.executeQuery();

                listaProdutos.clear();
                while (rs.next()) {
                    Produto produto = new Produto();
                    produto.setIdProduto(rs.getInt("idProduto"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setFoto1(rs.getBytes("foto1"));
                    produto.setPreco(rs.getString("preco"));

                    listaProdutos.add(produto);
                }

                produtoAdapter.notifyDataSetChanged();

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

    private void filtrarProdutosPorCategoria(int idCategoria) {
        carregarProdutos(idCategoria);

        containerCategorias.setVisibility(View.GONE);
        recyclerViewProdutos.setVisibility(View.VISIBLE);
        limparCategoria.setVisibility(View.VISIBLE);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) recyclerViewProdutos.getLayoutParams();
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.marginTop_60dp);
        recyclerViewProdutos.setLayoutParams(params);
    }
}