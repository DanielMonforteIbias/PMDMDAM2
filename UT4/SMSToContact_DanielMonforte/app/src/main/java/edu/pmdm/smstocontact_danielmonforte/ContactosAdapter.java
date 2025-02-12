package edu.pmdm.smstocontact_danielmonforte;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ViewHolder>{
    private final List<Contacto> contactos; //Lista de contactos

    public ContactosAdapter(List<Contacto> contactos) {
        this.contactos = contactos;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreCompleto;
        public ViewHolder(View itemView) {
            super(itemView);
            nombreCompleto = itemView.findViewById(R.id.txtNombreCompleto);
        }

        public TextView getNombreCompleto() {
            return nombreCompleto;
        }
    }

    @NonNull
    @Override
    public ContactosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacto_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactosAdapter.ViewHolder holder, int position) {
        holder.getNombreCompleto().setText(contactos.get(position).getNombre());
        holder.getNombreCompleto().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.binding.editTextMensaje.setVisibility(View.VISIBLE);
                MainActivity.binding.editTextMensaje.setText(""); //Vaciamos el mensaje al elegir otro contacto
                MainActivity.binding.editTextMensaje.setHint("Mensaje para "+contactos.get(holder.getAdapterPosition()).getNombre());
                MainActivity.binding.btnEnviar.setVisibility(View.VISIBLE);
                MainActivity.binding.txtCaracteresRestantes.setVisibility(View.VISIBLE);
                MainActivity.contactoSeleccionado=contactos.get(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }
}
