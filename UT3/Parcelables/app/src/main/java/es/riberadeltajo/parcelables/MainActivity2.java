package es.riberadeltajo.parcelables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView t =findViewById(R.id.txtInfo);
        Intent i=getIntent();
        Contacto c=i.getParcelableExtra("obj1");
        t.setText(c.toString());

    }
}