package edu.pmdm.actividad2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        TextView resultado=(TextView)findViewById(R.id.txtResultado);
        Button botonCalcular=(Button)findViewById(R.id.btnCalcular);
        EditText posicionText=(EditText)findViewById(R.id.editTextNumber);
        botonCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicion=Integer.parseInt(posicionText.getText().toString());
                if (posicion>0){
                    int i=0;
                    int j=1;
                    int primoActual=0;
                    while(i<posicion) {
                        j++;
                        int contadorDivisores=0;
                        for(int k=1;k<=j;k++) {
                            if (j%k==0) contadorDivisores++;
                        }
                        if (contadorDivisores==2) {
                            primoActual=j;
                            i++;
                        }
                    }
                    resultado.setText(primoActual+"");
                }
                else resultado.setText("Posicion no valida");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}