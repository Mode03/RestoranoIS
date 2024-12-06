package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "MIESTAI")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pavadinimas", nullable = false)
    private String pavadinimas;
}
