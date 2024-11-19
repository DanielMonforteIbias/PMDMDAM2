package edu.pmdm.ut2_pruebarecycleview;

public class Data {

    //Atributos del objeto
    String titulo;
    String subtitulo;

    //Constructor
    public Data(String titulo, String subtitulo) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    @Override
    public String toString() {
        return "data{" +
                "titulo='" + titulo + '\'' +
                ", subtitulo='" + subtitulo + '\'' +
                '}';
    }

}
