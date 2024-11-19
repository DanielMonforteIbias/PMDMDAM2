package edu.pmdm.ut2_pruebarecycleview;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    static ArrayList<Data> FARMACIAS = new ArrayList<Data>();

    static {
        FARMACIAS.add(new Data("Farmacia 1", "Calle Vadillo 11"));
        FARMACIAS.add(new Data("Farmacia 2", "Calle Corrochano 22"));
        FARMACIAS.add(new Data("Farmacia 3", "Calle Ceca Illán 33"));
        FARMACIAS.add(new Data("Farmacia 4", "Calle Alfonso 44"));
        FARMACIAS.add(new Data("Farmacia 5", "Calle Lara, 55"));
        FARMACIAS.add(new Data("Farmacia 6", "Calle Messi 66"));
        FARMACIAS.add(new Data("Farmacia 7", "Calle MBappe 99"));
        FARMACIAS.add(new Data("Farmacia 8", "Calle del huevo frito 109"));
        FARMACIAS.add(new Data("Farmacia 10", "Plaza del pan 66"));
    }
}

