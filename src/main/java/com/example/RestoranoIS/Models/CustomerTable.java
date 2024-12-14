package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Data
@Entity
@Table(name = "STALIUKAI")
public class CustomerTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staliuko_nr", nullable = false)
    private Integer staliukas;


    @Column(name = "vietu_skaicius", nullable = false)
    private Integer vietuSkaicius;

    public CustomerTable(){

    }
    public CustomerTable (Integer staliukas, Integer vietuSkaicius){
        this.staliukas=staliukas;
        this.vietuSkaicius=vietuSkaicius;
    }


}
