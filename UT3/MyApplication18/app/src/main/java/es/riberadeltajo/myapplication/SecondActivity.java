package es.riberadeltajo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView miLista;

    private String [] provincias= new String[]{"Ciudad Real","Toledo","Guadalajara",
            "Cuenca","Albacete","Talavera"}; //Datos
    private String [] descripciones= new String[]{"Qué gran ciudad para tapas","La ciudad imperial","Bonita ciudad de compras",
            "No te pierdas las casas colgantes","El nueva york de la mancha","Agua y sol"}; //Datos

    private int imagenes[]=new int[]{R.drawable.ciudadreal,R.drawable.toledo,R.drawable.guadalajara,
            R.drawable.cuenca,R.drawable.albacete,R.drawable.talavera};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<Ciudad> ciudades=new ArrayList<Ciudad>();
        for(int i=0;i< provincias.length;i++){
            Ciudad c=new Ciudad(provincias[i],descripciones[i],imagenes[i]);
            ciudades.add(c);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        miLista=findViewById(R.id.miLista); //IU -> se va a "inflar" una fila por cada ciudad a través de mi_fila_personalizada.xml


        MiAdaptadorPersonalizado adapter=
                new MiAdaptadorPersonalizado(this,R.layout.mi_fila_personalizada,ciudades);

        //enfuchar adaptador a la vista
        miLista.setAdapter(adapter);
        miLista.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent miIntent=new Intent();
        miIntent.putExtra("SELECCION",provincias[position]);
        setResult(RESULT_OK,miIntent);
        finish();
    }


    public class MiAdaptadorPersonalizado extends ArrayAdapter<Ciudad> {
        private int mResource;
        private ArrayList<Ciudad> misCiudades;

        public MiAdaptadorPersonalizado(@NonNull Context context, int resource, @NonNull List<Ciudad> objects) {
            super(context, resource, objects);
            mResource=resource;
            misCiudades=(ArrayList<Ciudad>) objects;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Este método es invocado tantas veces como "filas" se pinten en la actividad

            LayoutInflater miInflador= getLayoutInflater();
            View miFila=miInflador.inflate(mResource,parent,false);

            TextView txtDescripcion=miFila.findViewById(R.id.txtDescripcion);
            TextView txtCiudad=miFila.findViewById(R.id.txtCiudad);
            ImageView imgCiudad=miFila.findViewById(R.id.imgCiudad);

            txtDescripcion.setText(misCiudades.get(position).descripcion);
            txtCiudad.setText(misCiudades.get(position).nombre);
            imgCiudad.setImageResource(misCiudades.get(position).imagen);
            System.out.println("RDT: creada la fila"+position);
            return miFila;
        }

    }

    public class Ciudad{
        String nombre;
        String descripcion;
        int imagen;

        public Ciudad(String nombre, String descripcion, int imagen) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.imagen = imagen;
        }
    }
}
