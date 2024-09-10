package com.fieb.akecy.controller;

import android.content.Context;
import android.widget.Toast;

import com.fieb.akecy.api.ConexaoSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FavoritoController {

    public boolean favoritarProduto(Context context, int idUsuario, int idProduto) {
        try (Connection conn = ConexaoSQL.conectar(context)) {
            if (conn != null) {
                if (isProdutoFavoritado(conn, idUsuario, idProduto)) {
                    String deleteQuery = "DELETE FROM Favorito WHERE idUsuario = ? AND idProduto = ?";
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                        deleteStmt.setInt(1, idUsuario);
                        deleteStmt.setInt(2, idProduto);
                        int rowsAffected = deleteStmt.executeUpdate();
                        return rowsAffected > 0;
                    }
                } else {
                    String insertQuery = "INSERT INTO Favorito (idUsuario, idProduto) VALUES (?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, idUsuario);
                        insertStmt.setInt(2, idProduto);
                        int rowsAffected = insertStmt.executeUpdate();
                        return rowsAffected > 0;
                    }
                }
            } else {
                Toast.makeText(context, "Erro de conexão com o banco de dados", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro ao favoritar/desfavoritar produto", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isProdutoFavoritado(Connection conn, int idUsuario, int idProduto) throws SQLException {
        String query = "SELECT * FROM Favorito WHERE idUsuario = ? AND idProduto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}