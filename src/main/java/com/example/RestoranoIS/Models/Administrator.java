package com.example.RestoranoIS.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "ADMINISTRATORIAI")
public class Administrator {
    @Id
    @Column(name = "id_Naudotojas", nullable = false)
    private Integer idNaudotojas;

    @Column(name = "slaptas_raktas", nullable = false)
    private String slaptasRaktas;

    public Administrator() {
    }

    public Administrator(Integer id, String specialKey) {
        this.idNaudotojas = id;
        this.slaptasRaktas = specialKey;
    }
}
