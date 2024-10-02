package edu.pmdm.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        añadeHijos();
    }

    public void añadeHijos() {
        GridLayout g = (GridLayout) findViewById(R.id.grid1);
        Button b;
        for (int i = 0; i < 18; i++) {
            b = new Button(this);
            b.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            b.setId(View.generateViewId());
            b.setBackgroundColor(Color.rgb(i * 10, i * 50, i * 30));
            b.setOnClickListener(this);
            if(i!=17)
                b.setText("btn" + i);
            else
                b.setText("RESET!");
            g.addView(b, i);
        }
    }

    public void onClick(View v) {
        if (v.getClass().getSimpleName().equals("Button")) {
            Button b = (Button) v;
            acción(b);
        }
    }

    public void acción(Button b) {
        //programa aquí tu acción con el botón b
        if(b.getText().equals("RESET!"))
            Recorrer();
        else
            b.setBackgroundColor(Color.rgb(255,255,255)); //blanco
    }

    //Recorrido de objetos de un contenedor
    public void Recorrer(){
        GridLayout g=(GridLayout)findViewById(R.id.grid1);
        Button b;

        System.out.println("Filas="+g.getRowCount());
        System.out.println("Columnas="+g.getColumnCount());

        for(int i=0;i<g.getChildCount();i++){
            View v;
            v=g.getChildAt(i);
            if(v.getClass().getSimpleName().equals("Button")) {
                b = (Button) v;
                b.setBackgroundColor(Color.rgb(i * 10, i * 50, i * 30));
            }
        }

    }
}