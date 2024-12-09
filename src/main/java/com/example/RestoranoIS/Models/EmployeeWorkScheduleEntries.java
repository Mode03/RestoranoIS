package com.example.RestoranoIS.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "DARBUOTOJU_DARBO_GRAFIKU_IRASAI")
@IdClass(EmployeeWorkScheduleEntriesId.class)
public class EmployeeWorkScheduleEntries {

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_Darbuotojas", nullable = false)
    private Employee employee;

    @Id
    @ManyToOne
    @JoinColumn(name = "fk_Darbo_grafiko_irasas", nullable = false)
    private WorkScheduleEntry darboGrafikoIrasas;



    public EmployeeWorkScheduleEntries() {
    }

    public EmployeeWorkScheduleEntries(Employee employee, WorkScheduleEntry darboGrafikoIrasas) {
        this.employee = employee;
        this.darboGrafikoIrasas = darboGrafikoIrasas;
    }
}
