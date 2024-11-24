package com.example.parcelablesasignaturas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button botonEnviar;
    private Button botonAñadirAsignatura;
    private EditText editTextNombre;
    private EditText editTextEdad;
    private EditText editTextNotaMedia;
    private EditText editTextNombreAsignatura;
    private EditText editTextNotaAsignatura;

    private int maximoAsignaturas=5;

    private ArrayList<Asignatura> asignaturas=new ArrayList<Asignatura>();
    private ActivityResultLauncher<Intent> detallesLauncher;
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
        //Inicializar vistas
        editTextNombre=findViewById(R.id.editTextNombre);
        editTextEdad=findViewById(R.id.editTextEdad);
        editTextNotaMedia=findViewById(R.id.editTextNotaMedia);
        botonAñadirAsignatura=findViewById(R.id.btnAgregarAsignatura);
        botonEnviar=findViewById(R.id.btnEnviar);
        editTextNombreAsignatura=findViewById(R.id.editTextNombreAsignatura);
        editTextNotaAsignatura=findViewById(R.id.editTextNotaAsignatura);
        //Listener enviar
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean intenValido=true;
                String nombre=editTextNombre.getText().toString();
                String edadString=editTextEdad.getText().toString();
                String notaMediaString=editTextNotaMedia.getText().toString();
                int edad=0;
                double notaMedia=0;
                if(nombre.equals("") || edadString.equals("") || notaMediaString.equals("")){
                    showToast("Todos los parametros deben ser rellenados");
                    intenValido=false;
                }
                else{
                    edad=Integer.parseInt(edadString);
                    notaMedia=Integer.parseInt(notaMediaString);
                    if(notaMedia<0 || notaMedia>10){
                        showToast("La nota media debe estar entre 0 y 10");
                        intenValido=false;
                    }
                }
                if(intenValido){
                    Intent intent=new Intent(getApplicationContext(),DetailsActivity.class);
                    Estudiante e=new Estudiante(nombre,edad,notaMedia,asignaturas);
                    intent.putExtra("Estudiante",e);
                    detallesLauncher.launch(intent);
                }
            }
        });
        //Listener añadir asignatura
        botonAñadirAsignatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asignaturas.size()<maximoAsignaturas){
                    String nombreAsignatura=editTextNombreAsignatura.getText().toString();
                    String notaAsignaturaString=editTextNotaAsignatura.getText().toString();
                    double notaAsignatura=0;
                    boolean asignaturaValida=true;
                    if(nombreAsignatura.equals("")||notaAsignaturaString.equals((""))){
                        showToast("Todos los campos de la asignatura deben estar rellenos");
                        asignaturaValida=false;
                    }
                    else{
                        notaAsignatura=Double.parseDouble(notaAsignaturaString);
                        if(notaAsignatura<0 || notaAsignatura>10){
                            showToast("La nota de la asignatura debe estar entre 0 y 10");
                            asignaturaValida=false;
                        }
                    }
                    for(Asignatura a:asignaturas){
                        if(a.getNombre().equals(nombreAsignatura)){
                            showToast("Ya existe una asignatura con ese nombre");
                            asignaturaValida=false;
                        }
                    }
                    if(asignaturaValida) {
                        asignaturas.add(new Asignatura(nombreAsignatura,notaAsignatura));//Añadimos la asignatura al ArrayList
                        showToast(nombreAsignatura+" añadida");
                        //Vaciamos campos
                        editTextNombreAsignatura.setText("");
                        editTextNotaAsignatura.setText("");
                    }
                }
                else showToast("El maximo de asignaturas es "+maximoAsignaturas);
            }
        });
        //Launcher actividad detalles
        detallesLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                        }
                    }
                });
    }
    public void showToast(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
}