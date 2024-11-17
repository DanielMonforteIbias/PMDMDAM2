package com.example.prc2_danielmonforte;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    //VARIABLES DEL TABLERO
    private int filas=0; //Numero de filas de nuestra matriz
    private int columnas=0; //Numero de columnas de nuestra matriz
    private int numeroMinas=0; //Numero de minas a introducir en la matriz
    private int matriz[][]=new int[0][0]; //Matriz que contendra las casillas
    private String estadoCasillas[][]=new String[0][0]; //Matriz que contendrá el estado de las casillas
    private int minasRestantes=0; //Minas que faltan por marcar

    //TAMAÑO DEL TABLERO
    private int width=0; //Anchura de la pantalla
    private int height=0; //Altura de la pantalla

    //DIFICULTAD
    private int dificultad=0; //Guardará la dificultad actual. 0 es principiante, 1 es amateur y 2 es avanzado
    private int valoresFilas[]={8,12,16}; //Array que contiene las cantidades de filas que puede haber según la dificultad
    private int valoresColumnas[]={8,12,16}; //Array que contiene las cantidades de columnas que puede haber según la dificultad
    private int valoresMinas[]={10,30,60}; //Array que contiene las cantidades de minas que puede haber según la dificultad

    //VISTAS DE LAYOUTS Y RELACIONADOS
    private GridLayout gridLayout;
    private ConstraintLayout constraintLayout;
    private GridLayout.LayoutParams gridLayoutParams;
    private LinearLayout.LayoutParams linearLayoutParams;
    //VISTA DE LA TOOLBAR
    private Toolbar toolbar;
    //VARIABLE PARA MOSTRAR DIALOGOS EN PANTALLA
    private AlertDialog.Builder dialogo;
    private boolean dialogoAbierto=false; //Variable para controlar si hay algun dialogo abierto, para que no se abran varios al pulsar muy rápido
    //VISTAS DE BUTTON E IMAGEBUTTON PARA AÑADIR AL TABLERO
    private Button casilla;
    private ImageButton mina;
    //RECURSOS DE BORDES CON FONDO DE COLOR PARA APLICAR A LAS VISTAS
    private Drawable bordeFondoBlanco;
    private Drawable bordeFondoRojo;
    //LISTENERS PARA LOS BOTONES
    private View.OnClickListener onClickListener;
    private View.OnLongClickListener onLongClickListener;
    //VARIABLES PARA GUARDAR LOS ID DE LOS ICONOS DE MINA SELECCIONADOS
    private int iconoMina=R.drawable.bomba; //Inicializada con la bomba por defecto
    private int iconoMinaMuerta=R.drawable.bombamuerta; //Inicializada con la bomba por defecto
    //MEDIAPLAYER PARA REPRODUCIR SONIDOS
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar=findViewById(R.id.toolbar); //Obtenemos la toolbar por su id
        setSupportActionBar(toolbar); //Hacemos que nuestra toolbar actue como ActionBar
        bordeFondoBlanco =getDrawable(R.drawable.bordefondoblanco); //Inicializamos el Drawable borde con fondo blanco con el recurso deseado
        bordeFondoRojo=getDrawable(R.drawable.bordefondorojo); //Inicializamos el Drawable borde con fondo rojo con el recurso deseado
        crearListeners(); //Inicializamos los listeners de onClick y OnLongClick
        iniciarJuego(); //Iniciamos el juego
    }

    /**
     * Método que se ejecuta al cambiar la configuración, por ejemplo, rotar la pantalla
     * @param newConfig The new device configuration.
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        crearGridLayout(); //Volvemos a crear el GridLayout para que tome las nuevas medidas. Es importante incluir medidas en este método para que las casillas se mantengan como antes según lo que hay en las variables internas
        //Para que esto funcione es importante tener en el manifest la línea "android:configChanges="orientation|screenSize"
        //Al principio se usaba onSavedStateInstance y se recuperaban datos, pero había un error que no se podía arreglar
        //Al rotar la pantalla con un diálogo abierto ocurría una excepción, pues el diálogo no se puede mantener en pantalla
        //De esta forma no se recrea la actividad de nuevo y los diálogos se mantienen en pantalla
    }

    /**
     * Método que crea las opciones de menú de la Toolbar
     * @param menu El menú en el que se colocarán las opciones
     *
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Inicializa el contenido del menú
        getMenuInflater().inflate(R.menu.toolbar_menu,menu); //Inflamos las opciones del archivo toolbar_menu en el menú para que aparezcan al pulsarlo
        MenuItem menuItemIconoMina = menu.findItem(R.id.menuItemIconoMina); //Obtenemos el menu Item con el icono de la mina
        menuItemIconoMina.setIcon(iconoMina); //Le ponemos el icono de la mina actual
        return true; //Devolvemos true para que el menú se muestre. Si devolviésemos false no lo haría
    }

    /**
     * Método que se ejecuta al seleccionar una opción del menú
     * @param item El ítem del menú que se eligió
     *
     * @return true para consumir la llamada
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId(); //Obtenemos el id del item pulsado
        if(id==R.id.menuItemInstrucciones){ //Si el item pulsado es el de instrucciones
            if(!dialogoAbierto)mostrarInstrucciones(); //Mostramos las opciones si no hay ningún diálogo abierto
        }
        else if (id==R.id.menuItemReiniciar){ //Si el item pulsado es el de reiniciar
            reiniciarJuego(); //Reiniciamos el juego
        }
        else if (id==R.id.menuItemConfigurar){ //Si el item pulsado es el de configurar
            if(!dialogoAbierto)mostrarOpcionesConfiguracion(); //Mostramos las opciones de configuración si no hay ningún diálogo abierto
        }
        else if (id==R.id.menuItemIconoMina){ //Si el item pulsado es el de cambiar icono de mina
            if(!dialogoAbierto)cambiarIconoMinas(); //Mostramos el diálogo para cambiar el icono de la mina si no hay ningún diálogo abierto
        }
        return true; //Devolvemos true para consumir la llamada. Si devolviésemos false se podrían comprobar otras funciones de click
    }

    /**
     * Método llamado cada vez que se crea una partida, haciendo funciones como establecer el número de filas, columnas y minas,
     * o crear el tablero.
     */
    public void iniciarJuego(){
        filas=valoresFilas[dificultad]; //Según la dificultad cogemos un valor de filas
        columnas=valoresColumnas[dificultad]; //Según la dificultad cogemos un valor de columnas
        numeroMinas=valoresMinas[dificultad]; //Según la dificultad cogemos un valor de minas
        matriz=new int[filas][columnas]; //Inicializamos la matriz interna con el número de filas y columnas correspondientes
        estadoCasillas=new String[filas][columnas]; //Inicializamos la matriz de estado de las casillas con el número de filas y columnas correspondientes
        inicializarEstadoCasillas(); //Inicializamos las posiciones de la matriz de estado a cadenas vacías
        minasRestantes=numeroMinas; //Guardamos las minas restantes como el total de minas que hay
        rellenarMinas();//Rellenamos el tablero interno con minas
        comprobarAlrededoresMinas(); //Comprobamos las casillas alrededor de cada mina
        crearGridLayout(); //Creamos el tablero visualmente
    }

    /**
     * Método que inicializa el valor de las posiciones de la matriz estadoCasillas a cadenas vacías, pues por defecto se inicializan en null
     * y eso daría errores posteriores al intentar comparar cadenas
     */
    public void inicializarEstadoCasillas(){
        for(int i=0;i<filas;i++){ //Recorremos la matriz
            for(int j=0;j<columnas;j++){
                estadoCasillas[i][j]=""; //Cada posición la llenamos con una cadena vacía
            }
        }
    }
    /**
     * Método que rellena la matriz interna con las minas, poniendo un -1 en las casillas donde hay una mina
     */
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

    /**
     * Método que comprueba los alrededores de las casillas con minas para actualizarlas con el número de minas que las rodean
     */
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
     * Método que se encarga de modificar las casillas alrededor de una mina para saber cuántas minas hay alrededor
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

    /**
     * Método que inicializa los listeners que usarán las vistas de Button e ImageButton, para no crear uno nuevo por cada vista y usar siempre estos
     */
    public void crearListeners(){
        onClickListener=new View.OnClickListener() { //Inicializamos el onClickListener
            @Override
            public void onClick(View v) {
                if(v instanceof Button){ //Si la vista que ha activado el método es un botón (en nuestro caso una casilla sin mina)
                    if(v.getTag().equals(("Descubierta"))){ //Si ya ha sido descubierta
                        Toast.makeText(getApplicationContext(),"Casilla ya descubierta",Toast.LENGTH_SHORT).show(); //Se muestra un aviso al usuario
                    }
                    else{ //Si no se ha descubierto
                        descubrirCasilla((Button)v); //La descubrimos
                    }
                }
                else if (v instanceof ImageButton){ //Si la vista que ha activado el método es una ImageButton (es decir, una mina)
                    ((ImageButton)v).setImageResource(iconoMinaMuerta); //Ponemos como imagen el recurso "bombamuerta"
                    guardarMinaDetonada((ImageButton)v); //Guardamos la mina como detonada para conservarla al rotar pantalla
                    if(!dialogoAbierto) mostrarDialogoFinPartida("Has perdido. Has descubierto una mina",false); //Mostramos un diálogo con mensaje de derrota y motivo, si no hay ningún diálogo abierto (para no poder abrir varios al pulsar rápido)
                }
            }
        };
        onLongClickListener=new View.OnLongClickListener() { //Inicializamos el onLongClickListener
            @Override
            public boolean onLongClick(View v) {
                if(v instanceof Button){ //Si la vista que ha activado el método es un botón
                    if(v.getTag().equals(("Descubierta"))){ //Si la casilla ya ha sido descubierta
                        Toast.makeText(getApplicationContext(),"Casilla ya descubierta",Toast.LENGTH_SHORT).show(); //Mostramos un mensaje al usuario
                        //No se pierde aunque se intente marcar como mina una casilla ya descubierta, ya que según los requisitos del enunciado "marcar una casilla ya descubierta" es una acción inválida para la que se debe implementar un mensaje de error
                    }
                    else{ //Si no ha sido descubierta
                        ((Button)v).setText((int)v.getTag()+""); //Mostramos su tag, que es el número de minas alrededor
                        guardarMinaMarcadaIncorrectamente((Button)v); //La guardamos como descubierta para conservarla al rotar pantalla
                        mostrarDialogoFinPartida("Has perdido. Has marcado una casilla sin mina",false); //Mostramos un diálogo con mensaje de derrota y el motivo
                    }
                }
                else if (v instanceof ImageButton){ //Si la vista que ha activado el método es un ImageButton
                    marcarODesmarcarMina((ImageButton) v); //La marcamos o desmarcamos según toque
                }
                return true; //Devolvemos true para que no se llame a OnClick al soltar, pues si se devuelve true se consume la llamada
            }
        };
    }

    /**
     * Método que descubre una casilla y la guarda como "Descubierta" en la matriz de estado de casillas.
     * Además, si la casilla descubierta es 0, encuentra la casilla en el gridLayout y descubre las de alrededor
     * @param v El botón de la casilla que se ha pulsado
     */
    public void descubrirCasilla(Button v){
        if(!v.getTag().equals(("Descubierta"))){ //Si la casilla no ha sido descubierta (comprobación necesaria por si se intenta descubrir al pulsar en otra casilla con 0 alrededor pero esta ya está descubierta)
            v.setText((int)v.getTag()+""); //Mostramos su tag como texto del botón, que es el número de minas que hay alrededor
            v.setTag("Descubierta"); //Ahora su tag almacena que ya ha sido descubierta
            for (int i=0;i< matriz.length;i++){ //Recorremos la matriz para encontrar la casilla específica
                for (int j=0;j< matriz[0].length;j++){
                    int index=i*columnas+j; //Calculamos el índice que tendrá en el gridLayout, que es un número que se incrementa
                    if(gridLayout.getChildAt(index)==v) { //Si el elemento en ese index del gridLayout es la casilla que hemos descubierto
                        estadoCasillas[i][j]="Descubierta"; //Guardamos que está descubierta en la matriz de estado de casillas
                        if(v.getText().equals("0")) { //Si la casilla no tiene minas alrededor
                            v.setBackground(bordeFondoRojo); //Ponemos la casilla con el fondo rojizo para distinguirla del resto
                            descubrirCasillasAlrededor(i,j); //Descubrimos las casillas alrededor suya
                        }
                    }
                }
            }
        }
    }

    /**
     * Método que descubre las casillas alrededor de una posición recibida
     * @param y la fila de la casilla
     * @param x la columna de la casilla
     */
    public void descubrirCasillasAlrededor(int y, int x){
        for(int i=-1;i<=1;i++) { //Hacemos un bucle que recorre las filas que rodean a la fila donde está la casilla (encima y debajo suya)
            for(int j=-1;j<=1;j++) { //Hacemos un bucle que recorre las columnas que rodean a la columna donde está la casilla (a su izquierda y derecha)
                if(!(i==0 && j==0)) { //Si la casilla en la que estamos no es la de la posición recibida
                    int fila=i+y; //Calculamos la fila en la que estamos, sumando la fila recibida a i, que indica si estamos encima(-1), en ella (0) o debajo (1)
                    int columna=j+x; //Calculamos la columna en la que estamos, sumando la columna recibida a j, que indica si estamos a su izquierda (-1), en ella (0) o a su derecha (1)
                    if(fila>=0 && fila<filas && columna>=0 && columna<columnas) { //Si la casilla en la que estamos existe, es decir, no está fuera de la matriz
                        int index=fila*columnas+columna; //Calculamos el index de la casilla en el gridLayout
                        descubrirCasilla((Button)gridLayout.getChildAt(index)); //Descubrimos la casilla que tenga el index calculado
                    }
                }
            }
        }
    }

    /**
     * Método que marca o desmarca una mina, según si antes estaba marcada o no, y guarda su nuevo estado en la matriz de estados de casillas
     * @param v El ImageButton de la mina con la que se ha interactuado
     */
    public void marcarODesmarcarMina(ImageButton v){
        for (int i=0;i< matriz.length;i++){ //Recorremos la matriz para encontrar la mina específica
            for (int j=0;j< matriz[0].length;j++){
                int index=i*columnas+j; //Calculamos el índice que tendrá en el gridLayout, que es un número que se incrementa
                if(gridLayout.getChildAt(index)==v) { //Si el elemento en ese index del gridLayout es la mina que hemos tocado
                    if(v.getTag().equals(("Marcada"))){ //Si la mina estaba marcada
                        v.setTag("No marcada"); //Ponemos su tag a "No marcada"
                        v.setImageDrawable(null); //Quitamos la imagen de mina que indica que está marcada
                        estadoCasillas[i][j]="No marcada"; //La guardamos como No marcada en la matriz de estado de casillas
                        minasRestantes++; //Como se ha desmarcado una mina que estaba marcada correctamente, ahora queda una mina más por marcar
                        //Se permite desmarcar minas que estaban marcadas correctamente porque, según los requisitos, el onLongClick "Marca o desmarca la casilla como sospechosa de contener una mina"
                    }
                    else if (v.getTag().equals("No marcada")){ //Si la mina no estaba marcada
                        v.setTag("Marcada"); //Ponemos su tag como "Marcada"
                        v.setImageResource(iconoMina); //Ponemos la imagen de la mina para saber que se ha marcado
                        estadoCasillas[i][j]="Marcada"; //La guardamos como Marcada en la matriz de estado de casillas
                        minasRestantes--; //Como se ha marcado bien, ahora queda una mina menos por marcar
                        comprobarVictoria(); //Comprobamos si se han marcado todas las minas
                    }
                }
            }
        }
    }

    /**
     * Método que guarda una casilla como descubierta cuando se marca incorrectamente, para guardar su estado y que al rotar la pantalla permanezca visible
     * @param v El botón de la casilla marcada
     */
    public void guardarMinaMarcadaIncorrectamente(Button v){
        for (int i=0;i< matriz.length;i++){ //Recorremos la matriz para encontrar la casilla específica
            for (int j=0;j< matriz[0].length;j++){
                int index=i*columnas+j; //Calculamos el índice que tendrá en el gridLayout, que es un número que se incrementa
                if(gridLayout.getChildAt(index)==v) { //Si el elemento en ese index del gridLayout es la casilla que hemos descubierto
                    estadoCasillas[i][j]="Descubierta"; //Guardamos que está descubierta en la matriz de estado de casillas
                }
            }
        }
    }

    /**
     * Método que guarda una mina como detonada cuando se pulsa, para guardar su estado y conservarla al rotar
     * @param v La vista de la mina detonada
     */
    public void guardarMinaDetonada(ImageButton v){
        for (int i=0;i< matriz.length;i++){ //Recorremos la matriz para encontrar la casilla específica
            for (int j=0;j< matriz[0].length;j++){
                int index=i*columnas+j; //Calculamos el índice que tendrá en el gridLayout, que es un número que se incrementa
                if(gridLayout.getChildAt(index)==v) { //Si el elemento en ese index del gridLayout es la casilla que hemos descubierto
                    estadoCasillas[i][j]="Detonada"; //Guardamos que está detonada en la matriz de estado de casillas
                }
            }
        }
    }

    public void crearGridLayout(){
        gridLayout=new GridLayout(getApplicationContext()); //Inicializamos el layout que contendra las minas
        constraintLayout=findViewById(R.id.constraintLayout); //Inicializamos el layout que contendra el tablero
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this); //Eliminamos el listener para que no se vuelva a llamar

                height=constraintLayout.getHeight(); //Obtenemos la altura que va a tener el tablero
                width=constraintLayout.getWidth(); //Obtenemos la anchura que va a tener el tablero
                //Creamos los parámetros del GridLayout
                gridLayoutParams =new GridLayout.LayoutParams();
                gridLayoutParams.setMargins(0,0,0,0); //No tendrá márgenes
                gridLayoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT; //Su anchura será la del padre
                gridLayoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT; //Su altura será la del padre
                gridLayout.setRowCount(filas); //El número de filas será el guardado en la variable "filas"
                gridLayout.setColumnCount(columnas); //El número de columnas será el guardado en la variable "columnas"
                gridLayout.setLayoutParams(gridLayoutParams); //Asignamos los parámetros al gridLayout
                //Creamos los parámetros para los botones
                linearLayoutParams = new LinearLayout.LayoutParams(width/columnas,height/filas); //La anchura del botón será la anchura de la pantalla entre el número de columnas y la altura del botón será la altura de la pantalla entre el número de filas
                linearLayoutParams.setMargins(0,0,0,0); //No tendrán márgenes
                rellenarGridLayout(); //Rellenamos el gridLayout con las vistas de botones e ImageButton
                constraintLayout.addView(gridLayout); //Añadimos el gridLayout a la pantalla
            }
        });
    }

    /**
     * Método que rellena el gridLayout con las vistas correspondientes, siendo un ImageButton si en esa casilla hay una mina o un Button si no
     */
    public void rellenarGridLayout(){
        for(int i=0;i<filas;i++){ //Recorremos las filas del tablero
            for(int j=0;j<columnas;j++){ //Recorremos las columnas del tablero
                if(matriz[i][j]==-1){ //Si en esa posicion hay una mina
                    mina=new ImageButton(this); //Meteremos un ImageButton
                    mina.setLayoutParams(linearLayoutParams); //Le asignamos los parámetros de los botones para que tenga medidas correctas
                    mina.setBackground(bordeFondoBlanco); //Añadimos el borde y el fondo blanco
                    if(estadoCasillas[i][j].equals("Marcada")){ //Si la mina está marcada en la matriz de estado (necesario por si se ha creado el gridlayout de nuevo al rotar la pantalla)
                        mina.setImageResource(iconoMina); //Ponemos el icono de la mina para mostrar que está marcada
                        mina.setTag("Marcada"); //En un principio, guardamos en el tag que está marcada
                    }
                    else if(estadoCasillas[i][j].equals("Detonada")){ //Si su estado es detonada (por si se está creando el tablero tras rotar la pantalla con el diálogo de derrota abierto)
                        mina.setImageResource(iconoMinaMuerta); //Ponemos el icono de la mina muerta
                    }
                    else{ //Si no está marcada
                        mina.setTag("No marcada"); //En un principio, guardamos en el tag que no está marcada
                    }

                    mina.setOnClickListener(onClickListener); //Le asignamos el onClickListener
                    mina.setOnLongClickListener(onLongClickListener); //Le asignamos el onLongClickListener
                    gridLayout.addView(mina); //Añadimos la vista al gridLayout
                }
                else{ //Si en esa posición no hay una mina
                    casilla=new Button(this); //Crearemos un botón
                    casilla.setLayoutParams(linearLayoutParams); //Le asignamos los parámetros de los botones para que tenga medidas correctas
                    casilla.setBackground(bordeFondoBlanco); //Añadimos el borde y el fondo blanco
                    casilla.setPadding(0,0,0,0); //Quitamos el padding del botón. Necesario porque cuando hay muchos botones muy pequeños el padding hace que el texto no se vea
                    casilla.setTag(matriz[i][j]); //Guardamos en su tag el número de minas que hay alrededor, que está guardado en la matriz interna
                    if(estadoCasillas[i][j].equals("Descubierta")){ //Si la casilla está descubierta en la matriz de estado (necesario por si se ha creaod de nuevo el gridlayout al rotar la pantalla)
                        casilla.setText(matriz[i][j]+""); //Ponemos su valor como texto para mostrar el número de minas alrededor (concatenado con uan cadena vacía para que sea String y no int)
                        if(casilla.getText().equals("0")) casilla.setBackground(bordeFondoRojo); //Además, si no tiene minas alrededor el fondo será rojo claro
                        casilla.setTag("Descubierta"); //Marcamos que está descubierta en su tag
                    }
                    casilla.setOnClickListener(onClickListener); //Le asignamos el onClickListener
                    casilla.setOnLongClickListener(onLongClickListener); //Le asignamos el onLongClickListener
                    gridLayout.addView(casilla); //Añadimos la vista al gridLayout
                }
            }
        }
    }

    /**
     * Método usado durante el desarrollo que imprime por consola la matriz, para comprobar que las posiciones del GridLayout concuerdan con las de la matriz interna
     */
    public void imprimirMatriz(){
        for(int i=0;i<filas;i++) {
            for(int j=0;j<columnas;j++) {
                System.out.printf("%3s",matriz[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Método que reproduce un sonido recibido
     * @param soundId el id dle sonido a reproducir
     */
    public void playSound(int soundId){
        mp= MediaPlayer.create(getApplicationContext(),soundId);
        mp.start(); //Reproducimos el sonido cuyo id coincida con el recibido
    }

    /**
     * Método que muestra un mensaje al terminar la partida
     * @param mensaje El mensaje a mostrar en el diálogo
     * @param victoria Variable que nos dice si se ha terminado la partida porque el usuario ha ganado o porque ha perdido
     */
    private void mostrarDialogoFinPartida(String mensaje, boolean victoria) {
        dialogo = new AlertDialog.Builder(this); //Inicializamos el diálogo
        if(victoria) { //Si el usuario ha ganado
            playSound(R.raw.victoria); //Reproducimos el sonido de victoria
            dialogo.setTitle("VICTORIA"); //El título será "VICTORIA"
        }
        else { //Si ha perdido
            playSound(R.raw.derrota); //Reproducimos el sonido de derrota
            dialogo.setTitle("DERROTA"); //El título será "DERROTA"
        }
        dialogo.setCancelable(false); //Establecemos que no es cancelable para que no se pueda cerrar al pulsar en otro lado
        dialogo.setMessage(mensaje); //Ponemos el mensaje recibido en el diálogo
        dialogo.setPositiveButton("Jugar otra vez", new DialogInterface.OnClickListener() { //Ponemos un botón para jugar de nuevo
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogoAbierto=false; //Ya no hay diálogo abierto
                reiniciarJuego(); //Reiniciamos el juego
            }
        });
        dialogo.setNegativeButton("Salir", new DialogInterface.OnClickListener() { //Ponemos un botón para salir de la app
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogoAbierto=false;
                finish(); //Terminamos la actividad
            }
        });
        dialogoAbierto=true;
        dialogo.show(); //Mostramos el diálogo
    }

    /**
     * Método que reinicia el juego para comenzar una nueva partida
     */
    public void reiniciarJuego(){
        constraintLayout.removeAllViews(); //Quitamos lo existente en el layout
        iniciarJuego(); //Iniciamos el juego de nuevo
    }

    /**
     * Método que comprueba si el usuario ha ganado para mostrar el diálogo de victoria
     */
    public void comprobarVictoria(){
        if(minasRestantes==0){ //Si no quedan minas por marcar
            mostrarDialogoFinPartida("Has ganado! Todas las minas has sido marcadas",true); //Mostramos un diálogo de fin de partida con mensaje de victoria
            //No se ha considerado necesario descubrir todas las casillas sin mina para ganar ya que según los requisitos la condición de victoria es "El usuario gana cuando ha marcado correctamente todas las minas"
        }
    }

    /**
     * Método para mostrar un diálogo con instrucciones en pantalla
     */
    public void mostrarInstrucciones(){
        dialogo = new AlertDialog.Builder(this); //Inicializamos el dialogo
        dialogo.setCancelable(false); //Establecemos que no es cancelable para que no se pueda cerrar al pulsar en otro lado
        dialogo.setTitle("Instrucciones"); //Ponemos el título como "Instrucciones"
        String instruccionesMensaje="Cuando pulsas en una casilla, sale un número que identifica cuántas minas hay alrededor. Ten cuidado porque si pulsas en una casilla que tenga una mina escondida, perderás. Si crees o tienes la certeza de que hay una mina, haz un click largo sobre la casilla para señalarla. No hagas un click largo en una casilla donde no hay una mina porque perderás. Ganas una vez hayas encontrado todas las minas"; //Creamos el mensaje
        dialogo.setMessage(instruccionesMensaje); //Establecemos el mensaje del diálogo
        dialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() { //Ponemos un botón para cerrarlo
            public void onClick(DialogInterface dialog, int id) {
                dialogoAbierto=false;//Ya no hay diálogo abierto
            }
        });
        dialogoAbierto=true;
        dialogo.show(); //Mostramos el diálogo
    }

    /**
     * Método para mostrar un diálogo que permita mostrar las opciones de dificultad y cambiarla
     */
    public void mostrarOpcionesConfiguracion(){
        dialogo = new AlertDialog.Builder(this); //Inicializamos el diálogo
        dialogo.setTitle("Dificultad"); //Ponemos "Dificultad" como título
        dialogo.setCancelable(false); //Establecemos que no es cancelable para que no se pueda cerrar al pulsar en otro lado
        String opciones[]={"Nivel principiante","Nivel amateur","Nivel Avanzado"}; //Creamos las opciones a mostrar
        dialogo.setSingleChoiceItems(opciones, dificultad, new DialogInterface.OnClickListener() { //Ponemos los RadioButtons para elegir la dificultad. Por defecto está seleccionado el de la dificultad actual
            @Override
            public void onClick(DialogInterface dialog, int which) { //Cuando se selecciona uno nuevo
                dificultad=which; //Ponemos la dificultad a la seleccionada
            }
        });
        dialogo.setPositiveButton("Volver", new DialogInterface.OnClickListener() { //Botón para cerrar la configuración
            public void onClick(DialogInterface dialog, int id) {
                dialogoAbierto=false;//Ya no hay diálogo abierto
                Toast.makeText(getApplicationContext(),"La nueva dificultad se aplicará en la próxima partida", Toast.LENGTH_SHORT).show(); //Informamos al usuario de que la próxima partida tendrá la nueva dificultad
            }
        });
        dialogoAbierto=true;
        dialogo.show(); //Mostramos el diálogo
    }

    /**
     * Método para mostrar un diálogo que permita elegir el icono de las minas
     */
    public void cambiarIconoMinas(){
        dialogo=new AlertDialog.Builder(this); //Inicializamos el dialogo
        dialogo.setTitle("Icono de minas"); //Ponemos "Icono de minas" como título
        dialogo.setCancelable(false); //Hacemos que no se pueda cerrar pulsando en otro lado
        String opciones[]={"Mina clásica","Dinamita","Granada","Mina submarina","Cóctel molotov","Bomba atómica"}; //Creamos las opciones a mostrar
        int iconosMinas[]={R.drawable.bomba,R.drawable.dinamita,R.drawable.granada,R.drawable.minasubmarina,R.drawable.coctelmolotov,R.drawable.bombaatomica}; //Array con los ids de los iconos de las minas
        int iconosMinasMuertas[]={R.drawable.bombamuerta,R.drawable.dinamitamuerta,R.drawable.granadamuerta,R.drawable.minasubmarinamuerta,R.drawable.coctelmolotovmuerto,R.drawable.bombaatomicamuerta}; //Array con los ids de los iconos de las minas muertas
        dialogo.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { //Al seleccionar una opcion
                iconoMina=iconosMinas[which]; //Actualizamos el icono de mina
                iconoMinaMuerta=iconosMinasMuertas[which]; //Actualizamos el icono de mina muerta
                Toast.makeText(getApplicationContext(),"Icono cambiado a "+opciones[which], Toast.LENGTH_SHORT).show(); //Informamos al usuario del nuevo icono
                actualizarMinas(); //Actualizamos las minas ya puestas
                dialogoAbierto=false;//Ya no hay diálogo abierto
            }
        });
        dialogoAbierto=true;
        dialogo.show(); //Mostramos el diálogo
    }

    /**
     * Método que actualiza el icono de las minas ya marcadas y el icono del menú de elegir mina con el nuevo icono seleccionado
     */
    public void actualizarMinas(){
        invalidateOptionsMenu(); //Este método declara que las opciones del menu han cambiado y se debe volver a hacer, para actualizar el icono de la opcion de elegir el icono de las minas
        for(int i=0;i<matriz.length;i++){ //Recorremos la matriz interna
            for(int j=0;j<matriz[0].length;j++){
                int index=i*columnas+j; //Calculamos el índice de la casilla en el gridLayout
                if(gridLayout.getChildAt(index) instanceof ImageButton && gridLayout.getChildAt(index).getTag().equals("Marcada")){ //Si lo que hay en la casilla es una mina y está marcada
                    ((ImageButton) gridLayout.getChildAt(index)).setImageResource(iconoMina); //Actualizamos su icono
                }
            }
        }
    }
}