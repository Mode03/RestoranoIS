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
@Table(name = "KLIENTAI")
public class Client {
    @Id
    @Column(name = "id_Naudotojas", nullable = false)
    private Integer idNaudotojas;

    @Column(name = "slapyvardis", nullable = false)
    private String slapyvardis;

    public Client() {
    }

    public Client(Integer id, String slapyvardis) {
        this.idNaudotojas = id;
        this.slapyvardis = slapyvardis;
    }
}
