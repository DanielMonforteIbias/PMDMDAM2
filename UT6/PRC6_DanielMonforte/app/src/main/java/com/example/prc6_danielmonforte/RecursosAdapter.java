package com.example.prc6_danielmonforte;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecursosAdapter extends RecyclerView.Adapter<RecursosAdapter.ViewHolder>{
    private ArrayList<Recurso> recursos;

    public static AudioPlayer audioPlayer;

    public RecursosAdapter(ArrayList<Recurso> recursos, Context context) {
        this.recursos = recursos;
        audioPlayer=new AudioPlayer(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPortada;
        private final TextView txtNombre;
        private final TextView txtDescripcion;
        private final ImageView imgTipo;
        private final ImageButton imgBtnPlay;
        public ViewHolder(View itemView) {
            super(itemView);
            imgPortada=itemView.findViewById(R.id.imgPortada);
            txtNombre=itemView.findViewById(R.id.txtNombre);
            txtDescripcion=itemView.findViewById(R.id.txtDescripcion);
            imgTipo=itemView.findViewById(R.id.imgTipo);
            imgBtnPlay=itemView.findViewById(R.id.imgBtnPlay);
        }
        public ImageView getImgPortada() {return imgPortada;}
        public TextView getTxtNombre() {return txtNombre;}
        public TextView getTxtDescripcion() {return txtDescripcion;}
        public ImageView getImgTipo() {return imgTipo;}
        public ImageButton getImgBtnPlay() {return imgBtnPlay;}
    }
    @NonNull
    @Override
    public RecursosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecursosAdapter.ViewHolder holder, int position) {
        Recurso recurso=recursos.get(position);
        holder.imgPortada.setImageBitmap(recurso.getImagen());
        holder.txtNombre.setText(recurso.getNombre());
        holder.txtDescripcion.setText(recurso.getDescripcion());
        int imgTipoId=obtenerImagenTipo(recurso.getTipo());
        if(imgTipoId!=-1) holder.imgTipo.setImageResource(imgTipoId);
        holder.imgBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=v.getContext();
                if(recurso.getTipo()==EnumTipos.AUDIO.valor){
                    audioPlayer.playAudio(context,recurso.getUri());
                }
                else if(recurso.getTipo()==EnumTipos.VIDEO.valor || recurso.getTipo()==EnumTipos.STREAMING.valor){
                    Intent videoIntent=new Intent(context,VideoActivity.class);
                    videoIntent.putExtra("recurso",recurso);
                    context.startActivity(videoIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recursos.size();
    }

    public int obtenerImagenTipo(int tipo){
        switch(tipo){
            case 0:
                return R.drawable.audio;
            case 1:
                return R.drawable.video;
            case 2:
                return R.drawable.streaming;
            default:
                return -1;
        }
    }
}
