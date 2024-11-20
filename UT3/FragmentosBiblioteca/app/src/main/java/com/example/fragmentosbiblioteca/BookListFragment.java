package com.example.fragmentosbiblioteca;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BookListFragment extends Fragment {

    private RecyclerView listaLibros;
    public BookAdapter adaptador;
    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_book_list, container, false);
        adaptador=new BookAdapter(DataSource.LIBROS,getParentFragmentManager());
        listaLibros=vista.findViewById(R.id.listaLibros);
        listaLibros.setLayoutManager(new LinearLayoutManager(getContext()));
        listaLibros.setAdapter(adaptador);

        return vista;
    }
}