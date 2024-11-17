package edu.pmdm.intentsavanzado;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {
    private Button btnPaginaWeb;
    private Button btnEnviarCorreo;
    private Button btnTomarFoto;
    private Button btnConfiguracion;

    private String paginaWeb="https://google.com";
    private String correo="";
    private String asunto="";
    private String mensaje="";

    private ActivityResultLauncher<Intent>configuracionLauncher;
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
        //Inicializar las vistas
        btnPaginaWeb=findViewById(R.id.btnPaginaWeb);
        btnEnviarCorreo=findViewById(R.id.btnCorreo);
        btnTomarFoto=findViewById(R.id.btnTomarFoto);
        btnConfiguracion=findViewById(R.id.btnConfiguracion);
        //Listener del botón página web
        btnPaginaWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Patterns.WEB_URL.matcher(paginaWeb).matches()) { //Si la variable paginaWeb no es una URL válida
                    String query = Uri.encode(paginaWeb);
                    paginaWeb="https://www.google.com/search?q=" + query; //Haremos una búsqueda en Google
                }
                //Si paginaWeb era una URL válida se abrirá directamente en el navegador
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(paginaWeb)); //Hacemos el intent
                startActivity(intent);
            }
        });
        //Listener del botón enviar correo
        btnEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correo.equals(""))showToast("El correo no se ha configurado");
                else {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    String uriText = "mailto:" + Uri.encode(correo) + "?subject=" + Uri.encode(asunto) + "&body=" + Uri.encode(mensaje); //Crear el Uri con destinatario, asunto y mensaje. Al tener mailto solo dejara usar apps de correo
                    intent.setData(Uri.parse((uriText))); //Poner los datos del uri en el intent
                    startActivity(Intent.createChooser(intent, "Elige aplicacion:"));
                }
            }
        });
        //Listener del botón tomar foto
        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        });
        //Listener del botón configuracion
        btnConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Configuracion.class);
                configuracionLauncher.launch(intent);
            }
        });
        //Launcher de configuracion
        configuracionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if(data.getExtras().containsKey("Url")){
                                paginaWeb=data.getStringExtra("Url");
                            }
                            else{
                                correo=data.getStringExtra("Correo");
                                asunto=data.getStringExtra("Asunto");
                                mensaje=data.getStringExtra("Mensaje");
                            }
                        }
                    }
                });
    }
    public void showToast(String mensaje){
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show();
    }
}