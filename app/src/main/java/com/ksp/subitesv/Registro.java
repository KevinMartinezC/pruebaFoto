package com.ksp.subitesv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class Registro extends AppCompatActivity {
    SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mPref = getApplicationContext().getSharedPreferences("tipoUsuario", MODE_PRIVATE);
        String usuarioSeleccionado = mPref.getString("usuario","");//para traer el tipo de usuario seleccionado desde la MainActivity
        Toast.makeText(this, "Usuario Seleccionado: " + usuarioSeleccionado, Toast.LENGTH_SHORT).show();
    }
}