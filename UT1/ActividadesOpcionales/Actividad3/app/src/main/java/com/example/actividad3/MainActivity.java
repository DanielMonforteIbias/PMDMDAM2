package com.example.actividad3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText celsius=findViewById(R.id.editTextCelsius);
        Button btnConvertir=findViewById(R.id.btnConvertir);
        TextView txtFahrenheitResult=findViewById(R.id.txtFahrenheitResult);
        TextView txtKelvinResult=findViewById(R.id.txtKelvinResult);
        TextView txtRankineResult=findViewById(R.id.txtRankineResult);

        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    double celsiusTemp=Double.parseDouble(celsius.getText().toString()); //Obtenemos el valor en celsius
                    double fahrenheitTemp= (double) Math.round(((celsiusTemp * ((double) 9 / 5)) + 32) * 100) /100; //Calculamos el valor en Fahrenheit y lo redondeamos a 2 decimales
                    double kelvinTemp=(double)Math.round((celsiusTemp+273.15)*100)/100; //Calculamos el valor en Kelvin y lo redondeamos a 2 decimales
                    double rankineTemp=(double)Math.round(((celsiusTemp*((double) 9 /5))+491.67)*100)/100; //Calculamos el valor en Rankine y lo redondeamos a 2 decimales
                    txtFahrenheitResult.setText(fahrenheitTemp+" ºF"); //Mostramos el valor en Fahrenheit
                    txtKelvinResult.setText(kelvinTemp+" K"); //Mostramos el valor en Kelvin
                    txtRankineResult.setText(rankineTemp+" ºR"); //Mostramos el valor en Rankine
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, "Valor no válido", Toast.LENGTH_SHORT).show(); //Lanzamos un mensaje de error
                }
            }
        });
    }
}