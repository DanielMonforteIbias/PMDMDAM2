package com.example.mediacontrollertarea;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MusicReceiver extends BroadcastReceiver {
    private MainActivity mainActivity;
    public MusicReceiver(){
        mainActivity=null;
    }
    public MusicReceiver(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(mainActivity+" a");
        if (context instanceof MainActivity) System.out.println("Si");

        if(mainActivity!=null){
            String action = intent.getAction();

            if ("PLAY_PAUSE".equals(action)) {
                if (mainActivity.isPlaying()) {
                    mainActivity.pause();
                } else {
                    mainActivity.start();
                }
            } else if ("NEXT".equals(action)) {
                mainActivity.siguienteCancion();
            } else if ("PREVIOUS".equals(action)) {
                mainActivity.anteriorCancion();
            } else if ("STOP".equals(action)) {
                mainActivity.stop();
            }
        }
    }
}