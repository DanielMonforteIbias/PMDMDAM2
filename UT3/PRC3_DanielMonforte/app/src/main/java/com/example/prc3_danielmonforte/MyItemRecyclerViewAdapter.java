package com.example.prc3_danielmonforte;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

    private final List<BikesContent.Bike> lista; //Lista de bicis

    public MyItemRecyclerViewAdapter(List<BikesContent.Bike> items) {
        lista = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bicicleta = lista.get(position); //Obtenemos la bici en esa posicion
        holder.bicicletaImageView.setImageBitmap(holder.bicicleta.getPhoto()); //Ponemos la imagen de la bici en el ImageView
        holder.ciudadTextView.setText(holder.bicicleta.getCity()); //Ponemos la ciudad de la bici en el TextView de ciudad
        holder.propietarioTextView.setText(holder.bicicleta.getOwner()); //Ponemos el propietario de la bici en el TextView de propietario
        holder.direccionTextView.setText(holder.bicicleta.getLocation()); //Ponemos la ubicacion de la bici en el TextView de direccion
        holder.descripcionTextView.setText(holder.bicicleta.getDescription()); //Ponemos la descripcion de la bici en el TextView de direccion
        //Listener del ImageButton de enviar correo
        holder.correoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensajeReserva(holder.bicicleta,v.getContext()); //Enviamos un mensaje al propietario de esta bicicleta
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    } //Devolvemos el tamaño de la lista

    /**
     * Método que construye y lanza un Intent de tipo ACTION_SENDTO al propietario de una bicicleta con sus datos
     * @param bici la bicicleta que se va a reservar
     * @param c el contexto de la vista que llamó al método
     */
    public void enviarMensajeReserva(BikesContent.Bike bici, Context c){
        String correo=bici.getEmail(); //El receptor sera el correo de la bici
        String asunto="Reserva ShareMyBike"; //Se ha elegido que el asunto sea "Reserva ShareMyBike", pues no se ha especificado ninguno
        String mensaje="Dear Mr/Mrs "+bici.getOwner()+":\n\nI'd like to use your bike at "+bici.getLocation()+" ("+bici.getCity()+") for the following date: "+BikesContent.selectedDate+"\n\nCan you confirm its availability?\nKindest regards"; //Construimos el mensaje con los datos
        Intent intent = new Intent(Intent.ACTION_SENDTO); //Creamos un Intent de tipo ACTION_SENDTO
        String uriText = "mailto:" + Uri.encode(correo) + "?subject=" + Uri.encode(asunto) + "&body=" + Uri.encode(mensaje); //Crear el Uri con destinatario, asunto y mensaje. Al tener mailto solo dejara usar apps de correo
        intent.setData(Uri.parse((uriText))); //Poner los datos del uri en el intent
        c.startActivity(Intent.createChooser(intent, "Elige una aplicacion")); //Iniciar el intent (para ello tenemos que acceder al contexto de la vista que activó el callback)
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Variables de las vistas
        public final ImageView bicicletaImageView;
        public final TextView ciudadTextView;
        public final TextView propietarioTextView;
        public final TextView direccionTextView;
        public final TextView descripcionTextView;
        public final ImageButton correoImageButton;
        public BikesContent.Bike bicicleta; //Variable para el objeto Bike

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            //Inicializamos las vistas con el equivalente que hay en el binding
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