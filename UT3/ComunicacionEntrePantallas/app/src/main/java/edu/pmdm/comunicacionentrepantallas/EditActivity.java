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

public class EditActivity extends AppCompatActivity {

    private EditText nombreEditText;
    private EditText edadEditText;
    private EditText ciudadEditText;
    private RadioGroup preferenciaRadioGroup;
    private Button boton;
    private ActivityResultLauncher<Intent> summaryActivityLauncher;

    private String nombre="";
    private int edad=0;
    private String ciudad="";
    private String preferencia="Playa";
    private int selectedPreference=0; //Variable para que aparezca por defecto seleccionado el rb de la preferencia
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Inicializar variables con los datos recibidos del intent
        Intent intent=getIntent();
        nombre=intent.getStringExtra("Nombre");
        edad=intent.getIntExtra("Edad",0);
        ciudad=intent.getStringExtra("Ciudad");
        preferencia=intent.getStringExtra("Preferencia");
        if(preferencia.equals("Playa")) selectedPreference=R.id.rbPlaya;
        else if (preferencia.equals("Montaña")) selectedPreference=R.id.rbMontaña;
        //Inicializar variables de vistas
        nombreEditText=findViewById(R.id.editTextNombre);
        edadEditText=findViewById(R.id.editTextEdad);
        ciudadEditText=findViewById(R.id.editTextCiudad);
        boton=findViewById(R.id.btnGuardarCambios);
        preferenciaRadioGroup=findViewById(R.id.rgPreferencia);

        //Asignar a las vistas los valores recibidos
        nombreEditText.setText(nombre);
        edadEditText.setText(edad+"");
        ciudadEditText.setText(ciudad);
        preferenciaRadioGroup.check(selectedPreference); //Seleccionamos el que corresponda a la preferencia que hemos recibido

        //Listener del radiogroup
        preferenciaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.rbPlaya) preferencia="Playa";
                else if (checkedId==R.id.rbMontaña) preferencia="Montaña";
            }
        });
        //Listener del botón para guardar cambios
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean intentValido=true;
                nombre=nombreEditText.getText().toString();
                ciudad=ciudadEditText.getText().toString();
                //preferencia se modifica en el listener de su radioGroup
                try{
                    edad=Integer.parseInt(edadEditText.getText().toString()); //Intentamos pasar la edad a int
                }
                catch(Exception e){//Si no se puede capturamos la excepcion
                    Toast.makeText(getApplicationContext(),"La edad no es válida",Toast.LENGTH_LONG).show(); //Mostramos un mensaje con el error
                    intentValido=false;
                }
                if(nombre.equals("") || ciudad.equals("")){
                    Toast.makeText(getApplicationContext(),"Todos los campos deben estar rellenos",Toast.LENGTH_LONG).show();
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
                if(intentValido) {
                    Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
                    intent.putExtra("Nombre",nombre);
                    intent.putExtra("Edad",edad);
                    intent.putExtra("Ciudad",ciudad);
                    intent.putExtra("Preferencia",preferencia);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }
    public boolean contieneNumeros(String s){
        return s.matches(".*\\d+.*"); //Devuelve true si hay algun digito en el string, false si no
    }
}