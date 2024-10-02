package es.riberadeltajo.linearlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText edNumero=findViewById(R.id.edNumeroBotones);
        edNumero.setOnEditorActionListener(this);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //¿Cómo crear vistas en tiempo de ejecución?
        //-> 1) inflar un objeto
        //-> 2) crear un objeto de un tipo conocido

        int x=Integer.parseInt(v.getText().toString());
        LinearLayout miLinear=findViewById(R.id.miLinearLayout);
        for(int i=0;i<x;i++){
            View miVista=getLayoutInflater().inflate(R.layout.mi_fila,miLinear,false);
            miLinear.addView(miVista);
        }

        return false;
    }
}