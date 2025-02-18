package edu.pmdm.mivideojuego;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;

public class Disparo {
    public float coordX, coordY;
    private EboraJuego juego;
    private float velocidad;
    private MediaPlayer mediaPlayer;
    private final float MAX_SEGUNDOS_EN_PANTALLA=3;
    public Disparo(EboraJuego j,float x, float y){
        juego=j;
        coordX=x;
        coordY=y-j.disparo.getHeight()+15;
        velocidad=juego.maxX/MAX_SEGUNDOS_EN_PANTALLA/BucleJuego.MAX_FPS;

        mediaPlayer=MediaPlayer.create(j.getContext(),R.raw.disparo);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }
    public void actualizarCoordenadas(){
        coordX+=velocidad;
    }
    public void dibujar(Canvas canvas, Paint paint){
        canvas.drawBitmap(juego.disparo,coordX,coordY,paint);
    }
    public int ancho(){
        return juego.disparo.getWidth();
    }
    public int alto(){
        return juego.disparo.getHeight();
    }
    public boolean fueraDePantalla(){
        return coordY<0;
    }
}
