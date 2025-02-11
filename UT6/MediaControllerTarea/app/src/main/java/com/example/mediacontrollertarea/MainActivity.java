package com.example.mediacontrollertarea;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mediacontrollertarea.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private MediaController mc;
    private MediaPlayer mp;
    private Handler h;
    private ActivityMainBinding binding;
    private Runnable actualizarTiempo;

    private ActivityResultLauncher<Intent> elegirCancionLauncher;
    private SharedPreferences preferencias;
    private static final String PREFERENCIAS_NOMBRE = "Preferencias";
    private static final String ULTIMA_POSICION = "posicion";
    private static final String ULTIMA_CANCION = "cancion";
    private static final String LISTA_CANCIONES = "listaCanciones";
    private int posicionCancion = 0;

    private ArrayList<String> listaCanciones;
    private int cancionIndex = 0;

    private static final String CANAL = "MediaControllerTarea";
    private MediaSessionCompat mediaSession;
    private NotificationManagerCompat notificationManager;
    private MusicReceiver musicReceiver;

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferencias = getSharedPreferences(PREFERENCIAS_NOMBRE, MODE_PRIVATE);
        recuperarListaCanciones();
        recuperarUltimaCancion();

        mc = new MediaController(this);
        mc.setMediaPlayer(this);
        mc.setAnchorView(findViewById(R.id.constraintLayout));

        if (listaCanciones.isEmpty()) mp = MediaPlayer.create(MainActivity.this, R.raw.eltiempopasara);
        else {
            Uri uri = Uri.parse(listaCanciones.get(cancionIndex));
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mp = MediaPlayer.create(MainActivity.this, uri);
        }
        mp.seekTo(posicionCancion);
        h = new Handler();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        mc.show(0);
                    }
                });
            }
        });
        elegirCancionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Uri audioUri = result.getData().getData();
                            if (audioUri != null) {
                                if (!listaCanciones.contains(audioUri.toString())) {
                                    getContentResolver().takePersistableUriPermission(audioUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                    System.out.println(audioUri.toString());
                                    listaCanciones.add(audioUri.toString());
                                    cancionIndex = listaCanciones.size() - 1;
                                    cambiarCancion();
                                } else Toast.makeText(MainActivity.this, "Esta cancion ya esta en la lista", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        binding.btnElegirCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionManager.comprobarPermisosAlmacenamiento(MainActivity.this)) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("audio/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    elegirCancionLauncher.launch(intent);
                } else {
                    PermissionManager.pedirPermisosAlmacenamiento(MainActivity.this);
                    Toast.makeText(MainActivity.this, "Permisos de almacenamiento denegados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        binding.btnStopReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset(v);
            }
        });

        actualizarTiempo = new Runnable() {
            @Override
            public void run() {
                int actual = mp.getCurrentPosition() / 1000;
                int total = mp.getDuration() / 1000;
                binding.txtTiempoCancion.setText(formatTime(actual) + " / " + formatTime(total));
                h.postDelayed(this, 1000); //Cada 1000ms
            }
        };

        mp.setOnPreparedListener(mp -> h.post(actualizarTiempo));

        binding.btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguienteCancion();
            }
        });
        binding.btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anteriorCancion();
            }
        });

        //NOTIFICACION
        musicReceiver = new MusicReceiver(MainActivity.this);
        System.out.println(MainActivity.this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("PLAY_PAUSE");
        filter.addAction("NEXT");
        filter.addAction("PREVIOUS");
        filter.addAction("STOP");
        registerReceiver(musicReceiver, filter);

        mediaSession = new MediaSessionCompat(this, "Musica");
        mediaSession.setActive(true);
        notificationManager = NotificationManagerCompat.from(this);
        crearCanal();
        mostrarNotificacion();
    }

    private void guardarUltimaCancion() {
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putInt(ULTIMA_CANCION, cancionIndex);
        editor.apply();
    }

    private void guardarListaCanciones() {
        SharedPreferences.Editor editor = preferencias.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listaCanciones);
        editor.putString(LISTA_CANCIONES, json);
        editor.apply();
    }

    private void recuperarListaCanciones() {
        String json = preferencias.getString(LISTA_CANCIONES, "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        listaCanciones = gson.fromJson(json, type);
    }

    private void recuperarUltimaCancion() {
        cancionIndex = preferencias.getInt(ULTIMA_CANCION, 0);
        posicionCancion = preferencias.getInt(ULTIMA_POSICION, 0);
    }


    public void siguienteCancion() {
        if (cancionIndex < listaCanciones.size() - 1) {
            cancionIndex++;
            cambiarCancion();
        } else {
            // Si estamos en la última canción, ir al principio
            cancionIndex = 0;
            cambiarCancion();
        }
    }

    public void anteriorCancion() {
        if (cancionIndex > 0) {
            cancionIndex--;
            cambiarCancion();
        } else {
            // Si estamos en la primera canción, ir al final
            cancionIndex = listaCanciones.size() - 1;
            cambiarCancion();
        }
    }

    public void cambiarCancion() {
        if(!listaCanciones.isEmpty()){
            if (mp != null) {
                if (mp.isPlaying()) {
                    mp.stop();
                }
                mp.release();
            }
            try {
                Uri cancionUri = Uri.parse(listaCanciones.get(cancionIndex));
                getContentResolver().takePersistableUriPermission(cancionUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                mp = MediaPlayer.create(MainActivity.this, cancionUri);
                mp.seekTo(0);
                mp.start();
            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "No hay permisos para acceder al archivo", Toast.LENGTH_SHORT).show();
            }
        }
        else Toast.makeText(MainActivity.this, "No hay canciones en la lista", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null) {
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putInt(ULTIMA_POSICION, mp.getCurrentPosition());
            editor.apply();
        }
        guardarUltimaCancion();
        guardarListaCanciones();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicReceiver != null) {
            unregisterReceiver(musicReceiver);
        }
    }
    /*@Override
    protected void onResume() {
        super.onResume();
        posicionCancion=preferencias.getInt(ULTIMA_POSICION,mp.getCurrentPosition());
        mp.seekTo(posicionCancion);
    }*/

    public void show(View v) {
        mc.show();
    }

    public void stop() {
        if (mp.isPlaying()) {
            mp.stop();
            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void hide(View v) {
        mc.hide();
    }

    public void reset(View v) {
        if (mp.isPlaying()) {
            mp.pause();
        }
        mp.seekTo(0);
        binding.txtTiempoCancion.setText("00:00 / " + formatTime(mp.getDuration() / 1000));
    }

    @Override
    public void start() {
        if (!mp.isPlaying()) {
            mp.start();
            h.post(actualizarTiempo);
        }
    }

    @Override
    public void pause() {
        if (mp.isPlaying()) mp.pause();
    }

    @Override
    public int getDuration() {
        return mp.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mp.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        mp.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return mp.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mp.getAudioSessionId();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            if (!mc.isShowing())
                mc.show(0);
            else
                mc.hide();
        return false;
    }

    private String formatTime(int segundos) {
        int minutos = segundos / 60;
        int sec = segundos % 60;
        return String.format("%02d:%02d", minutos, sec);
    }


    private void crearCanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nombre = "Controles";
            String descripcion = "Controles para la aplicacion de MediaController";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CANAL, nombre, importance);
            channel.setDescription(descripcion);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void mostrarNotificacion() {
        PendingIntent playPauseIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, MusicReceiver.class).setAction("PLAY_PAUSE"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent nextIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, MusicReceiver.class).setAction("NEXT"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent previousIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, MusicReceiver.class).setAction("PREVIOUS"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent stopIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, MusicReceiver.class).setAction("STOP"), PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CANAL)
                .setContentTitle("Música")
                .setContentText("Artista - Canción")
                .setSmallIcon(R.drawable.baseline_music_note_24)
                .addAction(R.drawable.anterior, "Anterior", previousIntent)
                .addAction(isPlaying() ? R.drawable.pause : R.drawable.start, isPlaying() ? "Pausar" : "Reanudar", playPauseIntent)
                .addAction(R.drawable.siguiente, "Siguiente", nextIntent)
                .addAction(R.drawable.stop, "Detener", stopIntent)
                .setOngoing(true)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken()))
                .build();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            PermissionManager.pedirPermisosNotificaciones(MainActivity.this);
            return;
        }
        notificationManager.notify(1, notification);
    }
}