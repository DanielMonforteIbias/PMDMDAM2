package edu.pmdm.mivideojuego;

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
    private float velMapa = 5f;
    private int posInicialMapa = 0, posMapaX = 0;
    private int frameCount = 0;
    private static final int textoInicialX = 50;
    private static final int textoInicialY = 20;

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
    private ArrayList<Toque> toques = new ArrayList<>();
    public boolean hayToque = false;

    public Control[] controles = new Control[4];
    private final int IZQUIERDA = 0;
    private final int DERECHA = 1;
    private final int DISPARO = 2;
    private final int SALTO = 3;
    private boolean grounded = true;

    //DISPARO
    public Bitmap disparo;
    private ArrayList<Disparo> disparos = new ArrayList<>();
    private boolean nuevoDisparo = false;
    private int framesParaNuevoDisparo = 0;
    private final int MAX_FRAMES_ENTRE_DISPARO = BucleJuego.MAX_FPS / 4;

    //ENEMIGO Y EXPLOSION
    public Bitmap enemigo, explosion;
    private ArrayList<Enemigo> enemigos = new ArrayList<Enemigo>();
    public final int TOTAL_ENEMIGOS = 500; //Enemigos para acabar el juegoprivate
    int enemigos_minuto = 20; //número de enemigos por minutoprivate
    int frames_para_nuevo_enemigo = 0; //frames que restan hasta generar nuevo enemigoprivate
    int enemigos_muertos = 0; //Contador de enemigos muertosprivate
    int enemigos_creados = 0;
    private int Nivel = 0;
    private ArrayList<Explosion> explosiones = new ArrayList<Explosion>();


    private BucleJuego bucleJuego;
    private float deltaTime; //Mide el tiempo transcurrido entre frames,

    public EboraJuego(AppCompatActivity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        this.context = context;
    }

    public void cargarEnemigos() {
        frames_para_nuevo_enemigo = bucleJuego.MAX_FPS * 60 / enemigos_minuto;
        enemigo = BitmapFactory.decodeResource(getResources(), R.mipmap.enemigo);
        float factorEscala = 0.4f; // Reduce al 40% del tamaño original
        int nuevoAncho = (int) (enemigo.getWidth() * factorEscala);
        int nuevoAlto = (int) (enemigo.getHeight() * factorEscala);
        enemigo = Bitmap.createScaledBitmap(enemigo, nuevoAncho, nuevoAlto, false);
    }


    public void cargarControles() {
        float aux;
        //FLECHA IZQUIERDA
        controles[IZQUIERDA] = new Control(getContext(), 0, maxY / 5 * 4);
        controles[IZQUIERDA].cargar(R.drawable.left);
        controles[IZQUIERDA].nombre = "IZQUIERDA";
        //FLECHA DERECHA
        controles[DERECHA] = new Control(getContext(), controles[0].ancho() + controles[0].coordX + 5, controles[0].coordY);
        controles[DERECHA].cargar(R.drawable.right);
        controles[DERECHA].nombre = "DERECHA";
        //DISPARO
        aux = 5.0f / 7.0f * maxX; //En los 5/7 de ancho
        controles[DISPARO] = new Control(getContext(), aux, controles[0].coordY);
        controles[DISPARO].cargar(R.drawable.shoot);
        controles[DISPARO].nombre = "DISPARO";
        //SALTO
        aux = 4.0f / 7.0f * maxX; //En los 4/7 de ancho
        controles[SALTO] = new Control(getContext(), aux, controles[0].coordY);
        controles[SALTO].cargar(R.drawable.up);
        controles[SALTO].nombre = "SALTO";
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        getHolder().addCallback(this);
        Canvas canvas = this.holder.lockCanvas();
        maxX = canvas.getWidth();
        maxY = canvas.getHeight();
        this.holder.unlockCanvasAndPost(canvas);
        //Pintar mapa
        mapa = BitmapFactory.decodeResource(getResources(), R.drawable.mapa);
        mapHeight = mapa.getHeight();
        mapWidth = mapa.getWidth();
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

        cargarControles();
        velocidadMario[X] = maxX / tiempoCrucePantalla;

        //Cargar disparo
        disparo = BitmapFactory.decodeResource(getResources(), R.drawable.shot);
        // Cargamos la imagen de la explosión
        explosion = BitmapFactory.decodeResource(getResources(), R.mipmap.explosion);
        //Cargar enemigo
        cargarEnemigos();

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
            canvas.drawBitmap(mapa,
                    new Rect((int) posMapaX, 0, (int) maxX + posMapaX, mapHeight),
                    new Rect(0, destMapY, maxX, destMapY + mapHeight),
                    null);

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(40);
            Rect textBounds = new Rect();
            paint.getTextBounds("Frames ejecutados", 0, 1, textBounds);
            canvas.drawText("Frames ejecutados: " + frameCount, textoInicialX, textoInicialY + textBounds.height(), paint);

            //Posicionamos a Mario en pantalla
            int marioSprite = marioWidth / 21 * marioEstado;
            canvas.drawBitmap(mario, new Rect(marioSprite, 0, marioSprite + marioWidth / 21, marioHeight * 2 / 3),
                    new Rect((int) posicionMario[X], (int) posicionMario[Y] - marioHeight * 2 / 3,
                            (int) posicionMario[X] + marioWidth / 21, (int) posicionMario[Y]), null);
            //Dibujar controles
            paint.setAlpha(200);
            for (int i = 0; i < controles.length; i++) {
                controles[i].dibujar(canvas, paint);
            }

            //Dibujar disparos
            for (Disparo d : disparos) {
                d.dibujar(canvas, paint);
            }

            //Dibujar enemigos
            for (Enemigo e : enemigos) {
                e.dibujar(canvas, paint);
            }

            //actualizar explosiones
            for(Iterator<Explosion> it_explosiones=explosiones.iterator();it_explosiones.hasNext();){
                Explosion exp=it_explosiones.next();
                exp.actualizarEstado();
                if(exp.HaTerminado()) it_explosiones.remove();
            }
            //dibuja las explosiones
            for(Explosion exp:explosiones) {
                exp.dibujar(canvas,paint);
            }
        }
    }

    public void actualizar() {
        frameCount++;

        //MOVIMIENTO MAPA
        posMapaX += velMapa;
        if (posMapaX >= mapWidth - maxX) {
            posMapaX = 0;
        }

        //CONTROLES
        if (controles[SALTO].pulsado) {
            if (grounded) { //Solo se salta si está en el suelo
                velocidadMario[Y] = -(maxX / tiempoCrucePantalla) * 2;
                gravedad[X] = 0;
                gravedad[Y] = -velocidadMario[Y] * 2;
                grounded = false;
            }
        }
        if (posMapaX >= mapWidth) {
            posMapaX -= mapWidth;
        }
        //Mario corre derecha
        if (controles[DERECHA].pulsado) {
            //Cada 3 frames cambia animacion
            if (frameCount % 3 == 0) {
                marioEstado++;
                if (marioEstado == 4) marioEstado = 1;
            }
            if (posicionMario[X] < 0.7f * maxX) {
                posicionMario[X] += deltaTime * velocidadMario[X];
                velocidadMario[X] = velocidadMario[X] + deltaTime * gravedad[X];
            } else {
                posMapaX += deltaTime * velocidadMario[X];
            }
        }
        if (controles[IZQUIERDA].pulsado) {
            if (frameCount % 3 == 0) {
                marioEstado++;
                if (marioEstado == 4) marioEstado = 1;
            }
            if (posicionMario[X] > 0) {
                posicionMario[X] -= deltaTime * velocidadMario[X];
            }
        }
        if (!grounded) {//actualiza la posicion y de mario segun la velocidad y actualiza la gravedad para ajustar el arco
            posicionMario[Y] += deltaTime * velocidadMario[Y];
            velocidadMario[Y] += deltaTime * gravedad[Y];
        }
        //Rebote vertical
        if (posicionMario[Y] > inicialMario[Y]) {
            grounded = true;
            velocidadMario[Y] = 0;
            posicionMario[Y] = inicialMario[Y];
        }

        //DISPARO
        if (controles[DISPARO].pulsado) nuevoDisparo = true;
        if (framesParaNuevoDisparo == 0) {
            if (nuevoDisparo) {
                crearDisparo();
                nuevoDisparo = false;
            }
            framesParaNuevoDisparo = MAX_FRAMES_ENTRE_DISPARO;
        }
        framesParaNuevoDisparo--;
        //Mover disparo
        for (Iterator<Disparo> itDisparos = disparos.iterator(); itDisparos.hasNext(); ) {
            Disparo d = itDisparos.next();
            d.actualizarCoordenadas();
            if (d.fueraDePantalla()) itDisparos.remove();
        }

        //Enemigos
        if (frames_para_nuevo_enemigo == 0) {
            crearEnemigo();
            frames_para_nuevo_enemigo = bucleJuego.MAX_FPS * 60 / enemigos_minuto;
        }
        frames_para_nuevo_enemigo--;
        //Los enemigos persiguen al jugador
        for (Enemigo e : enemigos) {
            e.actualizaCoordenadas();
        }
        for (Iterator<Enemigo> it_enemigos = enemigos.iterator(); it_enemigos.hasNext(); ) {
            Enemigo e = it_enemigos.next();
            for (Iterator<Disparo> it_disparos = disparos.iterator(); it_disparos.hasNext(); ) {
                Disparo d = it_disparos.next();
                if (colision(e, d)) {            /* Creamos un nuevo objeto explosión */
                    explosiones.add(new Explosion(this, e.coordX, e.coordY - e.alto() / 2 - this.explosion.getHeight() / 4));            /* eliminamos de las listas tanto el disparo como el enemigo */
                    try {
                        it_enemigos.remove();
                        it_disparos.remove();
                    } catch (Exception ex) {
                    }
                    enemigos_muertos++; //un enemigo menos para el final
                }
            }
        }
    }

    public void crearDisparo() {
        disparos.add(new Disparo(this, posicionMario[X], posicionMario[Y] - marioHeight / 2));
    }

    public void crearEnemigo() {
        if (TOTAL_ENEMIGOS - enemigos_creados > 0) {
            enemigos.add(new Enemigo(this, Nivel));
            enemigos_creados++;
        }
    }

    public boolean colision(Enemigo e, Disparo d) {
        int alto_mayor = e.alto() > d.alto() ? e.alto() : d.alto();
        int ancho_mayor = e.ancho() > d.ancho() ? e.ancho() : d.ancho();
        float diferenciaX = Math.abs(e.coordX - d.coordX);
        float diferenciaY = Math.abs(e.coordY - d.coordY);
        return diferenciaX < ancho_mayor && diferenciaY < alto_mayor;
    }


    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int index, x, y;
        index = MotionEventCompat.getActionIndex(event); //Obtener el pointer de la accion

        //Coordenadas del toque
        x = (int) MotionEventCompat.getX(event, index);
        y = (int) MotionEventCompat.getY(event, index);
        //Identificar evento
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: //Primer dedo toca la pantalla
            case MotionEvent.ACTION_POINTER_DOWN: //Otro dedo toca la pantalla
                hayToque = true;
                synchronized (this) {
                    toques.add(index, new Toque(index, x, y));
                }
                for (int i = 0; i < controles.length; i++) {
                    controles[i].comprobarPulsado(x, y);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP: // un dedo levanta el toque pero hay otros tocando
                synchronized (this) {
                    toques.remove(index);
                }
                for (int i = 0; i < controles.length; i++) {
                    controles[i].comprobarSoltado(toques);
                }
                break;
            case MotionEvent.ACTION_UP: //Ultimo dedo levanta el toque
                synchronized (this) {
                    toques.clear();
                }
                hayToque = false;
                for (int i = 0; i < controles.length; i++) {
                    controles[i].comprobarSoltado(toques);
                }
                break;
        }
        return true;
    }
}
