package edu.pmdm.leerarchivo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LinesAdapter extends RecyclerView.Adapter<LinesAdapter.ViewHolder>{
    private ArrayList<String> lines;
    public LinesAdapter(ArrayList<String>lines){
        this.lines=lines;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtLine; //Cada item se representa por una imagen de su portada
        public ViewHolder(View itemView) {
            super(itemView);
            txtLine = itemView.findViewById(R.id.txtLine);
        }

        public TextView getPortada() {
            return txtLine;
        }
    }
    @NonNull
    @Override
    public LinesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinesAdapter.ViewHolder holder, int position) {
        holder.txtLine.setText(lines.get(position));
        holder.txtLine.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                lines.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }
}
