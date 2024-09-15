package com.fieb.akecy.view.conta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fieb.akecy.R;
import com.fieb.akecy.view.favoritos.Favoritos;
import com.fieb.akecy.view.novos.Novos;
import com.fieb.akecy.view.pesquisar.Pesquisar;
import com.fieb.akecy.view.cupons.Cupons;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.fieb.akecy.controller.UsuarioController;
import com.fieb.akecy.model.Usuario;
import com.fieb.akecy.view.usuario.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Conta extends AppCompatActivity {

    ImageView icNovos, icCupons, icPesquisar, icFavoritos, icConta;
    TextView nome, email, cpf, dataDeNascimento, sexo, telefone;
    Button editar, alterar, sair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conta);

        icNovos = findViewById(R.id.ic_novos);
        icCupons = findViewById(R.id.ic_promocoes);
        icPesquisar = findViewById(R.id.ic_pesquisar);
        icFavoritos = findViewById(R.id.ic_favoritos);
        icConta = findViewById(R.id.ic_conta);
        nome = findViewById(R.id.pessoalNome);
        email = findViewById(R.id.pessoalEmail);
        cpf = findViewById(R.id.pessoalCPF);
        dataDeNascimento = findViewById(R.id.pessoalDataDeNascimento);
        sexo = findViewById(R.id.pessoalSexo);
        telefone = findViewById(R.id.pessoalTelefone);
        editar = findViewById(R.id.editarDados);
        alterar = findViewById(R.id.alterarSenha);
        sair = findViewById(R.id.sairDaConta);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");

        UsuarioController usuarioController = new UsuarioController();
        Usuario usuario = usuarioController.buscarUsuarioPorEmail(this, userEmail);

        if (usuario != null) {
            nome.setText(usuario.getNome());
            email.setText(usuario.getEmail());
            cpf.setText(usuario.getCpf());
            dataDeNascimento.setText(usuario.getDataNasc());
            telefone.setText(usuario.getTelefone());
            if (usuario.getSexo() == null) {
                sexo.setText("-");
            } else {
                sexo.setText(usuario.getSexo());
            }
        } else {
            Toast.makeText(Conta.this, "Erro ao recuperar dados do usuário", Toast.LENGTH_SHORT).show();
        }

        icNovos.setOnClickListener(v -> {
            Intent intent = new Intent(Conta.this, Novos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icCupons.setOnClickListener(v -> {
            Intent intent = new Intent(Conta.this, Cupons.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icPesquisar.setOnClickListener(v -> {
            Intent intent = new Intent(Conta.this, Pesquisar.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        icFavoritos.setOnClickListener(v -> {
            Intent intent = new Intent(Conta.this, Favoritos.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        editar.setOnClickListener(v -> {
            Intent intent = new Intent(Conta.this, EditarDados.class);
            startActivity(intent);
        });

        alterar.setOnClickListener(v -> {
            Intent intent = new Intent(Conta.this, AlterarSenha.class);
            startActivity(intent);
        });

        sair.setOnClickListener(v -> {
            logout();
        });
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("isLoggedIn", false);

        editor.apply();

        Intent intent = new Intent(Conta.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        carregarDadosDoUsuario();
    }

    private void carregarDadosDoUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");

        UsuarioController usuarioController = new UsuarioController();
        Usuario usuario = usuarioController.buscarUsuarioPorEmail(this, userEmail);

        if (usuario != null) {
            nome.setText(usuario.getNome());
            email.setText(usuario.getEmail());
            cpf.setText(usuario.getCpf());
            SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat formatoDesejado = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date dataNasc = formatoOriginal.parse(usuario.getDataNasc());
                String dataNascFormatada = formatoDesejado.format(dataNasc);
                dataDeNascimento.setText(dataNascFormatada);
            } catch (ParseException e) {
                e.printStackTrace();
                dataDeNascimento.setText(usuario.getDataNasc());
            }
            telefone.setText(usuario.getTelefone());
            if (usuario.getSexo() == null) {
                sexo.setText("-");
            } else {
                sexo.setText(usuario.getSexo());
            }
        } else {
            Toast.makeText(Conta.this, "Erro ao recuperar dados do usuário", Toast.LENGTH_SHORT).show();
        }
    }
}