package com.example.parcelablesasignaturas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AsignaturasAdapter extends RecyclerView.Adapter<AsignaturasAdapter.ViewHolder>{
    private ArrayList<Asignatura>asignaturas;
    public AsignaturasAdapter(ArrayList<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    // Clase ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreAsignatura;
        private final TextView notaAsignatura;

        public ViewHolder(View itemView) {
            super(itemView);
            nombreAsignatura = itemView.findViewById(R.id.txtNombreAsignatura);
            notaAsignatura=itemView.findViewById(R.id.txtNota);

        }
        public TextView getNombreAsignatura() { return nombreAsignatura; }
        public TextView getNotaAsignatura(){return notaAsignatura;}
    }

    @NonNull
    @Override
    public AsignaturasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crear una nueva vista (inflar el layout)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsignaturasAdapter.ViewHolder holder, int position) {
        // Reemplazar el contenido de la vista (invocado por el layout manager)
        holder.getNombreAsignatura().setText(asignaturas.get(position).getNombre());
        holder.getNotaAsignatura().setText(String.valueOf(asignaturas.get(position).getNota()));
    }

    @Override
    public int getItemCount() {
        return asignaturas.size();
    }
}
