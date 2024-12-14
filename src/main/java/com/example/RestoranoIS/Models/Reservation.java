package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Data
@Entity
@Table(name = "REZERVACIJOS")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer rezervacija;

    @Column(name = "pradzia", nullable = false)
    private LocalDateTime pradzia;

    @Column(name = "pabaiga", nullable = false)
    private LocalDateTime pabaiga;

    @Column(name = "zmoniu_kiekis", nullable = false)
    private Integer zmoniuKiekis;

    @Column(name = "pageidavimas")
    private String pageidavimas;

    @ManyToOne
    @JoinColumn(name = "fk_Klientas")
    private Client klientas;

    @ManyToOne
    @JoinColumn(name = "fk_Staliukas")
    private CustomerTable staliukas;

    public Reservation(){

    }

    public Reservation(Integer id, LocalDateTime pradzia,LocalDateTime pabaiga, Integer zmoniuKiekis,String pageidavimas,Client klientas,CustomerTable staliukas) {
        this.rezervacija = id;
        this.pradzia = pradzia;
        this.pabaiga = pabaiga;
        this.zmoniuKiekis = zmoniuKiekis;
        this.pageidavimas = pageidavimas;
        this.klientas = klientas;
        this.staliukas = staliukas;
    }
/*
    public Reservation() {

    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }
*/
}
