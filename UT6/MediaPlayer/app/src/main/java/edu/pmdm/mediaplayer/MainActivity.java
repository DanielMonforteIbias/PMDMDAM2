package edu.pmdm.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = findViewById(R.id.txtResultado);
        mediaPlayer = MediaPlayer.create(this, R.raw.freemusic);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                t.setText("audio preparado");
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                t.setText("he liberado el fichero de audio!");
            }
        });



        

    }



    public void play(View view) {
        if (mediaPlayer.isPlaying()) {
            t.setText("ya está reproduciendo!");
        } else {
            mediaPlayer.start();
            t.setText("reproduciendo!");
        }
    }

    public void stop(View view) throws IOException {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
                t.setText("preparado!");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            t.setText("parado!");
        }
    }

    public void pause(View view) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            t.setText("pausado!");
        } else {
            t.setText("ya está en pausa!");
        }
    }
}