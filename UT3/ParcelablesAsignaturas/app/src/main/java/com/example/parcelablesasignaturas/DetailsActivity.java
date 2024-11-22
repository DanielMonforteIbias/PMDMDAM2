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
    private String nombre="";
    private int edad=0;
    private double notaMedia=0;
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
        nombre=i.getStringExtra("Nombre");
        edad=i.getIntExtra("Edad",0);
        notaMedia=i.getDoubleExtra("NotaMedia",0);
        textViewNombre=findViewById(R.id.txtNombreDetalles);
        textViewEdad=findViewById(R.id.txtEdadDetalles);
        textViewNotaMedia=findViewById(R.id.txtNotaMediaDetalles);
        textViewNombre.setText("Nombre: "+nombre);
        textViewEdad.setText("Edad: "+edad);
        textViewNotaMedia.setText("Nota media: "+notaMedia);
        botonVolver=findViewById(R.id.btnVolver);
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}