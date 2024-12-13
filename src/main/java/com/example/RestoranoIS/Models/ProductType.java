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
@Table(name = "MAISTO_TIPAI")
public class ProductType {
    @Id
    private Integer id;

    private String name;
    public ProductType(){

    }
}