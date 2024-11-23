package com.example.prc3_danielmonforte;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
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

    private ActivityMainBinding mainBinding; //Binding para acceder a las vistas sin declarar variables y hacer findViewById

    private ActivityResultLauncher<Intent> bikeActivityLauncher; //Launcher para BikeActivity
    private ActivityResultLauncher<Intent> configuracionLauncher; //Launcher para Configuracion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater()); //Inicializamos la variable de binding
        View view= mainBinding.getRoot();
        setContentView(view);
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
                if(ContextCompat.checkSelfPermission(MainActivity.this,"android.permission.ACCESS_FINE_LOCATION")== PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this,"android.permission.ACCESS_COARSE_LOCATION")== PackageManager.PERMISSION_GRANTED) {
                    System.out.println("a");
                }
                else if(checkSelfPermission("android.permission.ACCESS_FINE_LOCATION")== PackageManager.PERMISSION_DENIED && checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION")== PackageManager.PERMISSION_DENIED){
                    showToast("Los permisos se denegaron anteriormente. Vaya a la configuración de la aplicación para cambiarlo");
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{"android.permission.ACCESS_FINE_LOCATION,android.permission.ACCESS_COARSE_LOCATION"},PERMISO_UBICACION);
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
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) System.out.println("a");
    }

    public void comprobarCampos(){
        intentLoginValido=true;
        if(direccion.equals("")){
            showToast("Debes introducir la dirección");
            intentLoginValido=false;
        }
        if(correo.equals("")){
            showToast("Debes introducir el correo");
            intentLoginValido=false;
        }
        else if(!correoValido(correo)){
            showToast("El correo no sigue un formato correcto");
            intentLoginValido=false;
        }

    }
    public boolean correoValido(String correo){
        return correo.matches("^[a-zA-Z0-9_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
    }

    public void showToast(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
}