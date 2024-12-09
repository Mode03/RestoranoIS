package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
@Entity
@Table(name = "UZSAKYMAI")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate data;

    @Column(name = "bendra_suma")
    private double bendraSuma;

    @ManyToOne
    @JoinColumn(name = "mokejimo_tipas")
    private PaymentType paymentType;

    @ManyToOne
    @JoinColumn(name = "uzsakymo_statusas")
    private StatusType statusType;

    @ManyToOne
    @JoinColumn(name = "tipas")
    private OrderType orderType;

    @ManyToOne
    @JoinColumn(name = "fk_Klientas")
    private Client client;

    public Order() {
        // Default constructor
    }

    public Order(LocalDate data, double bendraSuma, PaymentType paymentType, StatusType statusType, OrderType orderType, Client client) {
        this.data = data;
        this.bendraSuma = bendraSuma;
        this.paymentType = paymentType;
        this.statusType = statusType;
        this.orderType = orderType;
        this.client = client;
    }
}
