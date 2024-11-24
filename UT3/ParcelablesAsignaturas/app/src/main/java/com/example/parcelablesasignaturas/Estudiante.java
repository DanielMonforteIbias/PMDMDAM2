package com.example.parcelablesasignaturas;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Estudiante implements Parcelable {
    private String nombre;
    private int edad;
    private double notaMedia;
    private ArrayList<Asignatura>asignaturas;

    public Estudiante(String nombre, int edad, double notaMedia, ArrayList<Asignatura> asignaturas) {
        this.nombre = nombre;
        this.edad = edad;
        this.notaMedia = notaMedia;
        this.asignaturas = asignaturas;
    }


    protected Estudiante(Parcel in) {
        nombre = in.readString();
        edad = in.readInt();
        notaMedia = in.readDouble();
        asignaturas = in.createTypedArrayList(Asignatura.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeInt(edad);
        dest.writeDouble(notaMedia);
        dest.writeTypedList(asignaturas);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Estudiante> CREATOR = new Creator<Estudiante>() {
        @Override
        public Estudiante createFromParcel(Parcel in) {
            return new Estudiante(in);
        }

        @Override
        public Estudiante[] newArray(int size) {
            return new Estudiante[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }


    public double getNotaMedia() {
        return notaMedia;
    }

    public ArrayList<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", notaMedia=" + notaMedia +
                ", asignaturas=" + asignaturas +
                '}';
    }
}
