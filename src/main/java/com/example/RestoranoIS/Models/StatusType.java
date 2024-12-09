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
@Table(name = "STATUSO_TIPAI")
public class StatusType {

    @Id
    private Integer id;

    private String name;

    public StatusType() {
        // Default constructor
    }
}

