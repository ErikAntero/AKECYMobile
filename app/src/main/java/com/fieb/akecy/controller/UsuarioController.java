package com.fieb.akecy.controller;

import android.content.Context;
import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.model.Usuario;

public class UsuarioController {

    public boolean cadastrarUsuario(Context context, Usuario usuario) {
        try {
            // Codifica a senha do usuário
            String senhaCodificada = Base64.getEncoder().encodeToString(usuario.getSenha().getBytes());
            usuario.setSenha(senhaCodificada);

            // Obtém a data atual para data de cadastro
            Date dataAtual = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String dataCadastroFormatada = dateFormat.format(dataAtual);

            // Formata a data de nascimento para o formato aceito pelo banco de dados (yyyy-MM-dd)
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Correção do formato!
            java.util.Date parsedDate = dateFormatter.parse(usuario.getDataNasc());
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

            // Prepara a query SQL com parâmetros para evitar SQL Injection
            PreparedStatement pst = ConexaoSQL.conectar(context).prepareStatement(
                    "INSERT INTO Usuario (nome, email, senha, cpf, telefone, sexo, dataNasc, dataCadastro, nivelAcesso, statusUsuario) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'USER', 'ATIVO')");

            // Define os valores dos parâmetros na query
            pst.setString(1, usuario.getNome());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getSenha());
            pst.setString(4, usuario.getCpf());
            pst.setString(5, usuario.getTelefone());
            pst.setString(6, usuario.getSexo());
            pst.setDate(7, sqlDate); // Define a data de nascimento formatada
            pst.setString(8, dataCadastroFormatada);

            // Executa a query e verifica se alguma linha foi afetada
            int linhasAfetadas = pst.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException | ParseException e) {
            Log.e("UsuarioController", "Erro ao cadastrar usuário: " + e.getMessage());
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