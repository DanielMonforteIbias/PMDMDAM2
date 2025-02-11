package com.example.mediacontrollertarea;

import android.net.Uri;

public class Cancion {
    private String titulo;
    private int id;
    private Uri uri;
    private boolean cancionDefault;
    public Cancion(String titulo, int id){
        this.titulo=titulo;
        this.id=id;
        this.cancionDefault=true;
    }
    public Cancion(String titulo, Uri uri){
        this.titulo=titulo;
        this.uri=uri;
        this.cancionDefault=false;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCancionDefault() {
        return cancionDefault;
    }

    public void setCancionDefault(boolean cancionDefault) {
        this.cancionDefault = cancionDefault;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
