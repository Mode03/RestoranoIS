package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name="SANDELIAI")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sandelio_nr", nullable = false)
    private Integer sandelioNr;

    @Column(name = "adresas", nullable = false)
    private String adresas;

    @Column(name = "talpa", nullable = false)
    private Integer talpa;

    @Column(name = "tipas", nullable = false)
    private Integer tipas;

    @ManyToOne
    @JoinColumn(name ="fk_Miestas")
    private City miestas;

    @ManyToOne
    @JoinColumn(name = "Darbuotojas")
    private Employee employee;

    public Storage(){

    }
    public Storage(Integer sandelioNr, String adresas, Integer talpa, Integer tipas, City miestas, Employee employee )
    {
        this.adresas = adresas;
        this.employee = employee;
        this.talpa = talpa;
        this.tipas = tipas;
        this.miestas = miestas;
        this.sandelioNr = sandelioNr;

    }

}
