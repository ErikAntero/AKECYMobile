package com.fieb.akecy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fieb.akecy.R;
import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.controller.FavoritoController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Produto extends RecyclerView.Adapter<Produto.ProdutoViewHolder> {

    private List<com.fieb.akecy.model.Produto> listaProdutos;
    private Context context;

    public Produto(List<com.fieb.akecy.model.Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.produto_item, parent, false);
        return new ProdutoViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        com.fieb.akecy.model.Produto produto = listaProdutos.get(position);

        byte[] imageBytes = produto.getFoto1();

        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imagemProduto.setImageBitmap(bitmap);
        } else {
            holder.imagemProduto.setImageResource(R.drawable.img_placeholder);
        }

        holder.nomeProduto.setText(produto.getNome());
        holder.descricaoProduto.setText(produto.getDescricao());
        holder.precoProduto.setText("R$ " + produto.getPreco());

        ImageButton favoritoButton = holder.favoritoButton;

        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        int idUsuario = sharedPreferences.getInt("userId", -1);

        if (idUsuario != -1) {
            boolean isFavorito = isProdutoFavoritado(idUsuario, produto.getIdProduto());

            if (isFavorito) {
                favoritoButton.setImageResource(R.drawable.ic_favorito2);
            } else {
                favoritoButton.setImageResource(R.drawable.ic_favorito);
            }

            favoritoButton.setOnClickListener(v -> {
                FavoritoController favoritoController = new FavoritoController();
                boolean sucesso = favoritoController.favoritarProduto(context, idUsuario, produto.getIdProduto());

                if (sucesso) {
                    if (isFavorito) {
                        favoritoButton.setImageResource(R.drawable.ic_favorito);
                    } else {
                        favoritoButton.setImageResource(R.drawable.ic_favorito2);
                    }
                } else {
                    Toast.makeText(context, "Erro ao favoritar/desfavoritar produto", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            favoritoButton.setEnabled(false);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.fieb.akecy.view.produto.Produto.class);
            intent.putExtra("idProduto", produto.getIdProduto());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagemProduto;
        TextView nomeProduto;
        TextView descricaoProduto;
        TextView precoProduto;
        ImageButton favoritoButton;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagemProduto = itemView.findViewById(R.id.imagem_produto);
            nomeProduto = itemView.findViewById(R.id.nome_produto);
            descricaoProduto = itemView.findViewById(R.id.descricao_produto);
            precoProduto = itemView.findViewById(R.id.preco_produto);
            favoritoButton = itemView.findViewById(R.id.favorito_button);
        }
    }

    private boolean isProdutoFavoritado(int idUsuario, int idProduto) {
        try (Connection conn = ConexaoSQL.conectar(context)) {
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
                Toast.makeText(context, "Erro de conexão com o banco de dados", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro ao verificar favorito", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}