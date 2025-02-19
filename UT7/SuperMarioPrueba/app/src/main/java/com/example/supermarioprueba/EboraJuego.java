package com.example.supermarioprueba;

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
import androidx.core.view.MotionEventCompat;

import java.util.ArrayList;
import java.util.Iterator;

public class EboraJuego extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private SurfaceHolder holder; //Controla surfaceview para manejar el dibujo en pantalla
    private AppCompatActivity context;
    //MAPA
    public int maxX = 0, maxY = 0;
    private Bitmap mapa;
    private int mapHeight, mapWidth, destMapY;
    private float velMapa = 0f;
    private int posInicialMapa = 0, posMapaX = 0;
    private int frameCount = 0;
    private static final int textoInicialX = 50;
    private static final int textoInicialY = 20;
    int scrollX = 0; // Desplazamiento horizontal

    private int tileSize;
    private int totalTiles=9;
    private int[][]tileMap={
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,3,0,0,0,0,0,0,0,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1}
    };
    //MARIO
    private Bitmap mario;
    private int marioHeight, marioWidth;
    private int marioEstado;
    public float[] posicionMario = new float[2];
    private float[] velocidadMario = new float[2];
    private float[] inicialMario = new float[2];
    private float[] gravedad = new float[2];
    public final int X = 0, Y = 1;

    private float tiempoCrucePantalla = 10f;


    private BucleJuego bucleJuego;
    private float deltaTime; //Mide el tiempo transcurrido entre frames,

    public EboraJuego(AppCompatActivity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        this.context = context;
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        getHolder().addCallback(this);
        Canvas canvas = this.holder.lockCanvas();
        maxX = canvas.getWidth();
        maxY = canvas.getHeight();
        this.holder.unlockCanvasAndPost(canvas);
        //Pintar mapa
        mapa = BitmapFactory.decodeResource(getResources(), R.drawable.tileset);
        mapHeight = mapa.getHeight();
        mapWidth = mapa.getWidth();
        tileSize=mapWidth/totalTiles;
        destMapY = (maxY - mapHeight) / 2;

        //Pintar Mario
        mario = BitmapFactory.decodeResource(getResources(), R.drawable.mario);
        marioHeight = mario.getHeight();
        marioWidth = mario.getWidth();
        //Posicionar mario
        inicialMario[Y] = destMapY + mapHeight * 0.9f;
        inicialMario[X] = maxY * 0.1f;
        posicionMario[X] = inicialMario[X];
        posicionMario[Y] = inicialMario[Y];

        deltaTime = 1f / BucleJuego.MAX_FPS; //Tiempo de cada frame

        velocidadMario[X] = maxX / tiempoCrucePantalla;

        //Creamos el Gameloop
        bucleJuego = new BucleJuego(getHolder(), this);
        setFocusable(true);
        setOnTouchListener(this);
        bucleJuego.start();
    }

    public void render(Canvas canvas) {
        if (canvas != null) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawColor(Color.RED);

            canvas.translate(-scrollX, 0); // Desplazamos el canvas según el scroll
        }   dibujarMapa(canvas);


        int marioSprite = marioWidth / 21 * marioEstado;
        canvas.drawBitmap(mario, new Rect(marioSprite, 0, marioSprite + marioWidth / 21, marioHeight * 2 / 3),
                new Rect((int) posicionMario[X], (int) posicionMario[Y] - marioHeight * 2 / 3,
                        (int) posicionMario[X] + marioWidth / 21, (int) posicionMario[Y]), null);
    }

    public void actualizar() {
        frameCount++;
        scrollX+=velMapa;

    }

    private void dibujarMapa(Canvas canvas) {
        for (int row = 0; row < tileMap.length; row++) {
            for (int col = 0; col < tileMap[row].length; col++) {
                int tile = tileMap[row][col];

                // Calculamos la posición X y Y del tile en el lienzo
                int x = col * tileSize;
                int y = row * tileSize;

                // Calculamos la posición del tile en el tileset (asumiendo que tus tiles están organizados en una fila)
                int tileX = (tile % (mapWidth / tileSize)) * tileSize;

                Bitmap tileBitmap = Bitmap.createBitmap(mapa, tileX, 0, tileSize, tileSize);

                // Dibujamos el tile en el canvas
                canvas.drawBitmap(tileBitmap, x, y, null);
            }
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
        // Obtener la posición del toque
        float touchX = event.getX();

        // Detectar el tipo de evento (si es un toque o un deslizamiento)
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Mover al jugador dependiendo de la posición X del toque
                if (touchX > getWidth() / 2) {
                    // Si el toque está en la mitad derecha de la pantalla, mueve a la derecha
                    posicionMario[X] += velocidadMario[X];
                } else {
                    // Si el toque está en la mitad izquierda de la pantalla, mueve a la izquierda
                    posicionMario[X] -= velocidadMario[X];
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // Aquí puedes agregar lógica si deseas mover al jugador mientras el dedo se mueve
                break;
            case MotionEvent.ACTION_UP:
                // Acción cuando se deja de tocar la pantalla (opcional)
                break;
        }
        return true;  // Indica que el evento ha sido manejado
    }
}