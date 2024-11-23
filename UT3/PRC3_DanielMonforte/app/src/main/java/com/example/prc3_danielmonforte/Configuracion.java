package com.example.prc3_danielmonforte;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prc3_danielmonforte.databinding.ActivityConfiguracionBinding;
import com.example.prc3_danielmonforte.databinding.ActivityMainBinding;

public class Configuracion extends AppCompatActivity {
    private ActivityConfiguracionBinding configuracionBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configuracionBinding = ActivityConfiguracionBinding.inflate(getLayoutInflater()); //Inicializamos la variable de binding
        View view= configuracionBinding.getRoot();
        setContentView(view);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        configuracionBinding.switchModoOscuro.setChecked((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES); //Si el modo oscuro está activado, aparecerá activado el switch
        //La linea (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES sirve para detectar si el móvil tiene activado por defecto el modo oscuro, la usamos para que salga activo el modo oscuro aunque no se haya activado en nuestra app sino en el dispositivo

        //Listener para el switch de modo oscuro
        configuracionBinding.switchModoOscuro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(configuracionBinding.switchModoOscuro.isChecked()){
                    System.out.println("a");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    System.out.println("b");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
        //Listener del boton de volver
        configuracionBinding.btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //Finalizamos la actividad de configuracion
            }
        });
    }
}