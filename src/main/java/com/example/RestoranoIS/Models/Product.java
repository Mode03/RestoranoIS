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
    private Integer id;

    @Column(name = "pavadinimas")
    private String pavadinimas;

    @Column(name = "kiekis")
    private int kiekis;

    @ManyToOne
    @JoinColumn(name = "kategorija")
    private ProductType type;
    public Product(){

    }
}
