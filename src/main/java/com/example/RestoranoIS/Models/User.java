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
@Table(name = "NAUDOTOJAI")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String vardas;
    private String pavarde;
    @Column(name = "gimimo_data")
    private LocalDate gimimoData;
    @Column(name = "el_pastas")
    //@Email(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*([A-Za-z]{2,})$")
    private String elPastas;
    private String slaptazodis;
    private String lytis;

    public User() {
        // Default constructor
    }

    public User(String vardas, String pavarde, LocalDate gimimo_data, String el_pastas, String slaptazodis, String lytis) {
        this.vardas = vardas;
        this.pavarde = pavarde;
        this.gimimoData = gimimo_data;
        this.elPastas = el_pastas;
        this.slaptazodis = slaptazodis;
        this.lytis = lytis;
    }
}
