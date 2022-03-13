package com.ksp.subitesv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SeleccionAutenticacion extends AppCompatActivity {

    Toolbar bToolBar;
    Button btnIrIniciarSesion, btnRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_autenticacion);
        bToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(bToolBar);
        getSupportActionBar().setTitle("Seleccionar opción");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnIrIniciarSesion = findViewById(R.id.btnIrInicioSeccion);
        btnRegistrarse = findViewById(R.id.btnIraRegistrarse);

        btnIrIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IraInicio();
            }
        });
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IraRegistro();
            }
        });
    }

    public void IraInicio() {
        Intent intent = new Intent(SeleccionAutenticacion.this, Login.class);
        startActivity(intent);
    }

    public void IraRegistro() {
        Intent intent = new Intent(SeleccionAutenticacion.this, Registro.class);
        startActivity(intent);
    }
}