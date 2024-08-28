package com.fieb.akecy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DatabaseHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DB_URL = "jdbc:sqlserver://192.168.15.200:1433;databaseName=AKECY;user=SA;password=@ITB123456";
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;

    private Context context;

    public DatabaseHelper(Context context) {
        this.context = context;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Log.d(TAG, "Driver JDBC carregado com sucesso.");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Erro ao carregar o driver JDBC: " + e.getMessage());
            showToast("Erro ao carregar o driver JDBC. Verifique as dependências.");
        }
    }

    // CREATE (Inserir novo usuário)
    public void inserirUsuario(String nome, String email, String telefone, LocalDate dataNasc, String cpf, String senha, OnUserInsertionListener listener) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                String sql = "INSERT INTO Usuarios (nome, email, telefone, dataNasc, cpf, senha, codStatusUsuario) VALUES (?, ?, ?, ?, ?, ?, ?)";
                return executeQueryWithRetries(sql, stmt -> {
                    try {
                        stmt.setString(1, nome);
                        stmt.setString(2, email);
                        stmt.setString(3, telefone);
                        stmt.setDate(4, java.sql.Date.valueOf(dataNasc.toString()));
                        stmt.setString(5, cpf);
                        stmt.setString(6, senha); // SENHA EM TEXTO PLANO - INSEGURO!
                        stmt.setBoolean(7, true);
                        return stmt.executeUpdate() > 0;
                    } catch (SQLException e) {
                        Log.e(TAG, "Erro SQL ao inserir usuário: " + e.getMessage());
                        e.printStackTrace();
                        throw e; // Relança a exceção para ser tratada no bloco catch externo
                    }
                });
            }

            @Override
            protected void onPostExecute(Boolean sucesso) {
                listener.onUserInsertionComplete(sucesso);
            }
        }.execute();
    }

    // READ (Verificar login)
    public void verificarLogin(String email, String senha, OnLoginVerificationListener listener) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                String sql = "SELECT * FROM Usuarios WHERE email = ? AND senha = ?";
                return executeQueryWithRetries(sql, stmt -> {
                    try {
                        stmt.setString(1, email);
                        stmt.setString(2, senha);

                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                boolean usuarioAtivo = rs.getBoolean("codStatusUsuario");
                                if (usuarioAtivo) {
                                    Log.d(TAG, "Login bem-sucedido para email: " + email);
                                    return true;
                                } else {
                                    Log.e(TAG, "Erro ao verificar login: Usuário inativo para email: " + email);
                                    return false;
                                }
                            } else {
                                Log.e(TAG, "Erro ao verificar login: Credenciais inválidas para email: " + email);
                            }
                        }
                        return false; // Login falhou
                    } catch (SQLException e) {
                        Log.e(TAG, "Erro SQL ao verificar login: " + e.getMessage());
                        e.printStackTrace();
                        throw e;
                    }
                });
            }

            @Override
            protected void onPostExecute(Boolean loginValido) {
                listener.onLoginVerificationComplete(loginValido);
            }
        }.execute();
    }

    // READ (Verificar se o email existe)
    public void verificarEmail(String email, OnEmailVerificationListener listener) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                String sql = "SELECT * FROM Usuarios WHERE email = ?";
                return executeQueryWithRetries(sql, stmt -> {
                    try {
                        stmt.setString(1, email);

                        try (ResultSet rs = stmt.executeQuery()) {
                            boolean emailExiste = rs.next();
                            Log.d(TAG, "Resultado da verificação de email: " + emailExiste);
                            return emailExiste;
                        }
                    } catch (SQLException e) {
                        Log.e(TAG, "Erro SQL ao verificar email: " + e.getMessage());
                        e.printStackTrace();
                        throw e;
                    }
                });
            }

            @Override
            protected void onPostExecute(Boolean emailExiste) {
                listener.onEmailVerificationComplete(emailExiste);
            }
        }.execute();
    }
    // UPDATE (Atualizar senha)
    public void atualizarSenha(String email, String novaSenha, OnPasswordUpdateListener listener) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                String sql = "UPDATE Usuarios SET senha = ? WHERE email = ?";
                return executeQueryWithRetries(sql, stmt -> {
                    try {
                        stmt.setString(1, novaSenha);
                        stmt.setString(2, email);

                        int rowsAffected = stmt.executeUpdate();

                        Log.d(TAG, "Linhas afetadas pela atualização de senha: " + rowsAffected);

                        return rowsAffected > 0;
                    } catch (SQLException e) {
                        Log.e(TAG, "Erro SQL ao atualizar senha: " + e.getMessage());
                        e.printStackTrace();
                        throw e;
                    }
                });
            }

            @Override
            protected void onPostExecute(Boolean sucesso) {
                listener.onPasswordUpdateComplete(sucesso);
            }
        }.execute();
    }

    // Método auxiliar para executar consultas com repetições e tratamento de erros
    private boolean executeQueryWithRetries(String sql, QueryExecutor executor) {
        int retryCount = 0;

        while (retryCount < MAX_RETRIES) {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    return executor.execute(pstmt);
                }
            } catch (SQLException e) {
                Log.e(TAG, "Erro SQL na operação do banco de dados (tentativa " + (retryCount + 1) + "): " + e.getMessage());
                e.printStackTrace();
                retryCount++;

                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ex) {
                    Log.e(TAG, "Erro durante o atraso da repetição: " + ex.getMessage());
                }
            }
        }

        Log.e(TAG, "Todas as tentativas da operação falharam.");
        return false;
    }

    // Interfaces de Callback
    public interface OnUserInsertionListener {
        void onUserInsertionComplete(boolean sucesso);
    }

    public interface OnLoginVerificationListener {
        void onLoginVerificationComplete(boolean loginValido);
    }

    public interface OnEmailVerificationListener {
        void onEmailVerificationComplete(boolean emailExiste);
    }

    public interface OnPasswordUpdateListener {
        void onPasswordUpdateComplete(boolean sucesso);
    }

    // Método auxiliar para exibir mensagens Toast na thread principal
    private void showToast(String message) {
        if (context != null) {
            ((AppCompatActivity) context).runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
        }
    }

    // Interface funcional para executar a consulta SQL dentro do bloco try-with-resources
    private interface QueryExecutor {
        boolean execute(PreparedStatement stmt) throws SQLException;
    }
}