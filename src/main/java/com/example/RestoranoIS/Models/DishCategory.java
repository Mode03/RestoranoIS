package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "PATIEKALU_KATEGORIJOS")
public class DishCategory {
    @Id
    private Integer id;

    private String name;

    public DishCategory() {
        // Default constructor
    }
}
