package edu.pmdm.mivideojuego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EboraJuego extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private SurfaceHolder holder; //Controla surfaceview para manejar el dibujo en pantalla
    private AppCompatActivity context;
    private int maxX=0, maxY=0;
    private Bitmap mapa;
    private int mapHeight, mapWidth, destMapY;
    private int velMapa=50;
    private int posInicialMapa=0, posMapaX=0;
    private int frameCount=0;
    private static final int textoInicialX=50;
    private static final int textoInicialY=20;

    private BucleJuego bucleJuego;
    private float deltaTime; //Mide el tiempo transcurrido entre frames,

    public EboraJuego(AppCompatActivity context) {
        super(context);
        holder=getHolder();
        holder.addCallback(this);
        this.context=context;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        getHolder().addCallback(this);
        Canvas canvas=this.holder.lockCanvas();
        maxX=canvas.getWidth();
        maxY=canvas.getHeight();
        this.holder.unlockCanvasAndPost(canvas);
        mapa= BitmapFactory.decodeResource(getResources(),R.drawable.mapa);
        mapHeight=mapa.getHeight();
        mapWidth=mapa.getWidth();
        destMapY=(maxY-mapHeight)/2;

        deltaTime=1f/BucleJuego.MAX_FPS; //Tiempo de cada frame

        //Creamos el Gameloop
        bucleJuego=new BucleJuego(getHolder(),this);
        setFocusable(true);
        bucleJuego.start();
    }

    public void render(Canvas canvas){
        if(canvas!=null){
            Paint paint=new Paint();
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawColor(Color.RED);
            canvas.drawBitmap(mapa,new Rect((int)posMapaX,0,(int)maxX+posMapaX,mapHeight),new Rect(0,destMapY,maxX,destMapY+mapHeight),null);

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(40);
            Rect textBounds=new Rect();
            paint.getTextBounds("Frames ejecutados",0,1,textBounds);
            canvas.drawText("Frames ejecutados: "+frameCount,textoInicialX,textoInicialY+textBounds.height(),paint);
        }
    }

    public void actualizar(){
        frameCount++;
        posMapaX+=velMapa;
        if(posMapaX>=mapWidth-maxX) {
            posMapaX=0;
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
