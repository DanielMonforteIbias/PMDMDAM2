package edu.pmdm.mivideojuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

public class Control {
    public boolean pulsado=false;
    public float coordX, coordY;
    private Bitmap imagen;
    private Context context;
    public String nombre;

    public Control(Context context, float coordX, float coordY) {
        this.context = context;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public void cargar(int recurso){
        this.imagen= BitmapFactory.decodeResource(context.getResources(),recurso);
    }

    public void dibujar(Canvas canvas, Paint paint){
        canvas.drawBitmap(imagen,coordX,coordY,paint);
    }

    public void comprobarPulsado(int x, int y){
        if(x>coordX && x<coordX+ancho() && y>coordY && y<coordY+alto()){
            pulsado=true;
            Log.d("CONTROL","Boton "+nombre+" pulsado");
        }
    }

    public int ancho(){
        return imagen.getWidth();
    }
    public int alto(){
        return imagen.getHeight();
    }

    public void comprobarSoltado(ArrayList<Toque> lista){
        boolean aux=false;
        for(Toque t:lista){
            if(t.x>coordX && t.x<coordX+ancho() && t.y>coordY && t.y<coordY+alto()){
                aux=true;
            }
        }
        if(!aux) pulsado=false;
    }
}
