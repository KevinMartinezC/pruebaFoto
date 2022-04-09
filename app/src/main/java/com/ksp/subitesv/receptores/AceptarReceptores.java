package com.ksp.subitesv.receptores;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.ActivityChooserView;

import com.ksp.subitesv.actividades.conductor.MapReservaConductorActivity;
import com.ksp.subitesv.proveedores.ReservaClienteProveedor;

public class AceptarReceptores extends BroadcastReceiver {

    private ReservaClienteProveedor mReservaClienteProveedor;
    @Override
    public void onReceive(Context context, Intent intent) {

        String idCliente = intent.getExtras().getString("idCliente");
       mReservaClienteProveedor = new ReservaClienteProveedor();
       mReservaClienteProveedor.actualizarEstado(idCliente,"accept");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

        Intent intent1 = new Intent(context, MapReservaConductorActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.setAction(Intent.ACTION_RUN);
        context.startActivity(intent1);
    }
}
