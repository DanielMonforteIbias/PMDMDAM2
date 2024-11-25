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
    private ActivityBikeBinding binding; //Variable para el binding de esta actividad

    public static SharedPreferences sharedPreferences; //Variable para guardar el archivo de preferencias
    public static SharedPreferences.Editor editor; //Editor del archivo de preferencias

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializarPreferencias(); //Inicializamos lo relacionado con las preferencias antes de crear la vista
        binding = ActivityBikeBinding.inflate(getLayoutInflater()); //Inicializamos la variable para el ViewBinding
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.violeta)); //Ponemos el color de la toolbar

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_bike);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        if(BikesContent.ITEMS.isEmpty())BikesContent.loadBikesFromJSON(this); //Cargamos los datos del JSON de bicicletas en la lista estática de BikesContent, solo si no hay datos cargados
        //Se comprueba para que se haga solo una vez, pues si volviesemos atras a Login y volviesemos a iniciar sesión se crearían una segunda vez
    }

    /**
     * Método que inicializa la variable de SharedPreferences y su editor, y recupera una fecha guardada si la había
     */
    public void inicializarPreferencias(){
        sharedPreferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE); //Obtenemos el archivo de preferencias
        editor=sharedPreferences.edit(); //Inicializamos el editor
        BikesContent.selectedDate=sharedPreferences.getString("Fecha",""); //Obtenemos la fecha guardada, o vacía si no existe, y la guardamos en la variable estática de BikesContect
    }
    /**
     * Método que permite navegar hacia arriba entre fragmentos
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_bike);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Método que se ejecuta al terminar la actividad
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim); //Animaciones al cambiar de actividad al regresar
    }
}