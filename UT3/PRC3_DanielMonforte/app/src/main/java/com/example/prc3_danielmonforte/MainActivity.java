package com.example.prc3_danielmonforte;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prc3_danielmonforte.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static String correo=""; //Variable para el correo que introduce el usuario
    private static String direccion=""; //Variable para la direccion del usuario
    private double latitud=0; //Latitud de la ubicacion del usuario
    private double longitud=0; //Longitud de la ubicacion del usuario
    private boolean intentLoginValido=true; //Variable booleana que servira para decidir si podemos lanzar o no un intent

    public final int PERMISO_UBICACION=1; //Constante para identificar el permiso de ubicacion
    private LocationManager locationManager; //LocationManager para obtener la ubicacion del usuario
    private Location ubicacion=null; //Objeto ubicacion para guardar la ubicacion del usuario
    private LocationListener locationListener;
    private static boolean pantallaActiva = true; //Variable estática para saber si estamos en esta pantalla o no

    private ActivityMainBinding mainBinding; //Binding para acceder a las vistas sin declarar variables y hacer findViewById

    private ActivityResultLauncher<Intent> bikeActivityLauncher; //Launcher para BikeActivity
    private ActivityResultLauncher<Intent> configuracionLauncher; //Launcher para Configuracion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater()); //Inicializamos la variable de binding
        View view= mainBinding.getRoot(); //Obtenemos la vista total del layout
        setContentView(view); //Establecemos el contenido de la vista como el obtenido anteriormente
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        inicializarLocationListener(); //Inicializamos el LocationListener
        if(pantallaActiva) pedirPermisosUbicacion(); //Pedimos permisos si estamos en esta pantalla
        else if(comprobarPermisosUbicacion()){ //Si no, si tenemos permisos
            solicitarActualizacionUbicacion(); //Solicitamos una actualizacion de ubicacion
        }
        crearListeners(); //Creamos los Listeners de los botoness
        inicializarLaunchers(); //Inicializamos los launchers de otras actividades
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISO_UBICACION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) actualizarUbicacion(); //Si se ha concedido el permiso, actualizamos la ubicacion
        }
    }

    /**
     * Método que crea listeners para los diferentes botones e ImageButtons de esta actividad
     */
    public void crearListeners(){
        //Listener del ImageButton para la direccion
        mainBinding.imgBtnDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comprobarPermisosUbicacion()) abrirIntentMaps(); //Si tenemos permisos, abrimos Google Maps
                else {
                    showToast("Los permisos se denegaron anteriormente. Vaya a la configuración de la aplicación para cambiarlo");
                    pedirPermisosUbicacion();
                    //IMPORTANTE. A partir de Android 11, si se deniegan dos veces los permisos se dejará de preguntar, así que avisamos al usuario para que lo cambie desde la configuración de la app
                }
            }
        });
        //Listener del boton de configuracion
        mainBinding.imgBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Configuracion.class); //Creamos un intent de Configuracion
                configuracionLauncher.launch(intent); //Lanzamos la actividad de Configuracion
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //Animaciones al cambiar de actividad
            }
        });

        //Listener del botón de login
        mainBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correo=mainBinding.editTextCorreo.getText().toString();
                comprobarCampos(); //Comprobamos que los campos están bien rellenados
                if(intentLoginValido){ //Si todos los datos son correctos
                    crearNotificacion("Sesion iniciada con exito"); //Enviamos una notificacion de sesion iniciada
                    Intent intent = new Intent(getApplicationContext(), BikeActivity.class); //Creamos un intent de BikeActivity
                    bikeActivityLauncher.launch(intent); //Lanzamos la actividad de BikeActivity
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //Animaciones al cambiar de actividad
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        pantallaActiva=false; //No estamos en esta pantalla
    }

    @Override
    protected void onResume() {
        super.onResume();
        pantallaActiva=true; //Sí estamos en esta pantalla
    }

    /**
     * Método que inicializa los launchers de las Activitys que se pueden lanzar desde esta actividad
     */
    public void inicializarLaunchers(){
        //Inicializamos el launcher de BikeActivity
        bikeActivityLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                }
            }
        });
        //Inicializamos el launcher de Configuracion
        configuracionLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                }
            }
        });
    }
    /**
     * Método que comprueba si tenemos permisos de ubicacion o no
     * @return true si tenemos permisos, false si no
     */
    private boolean comprobarPermisosUbicacion() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Método que pide los permisos de ubicación al usuario
     */
    private void pedirPermisosUbicacion() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_UBICACION);
    }

    /**
     * Método que inicializa el locationListener para obtener cambios en la ubicacion
     */
    public void inicializarLocationListener(){
        locationListener=new LocationListener() { //Solicitamos una actualizacion de localizacion
            @Override
            public void onLocationChanged(Location location) { //Cuando se obtiene una nueva localizacion
                locationManager.removeUpdates(this); //Eliminamos actualizaciones para quedarnos con la primera
                showToast("Se obtuvo una ubicacion!"); //Avisamos al usuario
                actualizarUbicacion(); //Actualizamos la ubicacion
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) { //AL activar el proveedor
                mainBinding.txtDireccion.setText("Buscando ubicacion..."); //Avisamos al usuario
                actualizarUbicacion(); //Actualizamos la ubicacion
            }
            @Override
            public void onProviderDisabled(String provider) { //Al desactivar el proveedor
                mainBinding.txtDireccion.setText("EL GPS se ha desactivado"); //Informamos al usuario
                direccion=""; //Vaciamos la direccion para que no deje entrar
            }
        };
    }
    /**
     * Método que actualiza la ubicación con la del dispositivo, si es posible
     * También maneja si la ubicación es nula, o si el GPS no está activado
     */
    public void actualizarUbicacion(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ubicacion=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //Obtenemos la ultima ubicacion conocida
        if (ubicacion!=null){ //Si la ubicacion no es nula
            latitud=ubicacion.getLatitude(); //Obtenemos la latitud
            longitud=ubicacion.getLongitude(); //Obtenemos la longitud
            direccion ="geo:"+latitud+","+longitud; //Creamos la cadena de la Uri
            mainBinding.txtDireccion.setText("Latitud: "+latitud+"\nLongitud: "+longitud); //Modificamos la informacion del TextView
        }
        else { //Si no hay ubicacion
            solicitarActualizacionUbicacion(); //Solicitamos una actualizacion
        }
    }

    /**
     * Método que solicita actualizacion de la ubicacion del dispositivo
     */
    public void solicitarActualizacionUbicacion(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; //Si no tenemos permisos, volvemos
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, locationListener); //Solicitamos la actualizacion
    }
    /**
     * Método que abre un intent en Google Maps, si es posible, con la direccion del dispositivo
     */
    public void abrirIntentMaps(){
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(direccion)); //Creamos un intent parseando a Uri la cadena creada
        if(intent.resolveActivity(getPackageManager())!=null) startActivity(intent); //Si hay apps para abrir el intent, se hace
        else showToast("No se pudo abrir Maps"); //Si no, se avisa al usuario
        //Es importante poner el apartado de queries en el manifest para que esto funcione, si no devuelve null aunque sí haya apps para abrirlo
    }
    /**
     * Método que comprueba que todos los campos estén rellenos y tengan el formato correcto
     */
    public void comprobarCampos(){
        intentLoginValido=true; //En un principio es true
        if(!comprobarPermisosUbicacion()){ //Si no tenemos permisos de ubicacion
            showToast("No hay permisos de ubicacion"); //Informamos al usuario
            intentLoginValido=false; //El intent no será válido
        }
        else if (direccion.equals("")){ //Si hay permisos pero no ubicacion (por ejemplo, porque no hay gps)
            showToast("No hay ubicacion"); //Informamos al usuario
            intentLoginValido=false; //El intent no será válido
        }
        if(correo.equals("")){ //Si el correo está vacío
            showToast("Debes introducir el correo");
            intentLoginValido=false; //El intent no será válido
        }
        else if(!correoValido(correo)){ //Si el correo no está vacío, pero no es válido
            showToast("El correo no sigue un formato correcto");
            intentLoginValido=false; //El intent no será válido
        }

    }

    /**
     * Método que comprueba si una cadena es un correo válido o no
     * @param correo la cadena a comprobar
     * @return true si sigue el formato de correo válido, false si no
     */
    public boolean correoValido(String correo){
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches(); //Devolvemos el resultado de si el correo concuerda con el patrón o no
    }

    /**
     * Método que construye un Toast de duración corta con un mensaje recibido y lo muestra
     * @param mensaje el mensaje a incluir en el toast
     */
    public void showToast(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }

    /**
     * Método que crea una notificación simple para la aplicación
     * @param texto el texto a mostrar en la notificacion
     */
    public void crearNotificacion(String texto){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); //Creamos el NotificationManager
        NotificationChannel canal = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //Si la API usa canales para manejar notificaciones (a partir de la 26)
            canal = new NotificationChannel("ShareMyBike", "Canal de notificacion de prueba", NotificationManager.IMPORTANCE_DEFAULT); //Creamos el canal
            notificationManager.createNotificationChannel(canal);
        }
        NotificationCompat.Builder constructorNotif = new NotificationCompat.Builder(this, "ShareMyBike") //Construimos la notificacion con el texto recibido
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle("NOTIFICACION")
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(); //Intent vacio para no hacer nada al pulsar la notificacion
        TaskStackBuilder pila = TaskStackBuilder.create(this);
        pila.addParentStack(MainActivity.class);

        pila.addNextIntent(intent); //Añadimos el intent vacío a la pila
        PendingIntent resultadoPendingIntent = pila.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        constructorNotif.setContentIntent(resultadoPendingIntent);
        notificationManager.notify(1, constructorNotif.build()); //Mandamos la notificacion
    }
}