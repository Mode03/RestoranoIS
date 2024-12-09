package com.example.RestoranoIS.Models;

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
@Table(name = "MOKEJIMO_TIPAI")
public class PaymentType {

    @Id
    private Integer id;

    private String name;

    public PaymentType() {
        // Default constructor
    }
}

