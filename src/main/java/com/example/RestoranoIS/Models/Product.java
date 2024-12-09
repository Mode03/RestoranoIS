package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "MAISTO_PRODUKTAI")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "pavadinimas", nullable = false)
    private String pavadinimas;

    @Column(name = "kiekis", nullable = false)
    private Integer kiekis;

    @Enumerated(EnumType.STRING) // Automatically maps string values to the enum
    @Column(name = "kategorija", nullable = false)
    private FoodType kategorija;

    public Product() {
    }

    public Product(Integer id, String pavadinimas, Integer kiekis, FoodType kategorija) {
        this.id = id;
        this.pavadinimas = pavadinimas;
        this.kiekis = kiekis;
        this.kategorija = kategorija;
    }
}
