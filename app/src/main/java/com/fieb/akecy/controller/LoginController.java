package com.fieb.akecy.controller;

import android.content.Context;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.fieb.akecy.api.ConexaoSQL;

public class LoginController {

    public class LoginResult {
        public int status;
        public String nivelAcesso;

        public LoginResult(int status, String nivelAcesso) {
            this.status = status;
            this.nivelAcesso = nivelAcesso;
        }
    }

    public LoginResult validarLogin(Context context, String email, String senha) {
        try {
            PreparedStatement pstSenha = ConexaoSQL.conectar(context).prepareStatement(
                    "SELECT email, senha, statusUsuario, nivelAcesso FROM Usuario WHERE email=? AND senha=?");
            pstSenha.setString(1, email);
            pstSenha.setString(2, senha);
            ResultSet resSenha = pstSenha.executeQuery();

            if (resSenha.next()) {
                String statusUsuario = resSenha.getString("statusUsuario");
                String nivelAcesso = resSenha.getString("nivelAcesso");

                if (statusUsuario.equals("INATIVO")) {
                    return new LoginResult(3, nivelAcesso);
                } else {
                    return new LoginResult(0, nivelAcesso);
                }
            } else {
                return new LoginResult(2, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResult(-1, null);
        }
    }
}