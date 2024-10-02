package edu.pmdm.radiobuttonsexample;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener, CheckBox.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        RadioGroup r=(RadioGroup) findViewById(R.id.radioGroup);
        r.setOnCheckedChangeListener(this);
        CheckBox c=(CheckBox)findViewById(R.id.cbFutbol);
        c.setOnCheckedChangeListener(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        TextView t=(TextView) findViewById(R.id.txtEquipos);
        if (i==R.id.rbTalavera) t.setText("El Talavera es un buen equipo");
        else if (i==R.id.rbAlcazar) t.setText("El Alcazar no está mal");
        else if (i==R.id.rbAlbacete) t.setText("Qué equipo es el Albacete?");
        else if (i==R.id.rbOtros) t.setText("Seguro que es un equipo popular...");
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        CheckBox cb=(CheckBox)findViewById(R.id.cbFutbol);
        if (isChecked) cb.setText("Te gusta el fútbol!");
        else cb.setText("No te gusta el fútbol???");
    }
}