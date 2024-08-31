package com.fieb.akecy.controller;

import android.content.Context;
import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.model.Usuario;

public class UsuarioController {

    public boolean cadastrarUsuario(Context context, Usuario usuario) {
        try {
            PreparedStatement pst = ConexaoSQL.conectar(context).prepareStatement(
                    "INSERT INTO Usuario (nome, email, senha, cpf , telefone, dataNasc, nivelAcesso, statusUsuario) " +
                            "VALUES (?, ?, ?, ?, ?, ?, 'USER', 'ATIVO')");
            pst.setString(1, usuario.getNome());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getSenha());
            pst.setString(4, usuario.getCpf());
            pst.setString(5, usuario.getTelefone());
            pst.setString(6, usuario.getDataNasc());

            int linhasAfetadas = pst.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario buscarUsuarioPorEmail(Context context, String email) {
        try {
            Statement st = ConexaoSQL.conectar(context).createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Usuario WHERE email='" + email + "'");

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setDataNasc(rs.getString("dataNasc"));
                usuario.setTelefone(rs.getString("telefone"));
                return usuario;
            } else {
                return null;
            }

        } catch (Exception e) {
            Log.e("UsuarioController", "Erro ao buscar usuário por e-mail: " + e.getMessage()); // Log de erro detalhado
            e.printStackTrace();
            return null;
        }
    }
}