package edu.pmdm.smstocontact_danielmonforte;

import android.graphics.Bitmap;

public class Contacto {
    private String id;
    private String nombre;
    private String telefono;
    private Bitmap foto;

    public Contacto(String id, String nombre, String telefono, Bitmap foto) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}
