package com.example.notificacionespersonalizado;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ObservadorVolumen observadorVolumen;
    private RecibidorVolumen recibidorVolumen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        observadorVolumen = new ObservadorVolumen(new Handler(), getApplicationContext());
        getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, observadorVolumen);
        recibidorVolumen=new RecibidorVolumen();
        IntentFilter intentFilter = new IntentFilter("com.example.notificacionespersonalizado.VOLUMENCAMBIADO");
        registerReceiver(recibidorVolumen, intentFilter); //Se registra el recibidor dinamicamente y no en el manifest porque a partir de la API 26 se restringieron los broadcast implicitos y no se permite declararlos en el manifest
        //Se ha probado a hacerlo declarandolo en el manifest y funciona en un movil con la API 24, pero no en uno con la API 30
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (observadorVolumen != null) {
            unregisterReceiver(recibidorVolumen);
        }
    }
}