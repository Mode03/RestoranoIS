package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "SANDELIU_MAISTO_PRODUKTAI")
public class StorageProducts {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_Maisto_produktas")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "fk_Sandelis")
    private Storage storage;
    public StorageProducts(){

    }
    public StorageProducts(Product product, Storage storage){
        this.product = product;
        this.storage = storage;
    }
}
