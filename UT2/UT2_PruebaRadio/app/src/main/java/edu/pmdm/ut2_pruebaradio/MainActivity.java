package edu.pmdm.ut2_pruebaradio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener, CheckBox.OnCheckedChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RadioGroup r=(RadioGroup)findViewById(R.id.radioGroup);
        r.setOnCheckedChangeListener(this);

        CheckBox miChk=(CheckBox)findViewById(R.id.chkFutbol);
        miChk.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        TextView t=(TextView)findViewById(R.id.txtEstado);
        if (checkedId == R.id.rbTalavera) { //Talavera
            t.setText("Buena elección!: El Talavera promete!!");
        } else if (checkedId == R.id.rbAlcazar) { //Alcazar
            t.setText("Gran equipo la gimnástica!!");
        } else if (checkedId == R.id.rbAlbacete) { //Albacete
            t.setText("El Albacete no es el mismo desde que se fué Iniesta");
        } else if (checkedId == R.id.rbOtros) { //Otros
            t.setText("El dinero no lo es todo....");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        TextView t=(TextView)findViewById(R.id.txtEstado);
        if(isChecked)
            t.setText("Te gusta el fútbol!!");
        else
            t.setText("No te gusta el fútbol?!??!!");
    }
}