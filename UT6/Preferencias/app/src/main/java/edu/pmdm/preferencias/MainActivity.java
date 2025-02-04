package edu.pmdm.preferencias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    EditText edNombre;
    EditText edEmail;
    EditText edEmpresa;
    EditText edEdad;
    EditText edSueldo;
    SharedPreferences misPreferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edNombre=findViewById(R.id.edNombre);
        edEmail=findViewById(R.id.edEmail);
        edEmpresa=findViewById(R.id.edEmpresa);
        edEdad=findViewById(R.id.edEdad);
        edSueldo=findViewById(R.id.edSueldo);


        misPreferencias=getSharedPreferences("Basic Data",MODE_PRIVATE);

        String nombre=misPreferencias.getString("nombre","");
        String empresa=misPreferencias.getString("empresa","Ribera del Tajo");
        String email=misPreferencias.getString("email","cambiame@riberadeltajo.es");
        int edad=misPreferencias.getInt("edad",18);
        float sueldo=misPreferencias.getFloat("sueldo",15000);

        edNombre.setText(nombre);
        edEmpresa.setText(empresa);
        edEmail.setText(email);
        edSueldo.setText(String.valueOf(sueldo));
        edEdad.setText(String.valueOf(edad));

        Button btnGuardar=findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=misPreferencias.edit();
                editor.putString("nombre",edNombre.getText().toString());
                editor.putString("empresa",edEmpresa.getText().toString());
                editor.putString("email",edEmail.getText().toString());
                try {
                    editor.putInt("edad", Integer.parseInt(edEdad.getText().toString()));
                    editor.putFloat("sueldo", Float.parseFloat(edSueldo.getText().toString()));
                }catch (Exception e){
                    //Error de conversión
                }
                if(!editor.commit())
                    Toast.makeText(getApplicationContext(), "Error al grabar las preferencias", Toast.LENGTH_SHORT).show();

            }
        });

        Button btnSiguiente=findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(i);
            }
        });

    }



}