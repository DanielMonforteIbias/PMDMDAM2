package com.example.notificacionespersonalizado;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

public class ObservadorVolumen extends ContentObserver {
    private Context context;
    private AudioManager audioManager;
    public ObservadorVolumen(Handler handler, Context context) {
        super(handler);
        this.context=context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        int volumen=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Intent intent = new Intent("com.example.notificacionespersonalizado.VOLUMENCAMBIADO");
        intent.putExtra("Volumen", volumen);
        context.sendBroadcast(intent);
        System.out.println("aaa");
    }
}
