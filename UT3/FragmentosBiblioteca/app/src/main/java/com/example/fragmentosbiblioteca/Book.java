package com.example.fragmentosbiblioteca;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Book implements Parcelable {
    private String titulo;
    private String autor;
    private int año;
    private String descripcion;

    public Book(String titulo, String autor, int año, String descripcion) {
        this.titulo = titulo;
        this.autor = autor;
        this.año = año;
        this.descripcion = descripcion;
    }

    protected Book(Parcel in) {
        titulo = in.readString();
        autor = in.readString();
        año = in.readInt();
        descripcion = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAño() {
        return año;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(autor);
        dest.writeInt(año);
        dest.writeString(descripcion);
    }
}
