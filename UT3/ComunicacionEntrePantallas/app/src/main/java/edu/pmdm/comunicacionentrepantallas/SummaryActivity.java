package edu.pmdm.comunicacionentrepantallas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class SummaryActivity extends AppCompatActivity {
    private TextView nombreTextView;
    private TextView edadTextView;
    private TextView ciudadTextView;
    private TextView preferenciaTextView;
    private Button regresar;
    private Button editar;
    private ActivityResultLauncher<Intent> editActivityLauncher;

    private String nombre="";
    private int edad=0;
    private String ciudad="";
    private String preferencia="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Inicializamos variables de vistas
        nombreTextView=findViewById(R.id.txtNombre);
        edadTextView=findViewById(R.id.txtEdad);
        ciudadTextView=findViewById(R.id.txtCiudad);
        preferenciaTextView=findViewById(R.id.txtPreferencia);
        regresar=findViewById(R.id.btnRegresar);
        editar=findViewById(R.id.btnEditar);
        //OnClick del botón registrar
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Datos guardados correctamente",Toast.LENGTH_LONG).show(); //Mensaje simulando que se han guardado datos
                setResult(RESULT_OK);
                finish(); //Terminamos la actividad, volviendo a la primera pantalla
            }
        });
        //OnClick del botón editar
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                //Ponemos la información en el intent
                intent.putExtra("Nombre",nombre);
                intent.putExtra("Edad",edad);
                intent.putExtra("Ciudad",ciudad);
                intent.putExtra("Preferencia",preferencia);
                editActivityLauncher.launch(intent); //Lanzamos la actividad de edicion
            }
        });
        //Recuperamos los datos del intent que invocó la actividad
        Intent intent=getIntent();
        nombre=intent.getStringExtra("Nombre");
        edad=intent.getIntExtra("Edad",0);
        ciudad=intent.getStringExtra("Ciudad");
        preferencia=intent.getStringExtra("Preferencia");
        //Actualizamos texto con los datos recibidos
        actualizarDatos();

        editActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            //Obtenemos los datos del intent
                            nombre=data.getStringExtra("Nombre");
                            edad=data.getIntExtra("Edad",0);
                            ciudad=data.getStringExtra("Ciudad");
                            preferencia=data.getStringExtra("Preferencia");
                            actualizarDatos(); //Actualizamos los datos de esta pantalla
                        }
                    }
                });
    }




    public void actualizarDatos(){
        nombreTextView.setText("Nombre: "+nombre);
        edadTextView.setText("Edad: "+edad);
        ciudadTextView.setText("Ciudad: "+ciudad);
        preferenciaTextView.setText("Preferencia: "+preferencia);
    }
}