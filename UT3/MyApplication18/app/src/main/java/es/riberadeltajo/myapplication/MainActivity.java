package es.riberadeltajo.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final int SELECCION_PROVINCIA=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b=findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abrir una segunda actividad!!! Actividad es un proceso distinto
                Intent i=new Intent(getApplicationContext(),SecondActivity.class);
                startActivityForResult(i,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECCION_PROVINCIA)
            if(resultCode==RESULT_OK){
                //procesar datos devueltos (ver qué provincia se ha seleccionado)
                String retorno=data.getExtras().getString("SELECCION");
                Toast.makeText(this,"El usuario seleccionó "+retorno,Toast.LENGTH_LONG).show();
                TextView t=findViewById(R.id.textView);
                t.setText(retorno);
            }
    }
}