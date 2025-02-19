package com.example.prc6_danielmonforte;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.prc6_danielmonforte.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private ArrayList<Recurso> recursos=new ArrayList<>();
    private RecursosAdapter adapter;

    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;
    private final String FILTRO="filtro";
    private int filtro=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferencias.edit();
        filtro=preferencias.getInt(FILTRO,-1); //Obtenemos el filtro guardado, -1 si no existe, que corresponde a TODOS

        adapter = new RecursosAdapter(recursos,this);
        filtrarLista(); //Filtramos la lista de recursos
        //Damos adaptador y layout manager al recyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            mostrarFiltros();
        }
        return super.onOptionsItemSelected(item);
    }

    public void mostrarFiltros() {
        final String[] tipos = {"Todos", "Audio", "Video", "Streaming"}; //Añadimos TODOS como opcion, ademas de los tipos de recursos
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int[] seleccion={filtro+1}; //Usamos un array de tamaño 1 y no una variable int para poder modificar la seleccion dentro de la expresion lambda de setSingleChoiceItems
        builder.setTitle("Selecciona el tipo de recurso")
                .setSingleChoiceItems(tipos, seleccion[0], (dialog, which) -> { //Las opciones van del 0 al 3 en el dialog, pero del -1 al 2 en el tipo, siendo TODOS -1
                    seleccion[0]=which-1; //se resta -1 a which para que coincida, ya que en el dialogo el 0 es TODOS, el 1 es AUDIO..., pero a la hora de filtrar es el -1, audio el 0, video el 1 y streaming el 2
                })
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    editor.putInt(FILTRO, seleccion[0]); //Guardamos en preferencias el nuevo filtro
                    editor.apply();
                    filtro=seleccion[0]; //Ponemos el filtro
                    filtrarLista(); //Filtramos la lista
                })
                .setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    public void filtrarLista(){
        recursos.clear(); //Limpiamos la lista actual
        if(filtro==EnumTipos.TODOS.valor) recursos.addAll(JSONExtractor.loadResources(this)); //Si el filtro es TODOS, añadimos todos los recursos del JSON
        else for(Recurso r:JSONExtractor.loadResources(this)){ //Si no es todos, añadimos solo los recursos cuyo tipo coincida
            if(r.getTipo()==filtro)recursos.add(r);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RecursosAdapter.audioPlayer.stopAudio();
    }
}