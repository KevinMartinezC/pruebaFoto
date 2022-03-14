package com.ksp.subitesv.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ksp.subitesv.R;

public class MainActivity extends AppCompatActivity {
    Button btnCliente;
    Button btnConductor;

    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref = getApplicationContext().getSharedPreferences("tipoUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();

        btnCliente = findViewById(R.id.BtnCliente);
        btnConductor = findViewById(R.id.BtnConductor);

        btnCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("usuario", "cliente");
                editor.apply();//Para guardar el dato y edintificar si es cliente o conductor
                irSeleccionAutenticacion();

            }
        });

        btnConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("usuario", "conductor");
                editor.apply();
                irSeleccionAutenticacion();
            }
        });
    }

    private void irSeleccionAutenticacion() {
        Intent intent = new Intent(this, SeleccionAutenticacionActivity.class);
        startActivity(intent);
    }
}