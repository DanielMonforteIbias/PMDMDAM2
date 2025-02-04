package edu.pmdm.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    MediaController mc;
    Handler h;
    VideoView v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mc= new MediaController(this);
        mc.setAnchorView(findViewById(R.id.constraintLayout));

        v=findViewById(R.id.videoView);
        v.setMediaController(mc);
        //v.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.magia));
        v.setVideoURI(Uri.parse("https://archive.org/download/archive-video-files/test.mp4"));


        h=new Handler();
        v.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
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

    }
}