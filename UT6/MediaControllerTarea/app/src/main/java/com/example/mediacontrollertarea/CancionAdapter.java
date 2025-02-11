package com.example.mediacontrollertarea;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CancionAdapter extends RecyclerView.Adapter<CancionAdapter.ViewHolder> {
    private ArrayList<Cancion> canciones;

    public CancionAdapter(ArrayList<Cancion> lines) {
        this.canciones = lines;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtCancion;

        public ViewHolder(View itemView) {
            super(itemView);
            txtCancion = itemView.findViewById(R.id.txtCancion);
        }

        public TextView getPortada() {
            return txtCancion;
        }
    }

    @NonNull
    @Override
    public CancionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cancion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CancionAdapter.ViewHolder holder, int position) {
        holder.txtCancion.setText(canciones.get(position).getTitulo());
    }

    @Override
    public int getItemCount() {
        return canciones.size();
    }
}