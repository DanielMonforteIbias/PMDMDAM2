package edu.pmdm.ut2_pruebarecycleview;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] elementos = {"Toledo", "Ciudad Real", "Cuenca", "Guadalajara", "Albacete"};
    ArrayAdapter<String> adaptador;
    Spinner sp;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        sp = (Spinner) findViewById(R.id.spinner);
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, elementos);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adaptador);
        sp.setOnItemSelectedListener(this);

        // RecyclerView (lista)
        RecyclerView lista = findViewById(R.id.RecycleList);
        AdaptadorData adaptadorRecycler = new AdaptadorData(DataSource.FARMACIAS);
        lista.setAdapter(adaptadorRecycler);

        // LinearLayoutManager para disposición de la lista en una sola columna (vertical)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // Alternativa: GridLayoutManager para disposición en varias columnas (grid de 2 columnas)
        // GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        // Alternativa: StaggeredGridLayoutManager para disposición en 2 columnas con alturas variables
        // StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        lista.setLayoutManager(layoutManager);
    }

    // Callback cuando se selecciona un elemento del Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sp.getItemAtPosition(position);
        tv = findViewById(R.id.tvSeleccion);
        tv.setText(elementos[position].toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Callback cuando no se ha seleccionado ningún elemento del Spinner
    }
}
