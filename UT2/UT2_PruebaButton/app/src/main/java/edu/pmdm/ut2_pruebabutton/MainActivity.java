package edu.pmdm.ut2_pruebabutton;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaración de variables
    Button miBoton, miBoton2;
    TextView miTexto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializa los widgets después de inflar el layout
        //Hay que inicializarlos en el método onCreate
        miBoton = findViewById(R.id.btnBotonPrueba);
        miTexto = findViewById(R.id.txtMiTexto);

        //Botón con solo texto y color amarillo
        miBoton2 = findViewById(R.id.btnSoloTexto);

        // Asigna el listener (FORMA1) para el botón, de modo que se dispare cuando se haga clic
        miBoton.setOnClickListener(this);

        //Otra forma (FORMA 2) de crear el listener y el evento onClick (para miBoton2)
        miBoton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miBoton2.setText("Pulsado");
                miBoton2.setTextSize(55);
                // Color Rojo
                miBoton2.setTextColor(Color.parseColor("blue"));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    //Hacemos referencia al evento onClick(FORMA1)
    @Override
    public void onClick(View view) {

        // Inicializa la referencia al TextView y cambia su texto a "pulsado"
        miTexto.setText("Pulsado");
        miTexto.setTextSize(84);
        // Color Rojo
        miTexto.setTextColor(Color.parseColor("#FF0000"));
    }
}