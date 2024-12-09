package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@Data
@Entity
@Table(name = "DARBO_GRAFIKAI")
public class WorkSchedule {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id", nullable = false)
private Integer id;

@Column(name = "savaites_pradzios_data", nullable = false)
private Date savaitesPradziosData;

@Column(name = "savaites_pabaigos_data", nullable = false)
private Date savaitesPabaigosData;

    public WorkSchedule() {
        // Default constructor
    }



}
