package com.example.prc3_danielmonforte;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prc3_danielmonforte.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private String correo=""; //Variable para el correo que introduce el usuario
    public static String direccion=""; //Variable para la direccion que introduce el usuario
    private boolean intentLoginValido=true; //Variable booleana que servira para decidir si podemos lanzar o no un intent

    public final int PERMISO_UBICACION=1;
    private LocationManager locationManager;
    private Location ubicacion=null;

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

        //Listener del botón de login
        mainBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //comprobarCampos();
                if(intentLoginValido){ //Si todos los datos son correctos
                    Intent intent = new Intent(getApplicationContext(), BikeActivity.class); //Creamos un intent de BikeActivity
                    bikeActivityLauncher.launch(intent); //Lanzamos la actividad de BikeActivity
                    overridePendingTransition(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_exit_anim); //Animaciones al cambiar de actividad
                }
            }
        });
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
                overridePendingTransition(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim); //Animaciones al cambiar de actividad
            }
        });

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISO_UBICACION)
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) abrirIntentMaps(); //Si se ha concedido el permiso, abrimos Google Maps
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
     * Método que abre un intent en Google Maps, si es posible, en la ubicación especificada.
     * También maneja si la ubicación es nula, o si el GPS no está activado
     */
    public void abrirIntentMaps(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            pedirPermisosUbicacion();
            return;
        }
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //Si está activado el GPS
            ubicacion=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //Obtenemos la ultima ubicacion conocida
            if (ubicacion!=null){ //Si la ubicacion no es nula
                double latitud=ubicacion.getLatitude();
                double longitud=ubicacion.getLongitude();
                direccion ="geo:"+latitud+","+longitud;
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(direccion)); //Creamos un intent con la Uri creada
                if(intent.resolveActivity(getPackageManager())!=null) startActivity(intent); //Si hay apps para abrir el intent, se hace
                else showToast("No se pudo abrir Maps"); //Si no, se avisa al usuario
                //Es importante poner el apartado de queries en el manifest para que esto funcione, si no devuelve null aunque sí haya apps para abrirlo
            }
            else { //Si no hay ubicacion
                showToast("No se pudo obtener la ubicacion. Tratando de obtener alguna ubicación..."); //Avisamos al usuario de que se está intentando obtener
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, new LocationListener() { //Solicitamos una actualizacion de localizacion
                    @Override
                    public void onLocationChanged(Location location) { //Cuando se obtiene una nueva localizacion
                        locationManager.removeUpdates(this); //Eliminamos actualizaciones para quedarnos con la primera
                        showToast("Se obtuvo una ubicacion!"); //Avisamos al usuario
                    }
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    @Override
                    public void onProviderEnabled(String provider) {}

                    @Override
                    public void onProviderDisabled(String provider) {}
                });
            }
        }
        else showToast("El GPS no está activado"); //Si no está activado el GPS, avisamos al usuario
    }
    /**
     * Método que comprueba que todos los campos estén rellenos y tengan el formato correcto
     */
    public void comprobarCampos(){
        intentLoginValido=true; //En un principio es true
        if(direccion.equals("")){ //Si la dirección está vacía
            showToast("Debes introducir la dirección");
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
        return correo.matches("^[a-zA-Z0-9_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
    }

    /**
     * Método que construye un Toast de duración corta con un mensaje recibido y lo muestra
     * @param mensaje el mensaje a incluir en el toast
     */
    public void showToast(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
}