package com.ksp.subitesv.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ksp.subitesv.R;
import com.ksp.subitesv.includes.AppToolBar;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText TxtCorreo, TxtContrasena;
    Button BtnIniciarSesion;
    FirebaseAuth mAuth;
    DatabaseReference mBasedeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppToolBar.mostrar(this, "Iniciar Sesion", true);

        TxtContrasena = findViewById(R.id.TxtContrasena);
        TxtCorreo = findViewById(R.id.TxtCorreo);
        BtnIniciarSesion = findViewById(R.id.BtnIniciarSesion);

        mAuth = FirebaseAuth.getInstance();
        mBasedeDatos = FirebaseDatabase.getInstance().getReference();

        BtnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });


    }

    private void iniciarSesion() {
        String correo = TxtCorreo.getText().toString();
        String contrasena = TxtContrasena.getText().toString();

        if (!correo.isEmpty() && !contrasena.isEmpty()) {
            if (contrasena.length() >= 6) {
                mAuth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//Login fue realizado Con exito
                            Toast.makeText(LoginActivity.this, getText(R.string.InicioSeccionExitoso), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, getText(R.string.CorreoContraInvalidos), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, getText(R.string.ContrasenaMayora6Digitos), Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, getText(R.string.CorreoContraObligatorios), Toast.LENGTH_SHORT).show();
        }
    }



}