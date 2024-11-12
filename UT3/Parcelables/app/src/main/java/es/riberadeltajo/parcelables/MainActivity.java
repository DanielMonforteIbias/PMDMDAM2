package es.riberadeltajo.parcelables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b=findViewById(R.id.button);
        b.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        SimpleDateFormat fecha=new SimpleDateFormat("dd/mm/yyyy");


        Contacto c= null;
        try {
            c = new Contacto("Paco",fecha.parse("25/10/1999"),false,2);
            c.mFamiliares.add(new Contacto("Paco Jr",fecha.parse("24/11/2002"),false,0));
            c.mFamiliares.add(new Contacto("Lucia",fecha.parse("11/10/2002"),false,0));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Cómo envio mi contacto c a la segunda Actividad???

        Intent i=new Intent(this,MainActivity2.class);
        i.putExtra("obj1",c);
        startActivity(i);
    }
}