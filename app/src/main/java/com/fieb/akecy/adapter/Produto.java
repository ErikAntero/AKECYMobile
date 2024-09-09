package com.fieb.akecy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fieb.akecy.R;

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

    };

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagemProduto;
        TextView nomeProduto;
        TextView descricaoProduto;
        TextView precoProduto;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagemProduto = itemView.findViewById(R.id.imagem_produto);
            nomeProduto = itemView.findViewById(R.id.nome_produto);
            descricaoProduto = itemView.findViewById(R.id.descricao_produto);
            precoProduto = itemView.findViewById(R.id.preco_produto);
        }
    }
}