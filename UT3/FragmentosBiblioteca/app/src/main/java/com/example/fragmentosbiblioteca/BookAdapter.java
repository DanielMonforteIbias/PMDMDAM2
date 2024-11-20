package com.example.fragmentosbiblioteca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{
    private ArrayList<Book> libros;
    private FragmentManager fragmentManager;

    public BookAdapter(ArrayList<Book> libros,FragmentManager fragmentManager) {
        this.libros = libros;
        this.fragmentManager=fragmentManager;
    }

    // Clase ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView titulo;
        public ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTitulo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion=getAdapterPosition();
                    if(posicion!=RecyclerView.NO_POSITION){
                        cambiarFragmentoDetallesLibro(libros.get(posicion));
                    }
                }
            });
        }
        public TextView getTitulo() { return titulo; }

        private void cambiarFragmentoDetallesLibro(Book libro) {
            BookDetailFragment detallesFragment = new BookDetailFragment();
            Bundle args = new Bundle();
            args.putSerializable("Libro", libro);
            detallesFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, detallesFragment).addToBackStack(null).commit();
        }
    }
    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crear una nueva vista (inflar el layout)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        // Reemplazar el contenido de la vista (invocado por el layout manager)
        holder.getTitulo().setText(libros.get(position).getTitulo());
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }
}
