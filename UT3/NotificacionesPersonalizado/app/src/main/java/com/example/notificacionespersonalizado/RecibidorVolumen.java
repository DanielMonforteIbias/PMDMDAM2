package com.example.notificacionespersonalizado;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

public class RecibidorVolumen extends BroadcastReceiver {
    int volumenNuevo=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if("com.example.notificacionespersonalizado.VOLUMENCAMBIADO".equals(intent.getAction())){
            volumenNuevo=intent.getIntExtra("Volumen",0);
            creaNotificacion("Volumen cambiado a "+volumenNuevo,context);
        }
    }

    public void creaNotificacion(String texto, Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Si la versión de Android requiere un canal
            NotificationChannel channel = new NotificationChannel("miCanal","Notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel); //Lo creamos
        }
        NotificationCompat.Builder constructorNotif = new NotificationCompat.Builder(context,"miCanal") //Importante que tenga la misma id que el canal creado
                .setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle("Has recibido una notificación")
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent resultadoIntent = new Intent(context, NotificacionPulsada.class);
        resultadoIntent.putExtra("Volumen",volumenNuevo);
        TaskStackBuilder pila = TaskStackBuilder.create(context);
        pila.addParentStack(MainActivity.class);
        pila.addNextIntent(resultadoIntent);

        // Especifica FLAG_IMMUTABLE junto con FLAG_UPDATE_CURRENT
        PendingIntent resultadoPendingIntent = pila.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        constructorNotif.setContentIntent(resultadoPendingIntent);
        notificationManager.notify(1, constructorNotif.build());
    }
}
