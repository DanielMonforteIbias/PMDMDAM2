package com.example.prc6_danielmonforte;

public enum EnumTipos {
    TODOS(-1),
    AUDIO(0),
    VIDEO(1),
    STREAMING(2);

    int valor;

    EnumTipos(int valor) {
        this.valor = valor;
    }

}
