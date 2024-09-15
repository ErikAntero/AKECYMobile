package com.fieb.akecy.controller;

import android.content.Context;
import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.model.Usuario;

public class UsuarioController {

    public boolean cadastrarUsuario(Context context, Usuario usuario) {
        try {
            String senhaCodificada = Base64.getEncoder().encodeToString(usuario.getSenha().getBytes());
            usuario.setSenha(senhaCodificada);

            Date dataAtual = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            String dataCadastroFormatada = dateFormat.format(dataAtual);

            PreparedStatement pst = ConexaoSQL.conectar(context).prepareStatement(
                    "INSERT INTO Usuario (nome, email, senha, cpf, telefone, sexo, dataNasc, dataCadastro, nivelAcesso, statusUsuario) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, CONVERT(smalldatetime, ?, 120), 'USER', 'ATIVO')");

            pst.setString(1, usuario.getNome());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getSenha());
            pst.setString(4, usuario.getCpf());
            pst.setString(5, usuario.getTelefone());
            pst.setString(6, null);
            pst.setString(7, usuario.getDataNasc());
            pst.setString(8, dataCadastroFormatada);

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
                usuario.setSexo(rs.getString("sexo"));
                return usuario;
            } else {
                return null;
            }

        } catch (Exception e) {
            Log.e("UsuarioController", "Erro ao buscar usuário por e-mail: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}