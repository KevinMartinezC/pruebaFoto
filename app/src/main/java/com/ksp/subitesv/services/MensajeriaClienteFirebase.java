package com.ksp.subitesv.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ksp.subitesv.canal.NotificacionHelper;

import java.util.Map;

public class MensajeriaClienteFirebase extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        RemoteMessage.Notification notificacion = message.getNotification();
        Map<String, String> data = message.getData();
        String title = data.get("title");
        String body = data.get("body");

        if (title != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showNotificacionApiOreo(title, body);
            } else {
                showNotificacion(title, body);
            }
        }
    }

    private void showNotificacion(String title, String body) {
        PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificacionHelper notificacionHelper = new NotificacionHelper(getBaseContext());
        NotificationCompat.Builder builder = notificacionHelper.obtenerNotificacionOldAPI(title, body, intent, sonido);
        notificacionHelper.obtenerManager().notify(1, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificacionApiOreo(String title, String body) {
        PendingIntent intent= PendingIntent.getBroadcast(getBaseContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT |
                PendingIntent.FLAG_IMMUTABLE);
        //PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificacionHelper notificacionHelper = new NotificacionHelper(getBaseContext());
        Notification.Builder builder = notificacionHelper.obtenerNotificacion(title, body, intent, sonido);
        notificacionHelper.obtenerManager().notify(1, builder.build());

    }
}
