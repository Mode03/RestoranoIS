package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
@Entity
@Table(name = "DARBUOTOJAI")
public class Employee {
    @Id
    @Column(name = "id_Naudotojas", nullable = false)
    private Integer idNaudotojas; // Foreign key į NAUDOTOJAI ID

    @Column(name = "asmens_kodas", nullable = false)
    private String asmensKodas;

    @Column(name = "pareigos", nullable = false)
    private String pareigos;

    @Column(name = "nuo_kada_dirba", nullable = false)
    private Date nuoKadaDirba;

    @Column(name = "alga", nullable = false)
    private double alga;

    @Column(name = "telefonas", nullable = false)
    private String telefonas;

    @Column(name = "adresas", nullable = false)
    private String adresas;

    @Column(name = "atostogu_dienos", nullable = false)
    private Integer atostoguDienos;

    @ManyToOne
    @JoinColumn(name = "fk_Miestas")
    private City miestas;

    public Employee() {
    }

    public Employee(Integer idNaudotojas, String asmensKodas, String pareigos, String telefonas, String adresas, City miestas) {
        this.idNaudotojas = idNaudotojas;
        this.asmensKodas = asmensKodas;
        this.pareigos = pareigos;
        this.nuoKadaDirba = new Date();
        this.alga = 0;
        this.telefonas = telefonas;
        this.adresas = adresas;
        this.atostoguDienos = 0;
        this.miestas = miestas;
    }
}
