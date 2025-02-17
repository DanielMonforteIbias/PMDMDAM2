package com.example.prc6_danielmonforte;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

public class AudioPlayer implements MediaController.MediaPlayerControl {
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;

    public AudioPlayer(Context context){
        mediaController=new MediaController(context);
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(((Activity) context).findViewById(android.R.id.content));
        ((Activity)context).findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mediaController!=null && mediaPlayer!=null){
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        if (!mediaController.isShowing()) mediaController.show(0);
                        else mediaController.hide();

                }
                return false;
            }
        });
        ((Activity)context).findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mediaController!=null && mediaPlayer!=null){
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        if (!mediaController.isShowing()) mediaController.show(0);
                        else mediaController.hide();

                }
                return false;
            }
        });
    }
    public void playAudio(Context context, String audioFileName) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        int resId = context.getResources().getIdentifier(audioFileName, "raw", context.getPackageName());
        mediaPlayer = MediaPlayer.create(context, resId);
        if(mediaPlayer!=null) {
            mediaPlayer.start();
            mediaController.show(0);
        }
        else Toast.makeText(context,"No se puede reproducir el audio",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void start() { mediaPlayer.start(); }

    @Override
    public void pause() { mediaPlayer.pause(); }

    @Override
    public int getDuration() { return mediaPlayer.getDuration(); }

    @Override
    public int getCurrentPosition() { return mediaPlayer.getCurrentPosition(); }

    @Override
    public void seekTo(int pos) { mediaPlayer.seekTo(pos); }

    @Override
    public boolean isPlaying() { return mediaPlayer.isPlaying(); }

    @Override
    public int getBufferPercentage() { return 0; }

    @Override
    public boolean canPause() { return true; }

    @Override
    public boolean canSeekBackward() { return true; }

    @Override
    public boolean canSeekForward() { return true; }

    @Override
    public int getAudioSessionId() { return mediaPlayer.getAudioSessionId(); }
}
