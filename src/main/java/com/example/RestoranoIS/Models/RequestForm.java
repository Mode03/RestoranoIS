package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PRASYMU_FORMOS")
public class RequestForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pradzios_data", nullable = false)
    private LocalDate pradziosData;

    @Column(name = "pabaigos_data", nullable = false)
    private LocalDate pabaigosData;

    @Column(name = "priezastis", nullable = false, length = 255)
    private String priezastis;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "statusas", nullable = false)
    private Statusas statusas;

    @Column(name = "fk_Darbuotojas", nullable = false)
    private Integer fkDarbuotojas;

    @Column(name = "fk_Administratorius", nullable = false)
    private Integer fkAdministratorius;

    public RequestForm() {}
    public RequestForm(LocalDate pradziosData, LocalDate pabaigosData, String priezastis,
                       Statusas statusas, Integer fkDarbuotojas, Integer fkAdministratorius) {
        this.pradziosData = pradziosData;
        this.pabaigosData = pabaigosData;
        this.priezastis = priezastis;
        this.statusas = statusas;
        this.fkDarbuotojas = fkDarbuotojas;
        this.fkAdministratorius = fkAdministratorius;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getPradziosData() {
        return pradziosData;
    }

    public void setPradziosData(LocalDate pradziosData) {
        this.pradziosData = pradziosData;
    }

    public LocalDate getPabaigosData() {
        return pabaigosData;
    }

    public void setPabaigosData(LocalDate pabaigosData) {
        this.pabaigosData = pabaigosData;
    }

    public String getPriezastis() {
        return priezastis;
    }

    public void setPriezastis(String priezastis) {
        this.priezastis = priezastis;
    }

    public Statusas getStatusas() {
        return statusas;
    }

    public void setStatusas(Statusas statusas) {
        this.statusas = statusas;
    }

    public Integer getFkDarbuotojas() {
        return fkDarbuotojas;
    }

    public void setFkDarbuotojas(Integer fkDarbuotojas) {
        this.fkDarbuotojas = fkDarbuotojas;
    }

    public Integer getFkAdministratorius() {
        return fkAdministratorius;
    }

    public void setFkAdministratorius(Integer fkAdministratorius) {
        this.fkAdministratorius = fkAdministratorius;
    }


}
