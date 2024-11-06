package com.example.prc2_danielmonforte;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private int filas=0; //Numero de filas de nuestra matriz
    private int columnas=0; //Numero de columnas de nuestra matriz
    private int numeroMinas=0; //Numero de minas a introducir en la matriz
    private int matriz[][]=new int[0][0]; //Matriz que contendra las casillas

    private int width=0; //Anchura de la pantalla
    private int height=0; //Altura de la pantalla

    private GridLayout gridLayout;
    private ConstraintLayout constraintLayout;
    private GridLayout.LayoutParams gridLayoutParams;
    LinearLayout.LayoutParams linearLayoutParams;

    private Button casilla;
    private ImageButton mina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        filas=8;
        columnas=8;
        numeroMinas=10;
        matriz=new int[filas][columnas];
        rellenarMinas();//Rellenamos el tablero con minas
        comprobarAlrededoresMinas(); //Comprobamos las casillas alrededor de cada mina
        crearGridLayout();
    }

    public void rellenarMinas(){
        for(int i=0;i<numeroMinas;i++) { //Hacemos un bucle en el que cada iteracion colocamos una mina
            boolean minaColocada=false; //Variable para saber si hemos colocado mina
            while(!minaColocada) { //Mientras no se haya colocado mina, lo intentamos
                int f=((int)(Math.random()*filas)); //Generamos un numero aleatorio para la fila
                int c=((int)(Math.random()*columnas)); //Generamos un numero aleatorio para la columna
                if(matriz[f][c]!=-1) { //Si lo que hay en la posicion aleatorio generada no es una mina
                    matriz[f][c]=-1; //Colocamos la mina
                    minaColocada=true; //Establecemos que hemos colocado la mina
                }
                //Si se ha colocado la mina se saldrá del while, si no se intentará en otra posición
            }
        }
    }
    public void comprobarAlrededoresMinas(){
        for(int i=0;i<filas;i++) { //Recorremos la matriz
            for(int j=0;j<columnas;j++) {
                if(matriz[i][j]==-1) { //Si lo que hay en la casilla es una mina
                    sumarMinaAlrededor(i,j); //Modificamos las casillas de alrededor
                }
            }

        }
    }

    /**
     * Este método se encarga de modificar las casillas alrededor de una mina para saber cuántas minas hay alrededor
     * @param y el valor de la fila en la matriz donde está la mina
     * @param x el valor de la columna en la matriz donde está la mina
     */
    public void sumarMinaAlrededor(int y,int x) {
        for(int i=-1;i<=1;i++) { //Hacemos un bucle que recorre las filas que rodean a la fila donde está la mina (encima y debajo suya)
            for(int j=-1;j<=1;j++) { //Hacemos un bucle que recorre las columnas que rodean a la columna donde está la mina (a su izquierda y derecha)
                if(!(i==0 && j==0)) { //Si la casilla en la que estamos no es la de la mina
                    int fila=i+y; //Calculamos la fila en la que estamos, sumando la fila recibida a i, que indica si estamos encima(-1), en ella (0) o debajo (1)
                    int columna=j+x; //Calculamos la columna en la que estamos, sumando la columna recibida a j, que indica si estamos a su izquierda (-1), en ella (0) o a su derecha (1)
                    if(fila>=0 && fila<filas && columna>=0 && columna<columnas) { //Si la casilla en la que estamos existe, es decir, no está fuera de la matriz
                        if(matriz[fila][columna]!=-1) { //Si no es una mina
                            matriz[fila][columna]+=1; //Incrementamos su valor en 1
                        }
                    }
                }
            }
        }
    }

    public void crearGridLayout(){
        gridLayout=new GridLayout(getApplicationContext());
        constraintLayout=findViewById(R.id.constraintLayout);
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height=constraintLayout.getHeight();
                width=constraintLayout.getWidth();
                System.out.println(height+" "+width);
                gridLayoutParams =new GridLayout.LayoutParams();
                gridLayoutParams.setMargins(0,0,0,0);
                gridLayoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
                gridLayoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
                gridLayout.setRowCount(filas);
                gridLayout.setColumnCount(columnas);
                gridLayout.setLayoutParams(gridLayoutParams);

                linearLayoutParams = new LinearLayout.LayoutParams(width/columnas,height/filas);
                linearLayoutParams.setMargins(0,0,0,0);
                rellenarGridLayout();
                constraintLayout.addView(gridLayout);
            }
        });

    }

    public void rellenarGridLayout(){
        for(int i=0;i<filas;i++){
            for(int j=0;j<columnas;j++){
                if(matriz[i][j]==-1){
                    mina=new ImageButton(this);
                    mina.setLayoutParams(linearLayoutParams);
                    mina.setImageResource(R.drawable.bomba);
                    mina.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("bomba descubierta");
                        }
                    });
                    mina.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            System.out.println("bomba marcada");
                            return true; //Devolvemos true para que no se llame a OnClick al soltar, pues si se devuelve true se consume la llamada
                        }
                    });
                    gridLayout.addView(mina);
                }
                else{
                    casilla=new Button(this);
                    casilla.setLayoutParams(linearLayoutParams);
                    casilla.setText(matriz[i][j]+"");
                    casilla.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("casilla descubierta");
                        }
                    });
                    casilla.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            System.out.println("casilla marcada erroneamente");
                            return true;
                        }
                    });
                    gridLayout.addView(casilla);
                }
            }
        }
    }
    public void imprimirMatriz(){
        for(int i=0;i<filas;i++) {
            for(int j=0;j<columnas;j++) {
                System.out.printf("%3s",matriz[i][j]);
            }
            System.out.println();
        }
    }
}