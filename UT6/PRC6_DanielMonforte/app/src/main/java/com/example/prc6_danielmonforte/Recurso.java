package com.example.prc6_danielmonforte;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Recurso implements Parcelable {
    private String nombre;
    private String descripcion;
    private int tipo;
    private String uri;
    private Bitmap imagen;

    public Recurso(String nombre, String descripcion, int tipo, String uri, Bitmap imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.uri = uri;
        this.imagen = imagen;
    }

    protected Recurso(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        tipo = in.readInt();
        uri = in.readString();
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Recurso> CREATOR = new Creator<Recurso>() {
        @Override
        public Recurso createFromParcel(Parcel in) {
            return new Recurso(in);
        }

        @Override
        public Recurso[] newArray(int size) {
            return new Recurso[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeInt(tipo);
        dest.writeString(uri);
        dest.writeParcelable(imagen, flags);
    }
}
