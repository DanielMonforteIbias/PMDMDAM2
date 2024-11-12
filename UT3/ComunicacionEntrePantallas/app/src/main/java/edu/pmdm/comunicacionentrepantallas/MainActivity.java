package edu.pmdm.comunicacionentrepantallas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

    private EditText nombreEditText;
    private EditText edadEditText;
    private EditText ciudadEditText;
    private RadioGroup preferenciaRadioGroup;
    private Button boton;
    private ActivityResultLauncher<Intent> summaryActivityLauncher;

    private String nombre="";
    private int edad=0;
    private String ciudad="";
    private String preferencia="Playa"; //Por defecto tiene el valor del que hay seleccionado al principio
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
        //Inicializamos vistas
        nombreEditText =findViewById(R.id.editTextNombre);
        edadEditText =findViewById(R.id.editTextEdad);
        ciudadEditText =findViewById(R.id.editTextCiudad);
        boton=findViewById(R.id.btnContinuar);
        preferenciaRadioGroup=findViewById(R.id.rgPreferencia);
        //Listener del radiogroup
        preferenciaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rbPlaya) preferencia="Playa"; //Si está seleccionado el rb de playa, la preferencia vale "Playa"
                else if (checkedId==R.id.rbMontaña) preferencia="Montaña"; //Si está seleccionado el rb de montaña, la preferencia vale "Montaña"
            }
        });
        //Listener del botón Continuar
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean intentValido=true;
                nombre=nombreEditText.getText().toString();
                ciudad=ciudadEditText.getText().toString();
                //preferencia se modifica en el listener de su radioGroup
               try{
                    edad=Integer.parseInt(edadEditText.getText().toString()); //Si la edad se puede pasar a int, se pasa
               }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"La edad no es válida",Toast.LENGTH_LONG).show(); //Si no se puede pasar a int, se saca un mensaje
                    intentValido=false;
                }
                if(nombre.equals("") || ciudad.equals("")){ //Si algun campo está vacío
                    Toast.makeText(getApplicationContext(),"Todos los campos deben estar rellenos",Toast.LENGTH_LONG).show(); //Se saca un mensaje
                    intentValido=false;
                }
                if(contieneNumeros(nombre)){ //Si el nombre contiene algun digito
                    Toast.makeText(getApplicationContext(),"El nombre no puede contener números",Toast.LENGTH_SHORT).show(); //Se saca un mensaje
                    intentValido=false;
                }
                if(contieneNumeros(ciudad)){ //Si la ciudad contiene algun digito
                    Toast.makeText(getApplicationContext(),"La ciudad no puede contener números",Toast.LENGTH_SHORT).show(); //Se saca un mensaje
                    intentValido=false;
                }
                if(intentValido) { //Si no ha habido ningun error
                    Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
                    //Ponemos la informacion en el intent para pasarla a la otra pantalla
                    intent.putExtra("Nombre",nombre);
                    intent.putExtra("Edad",edad);
                    intent.putExtra("Ciudad",ciudad);
                    intent.putExtra("Preferencia",preferencia);
                    summaryActivityLauncher.launch(intent); //Lanzamos la actividad de resumen
                }
            }
        });
        summaryActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            //Vaciamos campos
                            nombreEditText.setText("");
                            edadEditText.setText("");
                            ciudadEditText.setText("");
                            preferenciaRadioGroup.check(R.id.rbPlaya); //Marcamos el que hay por defecto siempre
                        }
                    }
                });
    }

    public boolean contieneNumeros(String s){
        return s.matches(".*\\d+.*"); //Devuelve true si hay algun digito en el string, false si no
    }

}