package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "PATIEKALAI")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pavadinimas")
    private String pavadinimas;

    private String aprasymas;

    private double kaina;

    private int kalorijos;

    @Column(name = "paruosimo_laikas")
    private int paruosimoLaikas;

    @ManyToOne
    @JoinColumn(name = "kategorija")
    private DishCategory category;

    public Dish() {
    }
}
