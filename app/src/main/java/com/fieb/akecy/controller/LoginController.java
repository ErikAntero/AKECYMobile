package com.fieb.akecy.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fieb.akecy.api.ConexaoSQL;

public class LoginController {

    public void validarLogin(Context context, String email, String senha, OnLoginValidatedListener listener) {
        new ValidarLoginTask(context, email, senha, listener).execute();
    }

    private static class ValidarLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final Context context;
        private final String email;
        private final String senha;
        private final OnLoginValidatedListener listener;

        public ValidarLoginTask(Context context, String email, String senha, OnLoginValidatedListener listener) {
            this.context = context;
            this.email = email;
            this.senha = senha;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Connection conn = ConexaoSQL.conectar(context);

                if (conn == null) {
                    return false;
                }

                String sql = "SELECT codStatusUsuario FROM Usuarios WHERE email = ? AND senha = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, email);
                pst.setString(2, senha);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    int codStatusUsuario = rs.getInt("codStatusUsuario"); // Recupere como inteiro
                    Log.d("LoginController", "codStatusUsuario = " + codStatusUsuario);

                    return codStatusUsuario == 1; // Compare com 1 (ativo)
                } else {
                    Log.d("LoginController", "Nenhum usuário encontrado com o email e senha fornecidos");
                    return false;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean loginValido) {
            Log.d("LoginController", "onPostExecute chamado. loginValido = " + loginValido); // Log para verificar se o método é chamado

            if (loginValido) {
                listener.onLoginSuccess();
            } else {
                listener.onLoginFailure();
            }
        }
    }

    public interface OnLoginValidatedListener {
        void onLoginSuccess();
        void onLoginFailure();
    }
}