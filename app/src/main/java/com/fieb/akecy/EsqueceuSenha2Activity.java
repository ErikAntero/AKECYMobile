package com.fieb.akecy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EsqueceuSenha2Activity extends AppCompatActivity {

    private EditText codigoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esqueceusenha2);

        codigoEditText = findViewById(R.id.esque2_codigo);
        codigoEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        codigoEditText.addTextChangedListener(new CodigoTextWatcher());

        ImageButton voltarButton = findViewById(R.id.esque2_voltar);
        voltarButton.setOnClickListener(v -> onBackPressed());

        Button continuarButton = findViewById(R.id.esque2_btnContinuar);
        continuarButton.setOnClickListener(v -> {
            String codigo = codigoEditText.getText().toString().trim();
            if (isValidCodigo(codigo)) {
                telaEsqueceuSenha3();
            } else {
                Toast.makeText(EsqueceuSenha2Activity.this, "Digite um código válido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void telaEsqueceuSenha3() {
        Intent intent = new Intent(this, EsqueceuSenha3Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private boolean isValidCodigo(String codigo) {
        return codigo.length() == 7;
    }

    private class CodigoTextWatcher implements TextWatcher {

        private boolean isFormatting;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isFormatting) {
                return;
            }
            isFormatting = true;

            String codigo = s.toString().replaceAll("\\s+", "");

            if (codigo.length() > 6) {
                codigo = codigo.substring(0, 6);
            }

            if (codigo.length() > 3) {
                StringBuilder formatted = new StringBuilder();
                formatted.append(codigo.substring(0, 3));
                formatted.append(" ");
                formatted.append(codigo.substring(3));
                codigoEditText.removeTextChangedListener(this);
                codigoEditText.setText(formatted.toString());
                codigoEditText.setSelection(formatted.length());
                codigoEditText.addTextChangedListener(this);
            } else {
                codigoEditText.removeTextChangedListener(this);
                codigoEditText.setText(codigo);
                codigoEditText.setSelection(codigo.length());
                codigoEditText.addTextChangedListener(this);
            }


            if (codigo.length() < 6) {
                codigoEditText.setError("Digite 6 dígitos");
            } else {
                codigoEditText.setError(null);
            }

            isFormatting = false;
        }
    }
}
