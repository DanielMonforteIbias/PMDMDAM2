package com.example.prc3_danielmonforte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prc3_danielmonforte.bikes.BikesContent;
import com.example.prc3_danielmonforte.databinding.FragmentSecondBinding;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding; //Variable para el binding de este fragmento
    ArrayList<String>opcionesFiltros=new ArrayList<String>(); //ArrayList para las opciones del Spinner de filtros
    private Fragment fragmentLista;
    private RecyclerView lista;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false); //Inicializamos la variable del binding
        return binding.getRoot();
    }

    /**
     * Método que crea las opcione del Spinner, tomando las ciudades de las bicis que existen en la lista y añadiendo una opcion "All"
     */
    public void crearFiltrosSpinner(){
        opcionesFiltros.add("All"); //Añadimos la opcion de "All"
        for(BikesContent.Bike b:BikesContent.ITEMS){ //Recorremos la lista de bicis
            if(!opcionesFiltros.contains(b.getCity())) opcionesFiltros.add(b.getCity()); //Si el nombre de la ciudad no es una opcion, lo añadimos
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,opcionesFiltros); //Creamos un adaptador para el spinner
        binding.spinnerFiltro.setAdapter(adaptador); //Asignamos el adaptador al Spinner
    }

    /**
     * Método que crea un onItemSelectedListener para el spinner de filtros y define lo que hace al seleccionar un ítem
     */
    public void crearListenerSpinner(){
        //Listener del spinner
        binding.spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //Cuando se selecciona un item
                String filtroCiudad=opcionesFiltros.get(position); //Obtenemos la opcion de filtrado en esa posicion
                fragmentLista=binding.fragmentContainerView.getFragment(); //Inicializamos el fragmento y el RecyclerView
                lista=fragmentLista.getView().findViewById(R.id.list);
                if(filtroCiudad.equals("All")){ //Si la opcion de filtrado es "All"
                    lista.setAdapter(new MyItemRecyclerViewAdapter(BikesContent.ITEMS)); //Ponemos la lista de todas las bicis en el RecyclerView
                }
                else{ //Si es alguna de las ciudades
                    filtrarLista(filtroCiudad); //Filtramos por esa ciudad
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    /**
     * Método que filtra la lista del RecyclerView por la ciudad recibida
     * @param filtroCiudad la opcion de filtrado a aplicar
     */
    public void filtrarLista(String filtroCiudad){
        ArrayList<BikesContent.Bike>listaFiltrada=new ArrayList<BikesContent.Bike>(); //Creamos un ArrayList para contener los objetos que vayamos a mostrar
        for(BikesContent.Bike b:BikesContent.ITEMS){ //Recorremos toda la lista de bicis
            if(b.getCity().equals(filtroCiudad)) listaFiltrada.add(b); //Si la ciudad coincide con la opcion de filtrado, la añadimos a la lista
        }
        lista.setAdapter(new MyItemRecyclerViewAdapter(listaFiltrada)); //Ponemos al RecyclerView el adaptador con la lista filtrada
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Listener del boton para volver al fragmento anterior
        binding.btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_FirstFragment); //Navegamos al primer fragment
            }
        });
        crearFiltrosSpinner(); //Creamos los filtros del Spinner
        crearListenerSpinner(); //Creamos el listener para el spinner
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}