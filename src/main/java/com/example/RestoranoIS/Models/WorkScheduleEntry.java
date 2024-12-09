package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "DARBO_GRAFIKU_IRASAI")
public class WorkScheduleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "darbo_diena", nullable = false)
    private String darboDiena;

    @Column(name = "pradzios_laikas", nullable = false)
    private LocalDateTime pradziosLaikas;

    @Column(name = "pabaigos_laikas", nullable = false)
    private LocalDateTime pabaigosLaikas;

    @ManyToOne
    @JoinColumn(name = "fk_Darbo_grafikas", nullable = false)
    private WorkSchedule fkDarboGrafikas;

    public WorkScheduleEntry() {

    }

    public WorkScheduleEntry(String darboDiena, LocalDateTime pradziosLaikas, LocalDateTime pabaigosLaikas, WorkSchedule workSchedule) {
        this.darboDiena = darboDiena;
        this.pradziosLaikas = pradziosLaikas;
        this.pabaigosLaikas = pabaigosLaikas;
        this.fkDarboGrafikas = workSchedule;
    }
}
