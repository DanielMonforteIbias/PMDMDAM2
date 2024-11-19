package com.example.fragmentosbiblioteca;

import java.util.ArrayList;

public class DataSource {
    public static ArrayList<Book>LIBROS = new ArrayList<Book>();

    public static void añadirLibrosPrueba(){
        LIBROS.add(new Book("Libro 1","Yo",2023,"Un libro de prueba"));
    }
}
