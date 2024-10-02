package edu.pmdm.imageviews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean llamando=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton b=findViewById(R.id.imageButton2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Llamar(view);
            }
        });
    }


    public void Llamar(View v){
        TextView t=findViewById(R.id.textView);
        ImageButton b=findViewById(R.id.imageButton2);
        ImageView ww=findViewById(R.id.imageView);
        if(!llamando) {
            t.setText("llamando a Walter White...");
            ww.setImageResource(R.drawable.walterwhitesombrero);
            b.setImageResource(R.drawable.colgar);
            llamando=true;
        }
        else{
            t.setText("llamada terminada");
            ww.setImageResource(R.drawable.walterwhitecalvo);
            b.setImageResource(R.drawable.llamar);
            llamando=false;
        }
    }
}