package es.riberadeltajo.notificaciones;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b=findViewById(R.id.btnEnvia);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creaNotificacion("¡Notificación  de pedido nuevo!");
            }
        });

        if(checkSelfPermission("android.permission.RECEIVE_SMS")!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{"android.permission.RECEIVE_SMS"},1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
    }

    public void creaNotificacion(String texto){
        // paso 1
        NotificationCompat.Builder constructorNotif = new NotificationCompat.Builder(this, "miCanal")
                .setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle("Has recibido una notificación")
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // paso 2
        // ¿Qué actividad se abre al pulsar el usuario sobre la notificación?
        Intent resultadoIntent = new Intent(this, NotificacionPulsada.class);

        TaskStackBuilder pila = TaskStackBuilder.create(this);
        pila.addParentStack(MainActivity.class);

        // Añade el Intent que comienza la Actividad al inicio de la pila
        pila.addNextIntent(resultadoIntent);

        // Especifica FLAG_IMMUTABLE junto con FLAG_UPDATE_CURRENT
        PendingIntent resultadoPendingIntent = pila.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        constructorNotif.setContentIntent(resultadoPendingIntent);

        // paso 3: Creación del canal (si la API>26 (oreo)) y el envío de la notificación
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel canal = new NotificationChannel("miCanal", "Canal de notificacion de prueba",
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(canal);
        notificationManager.notify(1, constructorNotif.build());
    }
}