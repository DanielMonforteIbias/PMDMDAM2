package com.example.supermarioprueba;

import static android.content.ContentValues.TAG;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class BucleJuego extends Thread{
    private EboraJuego juego;
    private SurfaceHolder surfaceHolder;
    private boolean juegoEnEjecucion=true;
    public final static int MAX_FPS=30;
    private final static int TIEMPO_FRAME=1000/MAX_FPS;
    private final static int MAX_FRAMES_SALTADOS=5;

    public BucleJuego(SurfaceHolder sh, EboraJuego s){
        juego=s;
        surfaceHolder=sh;
    }

    public void run(){
        Canvas canvas;
        Log.d(TAG,"Comienza el Gameloop");
        long tiempoComienzo, tiempoDiferencia;
        int tiempoDormir, frameASaltar;
        while(juegoEnEjecucion){
            canvas=null;
            try{
                canvas=this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    tiempoComienzo=System.currentTimeMillis();
                    frameASaltar=0;
                    juego.actualizar();
                    juego.render(canvas);
                    tiempoDiferencia=System.currentTimeMillis()-tiempoComienzo;
                    tiempoDormir=(int)(TIEMPO_FRAME-tiempoDiferencia);
                    if(tiempoDormir>0){
                        try {
                            Thread.sleep(tiempoDormir);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    while(tiempoDormir<0 && frameASaltar>MAX_FRAMES_SALTADOS){
                        juego.actualizar();
                        tiempoDormir+=TIEMPO_FRAME;
                        frameASaltar++;
                    }
                }
            }finally{
                if(canvas!=null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }Log.d(TAG,"Nueva iteracion");
    }

    public void fin(){
        juegoEnEjecucion=false;
    }
}