package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "UZSAKYMU_PATIEKALAI")
public class OrderDish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int kiekis;

    @ManyToOne
    @JoinColumn(name = "fk_Patiekalas")
    private Dish dish; // Nuoroda į patiekalą

    @ManyToOne
    @JoinColumn(name = "fk_Uzsakymas")
    private Order order; // Nuoroda į užsakymą

    public OrderDish() {
    }

    public OrderDish(Integer id, int kiekis, Dish dish, Order order) {
        this.id = id;
        this.kiekis = kiekis;
        this.dish = dish;
        this.order = order;
    }
}
