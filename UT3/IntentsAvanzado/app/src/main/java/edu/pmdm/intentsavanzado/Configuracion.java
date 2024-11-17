package edu.pmdm.intentsavanzado;

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

public class Configuracion extends AppCompatActivity {

    private Button btnGuardarPaginaWeb;
    private Button btnGuardarCorreo;
    private EditText editTextPaginaWeb;
    private EditText editTextCorreo;
    private EditText editTextAsunto;
    private EditText editTextMensaje;

    private String url="";
    private String correo="";
    private String asunto="";
    private String mensaje="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuracion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Inicializar vistas
        editTextPaginaWeb=findViewById(R.id.editTextPaginaWeb);
        editTextCorreo=findViewById(R.id.editTextCorreo);
        editTextAsunto=findViewById(R.id.editTextAsunto);
        editTextMensaje=findViewById(R.id.editTextMensaje);
        btnGuardarPaginaWeb=findViewById(R.id.btnGuardarPaginaWeb);
        btnGuardarCorreo=findViewById(R.id.btnGuardarCorreo);
        //Listener guardar pagina
        btnGuardarPaginaWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean intentValido=true;
                url=editTextPaginaWeb.getText().toString();
                if(url.equals("")){
                    showToast("La URL no puede estar vacía");
                    intentValido=false;
                }
                if(intentValido){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Url",url);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
        //Listener guardar correo
        btnGuardarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean intentValido=true;
                correo=editTextCorreo.getText().toString();
                asunto=editTextAsunto.getText().toString();
                mensaje=editTextMensaje.getText().toString();
                if(correo.equals("") || asunto.equals("")||mensaje.equals("")){
                    showToast("Todos los campos deben ser rellenados");
                    intentValido=false;
                }
                else{
                    if(!correoValido(correo)){
                        showToast("El correo no es válido");
                        intentValido=false;
                    }
                }
                if(intentValido){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Correo",correo);
                    intent.putExtra("Asunto",asunto);
                    intent.putExtra("Mensaje",mensaje);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }
    public boolean correoValido(String correo){
        return correo.matches("^[a-zA-Z0-9_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
    }
    public void showToast(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show();
    }
}