package edu.pmdm.ut2_pruebarecycleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorData extends RecyclerView.Adapter<AdaptadorData.ViewHolder> {

    private ArrayList<Data> localDataSet;

    // Clase ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tituloView;
        private final TextView subtituloView;

        public ViewHolder(View itemView) {
            super(itemView);
            tituloView = itemView.findViewById(R.id.titulo);
            subtituloView = itemView.findViewById(R.id.subtitulo);

            // Evento de clic
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Toast.makeText(view.getContext(),
                            DataSource.FARMACIAS.get(position).getTitulo() + " clicado",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        public TextView getTituloView() { return tituloView; }

        public TextView getSubtituloView() { return subtituloView; }
    }

    // Constructor para el adaptador
    public AdaptadorData(ArrayList<Data> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Crear una nueva vista (inflar el layout)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Reemplazar el contenido de la vista (invocado por el layout manager)
        viewHolder.getTituloView().setText(localDataSet.get(position).getTitulo());
        viewHolder.getSubtituloView().setText(localDataSet.get(position).getSubtitulo());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

