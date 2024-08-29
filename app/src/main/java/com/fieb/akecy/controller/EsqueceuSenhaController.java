package com.fieb.akecy.controller;

import android.content.Context;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.fieb.akecy.api.ConexaoSQL;

public class EsqueceuSenhaController {

    public boolean verificarEmail(Context context, String email) {
        try {
            PreparedStatement pst = ConexaoSQL.conectar(context).prepareStatement(
                    "SELECT COUNT(*) FROM Usuarios WHERE LOWER(email)=LOWER(?)");
            pst.setString(1, email);
            ResultSet res = pst.executeQuery();
            res.next();
            int count = res.getInt(1);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizarSenha(Context context, String email, String novaSenha) {
        try {
            PreparedStatement pst = ConexaoSQL.conectar(context).prepareStatement(
                    "UPDATE Usuarios SET senha=? WHERE LOWER(email)=LOWER(?)");
            pst.setString(1, novaSenha);
            pst.setString(2, email);
            int linhasAfetadas = pst.executeUpdate();
            return linhasAfetadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}