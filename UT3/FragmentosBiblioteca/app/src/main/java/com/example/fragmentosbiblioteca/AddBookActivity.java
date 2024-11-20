package com.example.fragmentosbiblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddBookActivity extends AppCompatActivity {
    private EditText editTextTitulo;
    private EditText editTextAutor;
    private EditText editTextAño;
    private EditText editTextDescripcion;
    private Button btnGuardarLibro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextTitulo=findViewById(R.id.editTextTitulo);
        editTextAutor=findViewById(R.id.editTextAutor);
        editTextAño=findViewById(R.id.editTextAño);
        editTextDescripcion=findViewById(R.id.editTextDescripcion);
        btnGuardarLibro=findViewById(R.id.btnGuardarLibro);

        btnGuardarLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean intentValido=true;
                String titulo=editTextTitulo.getText().toString();
                String autor=editTextAutor.getText().toString();
                String añoString=editTextAño.getText().toString();
                String descripcion=editTextDescripcion.getText().toString();
                int año=0;
                if(titulo.equals("")||autor.equals("")||añoString.equals("")||descripcion.equals("")){
                    showToast("Los campos deben estar rellenos");
                    intentValido=false;
                }
                else{
                    año=Integer.parseInt(añoString); //Si hemos llegado aquí sabemos que año es un Int
                }
                if(intentValido){
                    Book libro=new Book(titulo,autor,año,descripcion);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Libro",(Parcelable) libro);
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