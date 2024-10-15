package com.fieb.akecy.view.produtoCompleto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.controller.FavoritoController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoCompleto extends AppCompatActivity {

    ImageView foto1, foto2, foto3, foto4, foto5;
    TextView nome, descricao, descricaoCompleta, tamanhosDisponiveis, preco;
    ImageButton voltar, favoritoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produto_completo);

        foto1 = findViewById(R.id.foto1);
        foto2 = findViewById(R.id.foto2);
        foto3 = findViewById(R.id.foto3);
        foto4 = findViewById(R.id.foto4);
        foto5 = findViewById(R.id.foto5);
        nome = findViewById(R.id.nome);
        descricao = findViewById(R.id.descricao);
        descricaoCompleta = findViewById(R.id.descricao_completa);
        tamanhosDisponiveis = findViewById(R.id.tamanhos2);
        preco = findViewById(R.id.preco);
        voltar = findViewById(R.id.voltar_ed);
        favoritoButton = findViewById(R.id.favorito_button); // Certifique-se de que você tem esse botão no seu layout

        int idProduto = getIntent().getIntExtra("idProduto", -1);

        if (idProduto != -1) {
            com.fieb.akecy.model.Produto produto = buscarProdutoNoBanco(idProduto);

            if (produto != null) {
                preencherTelaComDadosDoProduto(produto);

                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                int idUsuario = sharedPreferences.getInt("userId", -1);

                if (idUsuario != -1) {
                    final boolean[] isFavorito = {isProdutoFavoritado(idUsuario, idProduto)};

                    if (isFavorito[0]) {
                        favoritoButton.setImageResource(R.drawable.ic_favorito2);
                    } else {
                        favoritoButton.setImageResource(R.drawable.ic_favorito);
                    }

                    favoritoButton.setOnClickListener(v -> {
                        FavoritoController favoritoController = new FavoritoController();
                        boolean sucesso = favoritoController.favoritarProduto(this, idUsuario, idProduto);

                        if (sucesso) {
                            if (isFavorito[0]) {
                                favoritoButton.setImageResource(R.drawable.ic_favorito);
                                isFavorito[0] = false;
                            } else {
                                favoritoButton.setImageResource(R.drawable.ic_favorito2);
                                isFavorito[0] = true;
                            }
                        } else {
                            Toast.makeText(this, "Erro ao favoritar/desfavoritar produto", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    favoritoButton.setEnabled(false);
                }
            } else {
                Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "ID de produto inválido", Toast.LENGTH_SHORT).show();
            finish();
        }

        voltar.setOnClickListener(v -> finish());
    }

    private com.fieb.akecy.model.Produto buscarProdutoNoBanco(int idProduto) {
        com.fieb.akecy.model.Produto produto = null;
        try (Connection conn = ConexaoSQL.conectar(this)) {
            if (conn != null) {
                String query = "SELECT * FROM Produto WHERE idProduto = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, idProduto);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            produto = new com.fieb.akecy.model.Produto();
                            produto.setIdProduto(rs.getInt("idProduto"));
                            produto.setNome(rs.getString("nome"));
                            produto.setDescricao(rs.getString("descricao"));
                            produto.setDescricao_completa(rs.getString("descricaoCompleta"));
                            produto.setTamanhos_disponiveis(rs.getString("tamanhosDisponiveis"));
                            produto.setFoto1(rs.getBytes("foto1"));
                            produto.setFoto2(rs.getBytes("foto2"));
                            produto.setFoto3(rs.getBytes("foto3"));
                            produto.setFoto4(rs.getBytes("foto4"));
                            produto.setFoto5(rs.getBytes("foto5"));
                            produto.setPreco(rs.getString("preco"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao buscar produto", Toast.LENGTH_SHORT).show();
        }
        return produto;
    }

    @SuppressLint("SetTextI18n")
    private void preencherTelaComDadosDoProduto(com.fieb.akecy.model.Produto produto) {
        preencherImageViewComBytes(foto1, produto.getFoto1());
        preencherImageViewComBytes(foto2, produto.getFoto2());
        preencherImageViewComBytes(foto3, produto.getFoto3());
        preencherImageViewComBytes(foto4, produto.getFoto4());
        preencherImageViewComBytes(foto5, produto.getFoto5());

        if (produto.getNome() != null) {
            nome.setText(produto.getNome());
        }
        if (produto.getDescricao() != null) {
            descricao.setText(produto.getDescricao());
        }
        if (produto.getDescricao_completa() != null) {
            descricaoCompleta.setText(produto.getDescricao_completa());
        }
        if (produto.getTamanhos_disponiveis() != null) {
            tamanhosDisponiveis.setText(produto.getTamanhos_disponiveis());
        }
        if (produto.getPreco() != null) {
            preco.setText("R$ " + produto.getPreco());
        }
    }

    private void preencherImageViewComBytes(ImageView imageView, byte[] imageBytes) {
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.img_black);
        }
    }

    private boolean isProdutoFavoritado(int idUsuario, int idProduto) {
        try (Connection conn = ConexaoSQL.conectar(this)) {
            if (conn != null) {
                String query = "SELECT * FROM Favorito WHERE idUsuario = ? AND idProduto = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, idUsuario);
                    stmt.setInt(2, idProduto);
                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next();
                    }
                }
            } else {
                Toast.makeText(this, "Erro de conexão com o banco de dados", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao verificar favorito", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}