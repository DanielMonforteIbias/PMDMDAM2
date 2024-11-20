package com.example.fragmentosbiblioteca;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookDetailFragment extends Fragment {
    private TextView textViewTitulo;
    private TextView textViewAutor;
    private TextView textViewAño;
    private TextView textViewDescripcion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_book_detail, container, false);
        Book libro =(Book)getArguments().getSerializable("Libro"); //Obtenemos el libro que se ha pasado en el Bundle al crear el fragmento
        //Inicializamos las vistas
        textViewTitulo = vista.findViewById(R.id.txtTituloDetalles);
        textViewAutor = vista.findViewById(R.id.txtAutorDetalles);
        textViewAño=vista.findViewById(R.id.txtAñoDetalles);
        textViewDescripcion=vista.findViewById(R.id.txtDescripcionDetalles);

        //Ponemos la información del libro en los TextView
        textViewTitulo.setText(libro.getTitulo());
        textViewAutor.setText(libro.getAutor());
        textViewAño.setText(libro.getAño()+"");
        textViewDescripcion.setText(libro.getDescripcion());
        return vista;
    }
}