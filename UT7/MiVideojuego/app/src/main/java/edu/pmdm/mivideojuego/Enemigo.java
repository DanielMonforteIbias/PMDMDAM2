package edu.pmdm.mivideojuego;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemigo {
    public final float VELOCIDAD_ENEMIGO;
    public float coordX,coordY;
    public int tipoEnemigo;
    public int nivel;
    private EboraJuego juego;
    public Enemigo(EboraJuego j, int n){
        juego=j;
        nivel=n;
        //5 segundos en cruzar
        VELOCIDAD_ENEMIGO=j.maxX/5f/BucleJuego.MAX_FPS;
        coordY=juego.posicionMario[juego.Y]-alto();
        coordX=juego.maxX;
    }
    public void actualizaCoordenadas(){
        coordX-=VELOCIDAD_ENEMIGO;
    }
    public void dibujar(Canvas c, Paint p){
        c.drawBitmap(juego.enemigo,coordX,coordY,p);
    }
    public int ancho(){
        return juego.enemigo.getWidth();
    }
    public int alto(){
        return juego.enemigo.getHeight();
    }
}
