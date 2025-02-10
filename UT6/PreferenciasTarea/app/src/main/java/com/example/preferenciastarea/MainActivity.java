package com.example.preferenciastarea;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.preferenciastarea.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences misPreferencias;
    private SharedPreferences.Editor editor;

    private ActivityResultLauncher<Intent> preferenciasLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        misPreferencias=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=misPreferencias.edit();
        boolean notificacionesActivadas=misPreferencias.getBoolean("notificaciones",false);

        if(notificacionesActivadas)crearNotificacion("Bienvenido a la app!");

        updateUI();

        preferenciasLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        updateUI();
                    }
                });
        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("nombre",binding.edNombre.getText().toString());
                editor.putString("empresa",binding.edEmpresa.getText().toString());
                editor.putString("email",binding.edEmail.getText().toString());
                try {
                    editor.putInt("edad", Integer.parseInt(binding.edEdad.getText().toString()));
                    editor.putFloat("sueldo", Float.parseFloat(binding.edSueldo.getText().toString()));
                }catch (Exception e){
                    //Error de conversión
                }
                if(!editor.commit()) Toast.makeText(getApplicationContext(), "Error al grabar las preferencias", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(), "Preferencias guardadas", Toast.LENGTH_SHORT).show();
                    updateUI();
                }
            }
        });

        binding.btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_preferences) {
            Intent intent = new Intent(this, SettingsActivity.class);
            preferenciasLauncher.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateUI(){
        String nombre=misPreferencias.getString("nombre","usuario");
        String empresa=misPreferencias.getString("empresa","");
        String email=misPreferencias.getString("email","");
        int edad=misPreferencias.getInt("edad",0);
        float sueldo=misPreferencias.getFloat("sueldo",0);
        boolean modoOscuro=misPreferencias.getBoolean("modoOscuro",false);
        String prefijo=misPreferencias.getString("formatoTelefono","(sin prefijo)");
        boolean notificacionesActivadas=misPreferencias.getBoolean("notificaciones",false);

        binding.txtBienvenida.setText("Bienvenido, "+nombre);
        binding.edNombre.setText(nombre);
        binding.edEmpresa.setText(empresa);
        binding.edEmail.setText(email);
        binding.edSueldo.setText(String.valueOf(sueldo));
        binding.edEdad.setText(String.valueOf(edad));
        binding.txtPrefijo.setText("Prefijo: "+prefijo);

        if(modoOscuro) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if(notificacionesActivadas) binding.txtNotificaciones.setText("Notificaciones activadas: Si");
        else binding.txtNotificaciones.setText("Notificaciones activadas: No");
    }

    public void crearNotificacion(String texto){
        System.out.println(texto);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); //Creamos el NotificationManager
        NotificationChannel canal = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //Si la API usa canales para manejar notificaciones (a partir de la 26)
            canal = new NotificationChannel("PreferenciasTarea", "Canal de notificacion", NotificationManager.IMPORTANCE_DEFAULT); //Creamos el canal
            notificationManager.createNotificationChannel(canal);
        }
        NotificationCompat.Builder constructorNotif = new NotificationCompat.Builder(this, "PreferenciasTarea") //Construimos la notificacion con el texto recibido
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