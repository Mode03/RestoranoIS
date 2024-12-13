package com.example.RestoranoIS.Models;


public enum Statusas {
    priimta(1),
    laukiama(2),
    atmesta(3),
    pabaigta(4);

    private final int id;

    Statusas(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
