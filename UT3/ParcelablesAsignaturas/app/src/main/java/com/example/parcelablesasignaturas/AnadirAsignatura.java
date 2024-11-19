package com.example.parcelablesasignaturas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnadirAsignatura extends AppCompatActivity {
    private Button botonAnadir;
    private EditText editTextAsignatura;
    private EditText editTextNota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anadir_asignatura);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        botonAnadir=findViewById(R.id.btnAnadir);
        editTextAsignatura=findViewById(R.id.editTextAsignatura);
        editTextNota=findViewById(R.id.editTextNota);
        botonAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean intentValido=true;
                String nombreAsignatura=editTextAsignatura.getText().toString();
                String notaString=editTextNota.getText().toString();
                double nota=0;
                if(nombreAsignatura.equals("") || notaString.equals("")){
                    showToast("Los campos no pueden estar vacíos");
                    intentValido=false;
                }
                else{ //Si noe stán vacíos
                    nota=Double.parseDouble(notaString); //Sabemos que lo que hay en el EditText de nota es un número, porque es de tipo Number Signed
                    if(nota<0 ||nota>10){
                        showToast("La nota debe estar entre 0 y 10");
                        intentValido=false;
                    }
                }
                if(intentValido){
                    Asignatura asignatura=new Asignatura(nombreAsignatura,nota);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Asignatura",asignatura);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }
    public void showToast(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show();
    }
}