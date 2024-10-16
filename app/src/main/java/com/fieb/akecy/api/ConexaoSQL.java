package com.fieb.akecy.api;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSQL {

    public static Connection conectar(Context context) {
        Connection conn = null;
        try {
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://AKECYBD.mssql.somee.com:1433;" +
                    "databaseName=AKECYBD;user=arthuralvs2_SQLLogin_1;password=w79kdydnmc");

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "Servidor Indisponível", Toast.LENGTH_LONG).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return conn;
    }
}