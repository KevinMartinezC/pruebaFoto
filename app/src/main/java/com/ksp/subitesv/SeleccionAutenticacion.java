package com.ksp.subitesv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SeleccionAutenticacion extends AppCompatActivity {

    Toolbar bToolBar;
    Button BtnIrIniciarSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_autenticacion);
        bToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(bToolBar);
        getSupportActionBar().setTitle("Seleccionar opción");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BtnIrIniciarSesion =findViewById(R.id.btnIrInicioSeccion);

        BtnIrIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IraInicio();
            }
        });
    }
    public void  IraInicio(){
        Intent intent = new Intent(SeleccionAutenticacion.this, Login.class);
        startActivity(intent);
    }
}