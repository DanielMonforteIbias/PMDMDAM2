package com.example.prc3_danielmonforte;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.prc3_danielmonforte.bikes.BikesContent;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.prc3_danielmonforte.databinding.ActivityBikeBinding;

public class BikeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityBikeBinding binding;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String fecha=""; //Variable para guardar la fecha seleccionada en el CalendarView de FirstFragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE); //Obtenemos el archivo de preferencias
        editor=sharedPreferences.edit(); //Inicializamos el editor
        fecha=sharedPreferences.getString("Fecha",""); //Obtenemos la fecha guardada, o vacía si no existe
        binding = ActivityBikeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.violeta)); //Ponemos el color de la toolbar

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_bike);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        BikesContent.loadBikesFromJSON(this); //Cargamos los datos del JSON de bicicletas en la lista estática de BikesContent
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_bike);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim); //Animaciones al cambiar de actividad al regresar
    }
}