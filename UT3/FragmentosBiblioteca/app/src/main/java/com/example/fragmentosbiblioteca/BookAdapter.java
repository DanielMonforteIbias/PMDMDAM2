package com.example.fragmentosbiblioteca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{
    private ArrayList<Book> libros;

    public BookAdapter(ArrayList<Book> libros) {
        this.libros = libros;
    }

    // Clase ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titulo;


        public ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTitulo);
        }
        public TextView getNombreAsignatura() { return titulo; }
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
        holder.getNombreAsignatura().setText(libros.get(position).getTitulo());
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }
}
