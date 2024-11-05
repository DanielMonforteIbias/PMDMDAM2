package com.example.contextproyect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
//En este ejemplo, usaremos el contexto para acceder a varios servicios y recursos del sistema, como SharedPreferences, AlarmManager, y AudioManager.
    private AudioManager audioManager;
    private SeekBar volumeSeekBar;
    private Button botonFondoAzul;
    private Button botonFondoBlanco;
    private Button buttonStartActivity;
    private Button buttonReadVolume;
    private ConstraintLayout layout;
    private RadioGroup radioGroupColorBotones;
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

        // Inicializar AudioManager para gestionar el volumen del dispositivo
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Inicializar SeekBar para controlar el volumen
        volumeSeekBar = findViewById(R.id.volumeSeekBar);

        // Obtener el volumen máximo del dispositivo y configurarlo en la SeekBar
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);

        /**
         * Obtiene una instancia de SharedPreferences para leer y guardar datos persistentes.
         *
         * Se usa SharedPreferences para almacenar pequeños conjuntos de datos, como configuraciones
         * o preferencias del usuario, de manera persistente. Esto significa que los datos
         * se conservarán incluso después de cerrar la aplicación.
         *
         * @param "MyPrefs" El nombre del archivo donde se guardarán las preferencias.
         *                  Puedes elegir cualquier nombre, pero debes usar el mismo nombre
         *                  al acceder a estas preferencias más tarde.
         * @param Context.MODE_PRIVATE El modo de acceso, que asegura que el archivo de
         *                             preferencias solo sea accesible por esta aplicación.
         *                             Es la opción más segura para proteger los datos.
         * @return SharedPreferences El objeto SharedPreferences para leer y guardar datos.
         */
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Leer el volumen guardado de SharedPreferences, o usar el volumen actual si no hay ninguno guardado
        int savedVolume = sharedPreferences.getInt("audioLevel", audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(savedVolume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, savedVolume, 0);

        // Listener para detectar cambios en la SeekBar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Ajustar el volumen con AudioManager
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

                // Guardar el nivel de audio en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("audioLevel", progress);
                editor.apply();

                // Mostrar un Toast con el nivel de volumen guardado
                Toast.makeText(MainActivity.this, "Volumen guardado: " + progress, Toast.LENGTH_SHORT).show();
            }
            /**
             * Método llamado cuando el usuario comienza a tocar o interactuar con la SeekBar.
             *
             * Aunque no se necesita hacer nada específico aquí en este ejemplo, este método debe estar
             * presente porque forma parte de la interfaz SeekBar.OnSeekBarChangeListener.
             * Puedes usarlo para pausar acciones, registrar eventos o realizar otras tareas
             * cuando el usuario empiece a ajustar la SeekBar.
             *
             * @param seekBar La SeekBar que se está tocando.
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }


            /**
             * Método llamado cuando el usuario deja de tocar o interactuar con la SeekBar.
             *
             * Al igual que con onStartTrackingTouch, no se necesita hacer nada en este ejemplo,
             * pero el método debe estar implementado porque es obligatorio por la interfaz
             * SeekBar.OnSeekBarChangeListener. Puedes usarlo para reanudar acciones, guardar
             * datos, o realizar otras tareas cuando el usuario termine de ajustar la SeekBar.
             *
             * @param seekBar La SeekBar que se dejó de tocar.
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Botón para leer el volumen guardado
        buttonReadVolume = findViewById(R.id.buttonReadVolume);
        buttonReadVolume.setOnClickListener(v -> {
            int currentSavedVolume = sharedPreferences.getInt("audioLevel", -1);
            Toast.makeText(MainActivity.this, "Volumen leido: " + currentSavedVolume, Toast.LENGTH_SHORT).show();
        });

        // Botón para iniciar la segunda actividad
        buttonStartActivity = findViewById(R.id.buttonStartActivity);
        buttonStartActivity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        //Layout del que cambiaremos el fondo
        layout=findViewById(R.id.main);

        //Ponemos el fondo al color guardado, o a blanco si no hay guardado
        int savedBackgroundColor = sharedPreferences.getInt("backgroundColor",Color.WHITE);
        layout.setBackgroundColor(savedBackgroundColor);


        //Botón para cambiar el fondo a azul
        botonFondoAzul =findViewById(R.id.btnFondoAzul);
        botonFondoAzul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setBackgroundColor(Color.BLUE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("backgroundColor", Color.BLUE);
                editor.apply();
                Toast.makeText(MainActivity.this, "Color cambiado a azul", Toast.LENGTH_SHORT).show();
            }
        });
        //Botón para cambiar el fondo a blanco
        botonFondoBlanco=findViewById(R.id.btnFondoBlanco);
        botonFondoBlanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setBackgroundColor(Color.WHITE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("backgroundColor", Color.WHITE);
                editor.apply();
                Toast.makeText(MainActivity.this, "Color cambiado a blanco", Toast.LENGTH_SHORT).show();
            }
        });

        int savedButtonColor = sharedPreferences.getInt("buttonColor",Color.MAGENTA);
        cambiarColorBotones(savedButtonColor);
        //RadioGroup para cambiar color de botones
        radioGroupColorBotones =findViewById(R.id.rdGroupColorBotones);
        radioGroupColorBotones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId==R.id.rbBotonesMorados){
                    cambiarColorBotones(Color.MAGENTA);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("buttonColor", Color.MAGENTA);
                    editor.apply();
                }
                else if (checkedId==R.id.rbBotonesRojos){
                    cambiarColorBotones(Color.RED);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("buttonColor", Color.RED);
                    editor.apply();
                }
            }
        });
    }
    public void cambiarColorBotones(int color){
        botonFondoBlanco.setBackgroundColor(color);
        botonFondoAzul.setBackgroundColor(color);
        buttonReadVolume.setBackgroundColor(color);
        buttonStartActivity.setBackgroundColor(color);
    }
}