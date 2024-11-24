package com.example.parcelablesasignaturas;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Asignatura implements Parcelable{
    private String nombre;
    private double nota;

    public Asignatura(String nombre, double nota){
        this.nombre=nombre;
        this.nota=nota;
    }

    protected Asignatura(Parcel in) {
        nombre = in.readString();
        nota = in.readDouble();
    }

    public static final Creator<Asignatura> CREATOR = new Creator<Asignatura>() {
        @Override
        public Asignatura createFromParcel(Parcel in) {
            return new Asignatura(in);
        }

        @Override
        public Asignatura[] newArray(int size) {
            return new Asignatura[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public double getNota() {
        return nota;
    }

    @Override
    public String toString() {
        return "Asignatura{" +
                "nombre='" + nombre + '\'' +
                ", nota=" + nota +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeDouble(nota);
    }
}
