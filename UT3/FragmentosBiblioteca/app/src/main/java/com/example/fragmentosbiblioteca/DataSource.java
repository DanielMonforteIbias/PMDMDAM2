package com.example.fragmentosbiblioteca;

import java.util.ArrayList;

public class DataSource {
    public static ArrayList<Book>LIBROS = new ArrayList<Book>();

    public static void añadirLibrosPrueba(){
        LIBROS.add(new Book("El Quijote","Miguel de Cervantes",1605,"Aventuras de un Caballero y su Escudero"));
        LIBROS.add(new Book("Cien años de soledad","Gabriel García Márquez",1967,"Crónica de la vida de la familia Buendía"));
        LIBROS.add(new Book("1984","George Orwell",1949,"Novela política de ficción distópica"));
    }
}
