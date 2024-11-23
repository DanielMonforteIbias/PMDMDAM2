package com.example.prc3_danielmonforte;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prc3_danielmonforte.bikes.BikesContent;
import com.example.prc3_danielmonforte.databinding.FragmentItemBinding;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<BikesContent.Bike> lista;

    public MyItemRecyclerViewAdapter(List<BikesContent.Bike> items) {
        lista = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bicicleta = lista.get(position);
        holder.bicicletaImageView.setImageBitmap(holder.bicicleta.getPhoto());
        holder.ciudadTextView.setText(holder.bicicleta.getCity());
        holder.propietarioTextView.setText(holder.bicicleta.getOwner());
        holder.direccionTextView.setText(holder.bicicleta.getLocation());
        holder.descripcionTextView.setText(holder.bicicleta.getDescription());
        holder.correoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikesContent.Bike bici=holder.bicicleta;
                String correo=bici.getEmail();
                String asunto="Reserva ShareMyBike";
                String mensaje="Dear Mr/Mrs "+bici.getOwner()+":\n\nI'd like to use your bike at "+bici.getLocation()+" ("+bici.getCity()+") for the following date: "+BikeActivity.fecha+"\n\nCan you confirm its availability?\nKindest regards"; //Construimos el mensaje con los datos
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode(correo) + "?subject=" + Uri.encode(asunto) + "&body=" + Uri.encode(mensaje); //Crear el Uri con destinatario, asunto y mensaje. Al tener mailto solo dejara usar apps de correo
                intent.setData(Uri.parse((uriText))); //Poner los datos del uri en el intent
                v.getContext().startActivity(Intent.createChooser(intent, "Elige una aplicacion")); //Iniciar el intent (para ello tenemos que acceder al contexto de la vista que activó el callback)
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView bicicletaImageView;
        public final TextView ciudadTextView;
        public final TextView propietarioTextView;
        public final TextView direccionTextView;
        public final TextView descripcionTextView;
        public final ImageButton correoImageButton;
        public BikesContent.Bike bicicleta;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            bicicletaImageView=binding.imageViewBicicleta;
            ciudadTextView = binding.txtCiudadItem;
            propietarioTextView = binding.txtPropietarioItem;
            direccionTextView=binding.txtDireccionItem;
            descripcionTextView=binding.txtDescripcionItem;
            correoImageButton=binding.imageButtonCorreoItem;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + ciudadTextView.getText() + "'";
        }
    }
}