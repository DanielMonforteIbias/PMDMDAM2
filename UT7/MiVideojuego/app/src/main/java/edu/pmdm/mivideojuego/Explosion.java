package edu.pmdm.mivideojuego;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

public class Explosion {
    private int anchoSprite;
    private int altoSprite;
    private final int NUMERO_IMAGENES_EN_SECUENCIA = 17;
    public float coordX, coordY; //coordenadas donde se dibuja el control
    private int estado;
    private EboraJuego juego;
    private MediaPlayer mediaPlayer;

    public Explosion(EboraJuego j, float x, float y) {
        coordX = x;
        coordY = y;
        juego = j;
        anchoSprite = juego.explosion.getWidth() / NUMERO_IMAGENES_EN_SECUENCIA;
        altoSprite = juego.explosion.getHeight();
        estado = -1; //recien creado
        mediaPlayer = MediaPlayer.create(j.getContext(), R.mipmap.explosion);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }

    public void actualizarEstado() {
        estado++;
    }

    public void dibujar(Canvas c, Paint p) {
        int posicionSprite = estado * anchoSprite;
        if (!HaTerminado()) {            //Calculamos el cuadrado del sprite que vamos a dibujar
            Rect origen = new Rect(posicionSprite, 0, posicionSprite + anchoSprite, altoSprite);            //calculamos donde vamos a dibujar la porcion del sprite
            Rect destino = new Rect((int) coordX, (int) coordY, (int) coordX + anchoSprite, (int) coordY + juego.explosion.getHeight());
            c.drawBitmap(juego.explosion, origen, destino, p);
        }
    }

    public boolean HaTerminado() {
        return estado >= NUMERO_IMAGENES_EN_SECUENCIA;
    }
}

