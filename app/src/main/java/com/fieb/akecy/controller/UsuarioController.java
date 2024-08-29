package com.fieb.akecy.controller;

import android.content.Context;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fieb.akecy.api.ConexaoSQL;
import com.fieb.akecy.model.Usuario;

public class UsuarioController {

    public boolean cadastrarUsuario(Context context, Usuario usuario) {
        try {
            PreparedStatement pst = ConexaoSQL.conectar(context).prepareStatement(
                    "INSERT INTO Usuarios (nome, email, senha, cpf , telefone, dataNasc, nivelAcesso, statusUsuario) " +
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
}