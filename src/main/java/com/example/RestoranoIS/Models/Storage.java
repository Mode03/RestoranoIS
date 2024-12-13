package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "SANDELIAI")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sandelio_nr", nullable = false)
    private Integer sandelioNr;

    @Column(name = "adresas", nullable = false)
    private String adresas;

    @Column(name = "talpa", nullable = false)
    private int talpa;

    @ManyToOne
    @JoinColumn(name = "tipas")
    private StorageType type;

    @ManyToOne
    @JoinColumn(name = "fk_Darbuotojas")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "fk_Miestas")
    private City miestas;
    public Storage(){

    }

}
