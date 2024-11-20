package com.example.parcelablesasignaturas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button anadirAsignatura;
    private RecyclerView listaAsignaturas;

    private ActivityResultLauncher<Intent> anadirAsignaturaLauncher;

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
        DataSource.leerJSONAsignaturas(this); //Leemos el JSON con las asignaturas
        for(Asignatura a:DataSource.ASIGNATURAS){
            System.out.println(a.getNombre());
        }
        //Inicializamos vistas
        listaAsignaturas=findViewById(R.id.listaAsignaturas);
        anadirAsignatura=findViewById(R.id.btnAnadirAsignatura);
        //Creamos el adaptador para el RecyclerView
        AsignaturasAdapter adaptador=new AsignaturasAdapter(DataSource.ASIGNATURAS); //Creamos un adaptador con el ArrayList de asignaturas que hay en DataSource
        listaAsignaturas.setLayoutManager(new LinearLayoutManager(this)); //Sin un layout manager, sale el mensaje "No layout manager attached; skipping layout"
        listaAsignaturas.setAdapter(adaptador); //Establecemos el adaptador a la lista

        //OnClickListener del botón anadirAsignatura
        anadirAsignatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnadirAsignatura.class);
                anadirAsignaturaLauncher.launch(intent); //Lanzamos la actividad de añadir asignatura
            }
        });

        //Launcher de anadirAsignatura. Obtendremos lo que se haya ingresado y lo meteremos en la lista
        anadirAsignaturaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Asignatura asignatura=data.getParcelableExtra("Asignatura"); //Obtenemos la asignatura de la otra actividad
                            DataSource.ASIGNATURAS.add(asignatura); //Lo almacenamos en el ArrayList de DataSoruce
                            adaptador.notifyItemInserted(DataSource.ASIGNATURAS.size() - 1); //Notificamos a la lista para que renderice el nuevo elemento. Sin esta línea no aparecería en la lista
                        }
                    }
                });
    }
}