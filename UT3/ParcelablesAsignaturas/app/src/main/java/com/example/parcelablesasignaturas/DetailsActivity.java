package com.example.parcelablesasignaturas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailsActivity extends AppCompatActivity {
    private Button botonVolver;
    private TextView textViewNombre;
    private TextView textViewEdad;
    private TextView textViewNotaMedia;
    private TextView textViewAsignaturas;
    private TextView textViewJson;
    private Estudiante estudiante;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent i=getIntent();
        estudiante=i.getParcelableExtra("Estudiante"); //Obtenemos el estudiante del intent
        //Inicializamos vistas
        textViewNombre=findViewById(R.id.txtNombreDetalles);
        textViewEdad=findViewById(R.id.txtEdadDetalles);
        textViewNotaMedia=findViewById(R.id.txtNotaMediaDetalles);
        textViewAsignaturas=findViewById(R.id.txtAsignaturas);
        textViewJson=findViewById(R.id.txtJSON);
        //Ponemos los datos
        textViewNombre.setText("Nombre: "+estudiante.getNombre());
        textViewEdad.setText("Edad: "+estudiante.getEdad());
        textViewNotaMedia.setText("Nota media: "+estudiante.getNotaMedia());
        String asignaturas="";//Variable para guardar solo los nombres de las asignaturas
        for(Asignatura a:estudiante.getAsignaturas()){ //Recorremos la lista de asignaturas
            asignaturas+=a.getNombre()+"   ";
        }
        textViewAsignaturas.setText("Asignaturas: "+asignaturas);

        textViewJson.setText(estudiante.toString()); //Directamente ponemos el toString del estudiante
        botonVolver=findViewById(R.id.btnVolver);
        //Listener del boton para volver a la pantalla principal
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}