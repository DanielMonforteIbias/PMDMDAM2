package es.riberadeltajo.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    final int NUMERO_COLUMNAS=4;
    final int NUMERO_FILAS=10;

    Button[] botones=new Button[NUMERO_COLUMNAS*NUMERO_FILAS];
    int[] colores=new int[NUMERO_COLUMNAS*NUMERO_FILAS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random r=new Random();
        GridLayout g=findViewById(R.id.grid);
        Clicker c=new Clicker();
        g.setRowCount(NUMERO_FILAS);
        g.setColumnCount(NUMERO_COLUMNAS);

        for(int i=0;i<NUMERO_FILAS*NUMERO_COLUMNAS;i++) {
            botones[i] = new Button(this);
            botones[i].setText("B"+i/NUMERO_COLUMNAS+""+i%NUMERO_COLUMNAS);
            colores[i]=Color.rgb(r.nextInt(),r.nextInt(),r.nextInt());
            botones[i].setBackgroundColor(colores[i]);
            botones[i].setOnClickListener(c);
            botones[i].setId(View.generateViewId());
            g.addView(botones[i]);
        }
        Button reset=new Button(this);
        reset.setText("reset!");
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<NUMERO_FILAS*NUMERO_COLUMNAS;i++)
                    botones[i].setBackgroundColor(colores[i]);
            }
        });
        g.addView(reset);


    }

    public class Clicker implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Button b=(Button) view;
            b.setBackgroundColor(Color.rgb(255,255,255));
        }
    }
}