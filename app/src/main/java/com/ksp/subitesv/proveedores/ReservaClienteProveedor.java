package com.ksp.subitesv.proveedores;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ksp.subitesv.modulos.ReservaCliente;

import java.util.HashMap;
import java.util.Map;

public class ReservaClienteProveedor {

    private DatabaseReference mBasedeDatos;

    public ReservaClienteProveedor() {
        mBasedeDatos = FirebaseDatabase.getInstance().getReference().child("ReservaCliente");//Creacion de nodo
    }
    public Task<Void> crear(ReservaCliente reservaCliente){
        return mBasedeDatos.child(reservaCliente.getIdCliente()).setValue(reservaCliente);
    }
    public Task<Void> actualizarEstado(String idReservaCliente, String estado){
        Map<String, Object> map = new HashMap<>();
        map.put("estado", estado);
        return  mBasedeDatos.child(idReservaCliente).updateChildren(map);
    }

}
