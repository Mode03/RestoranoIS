package com.example.RestoranoIS.Models;

import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class EmployeeWorkScheduleEntriesId implements Serializable {

    private Integer employee; // This corresponds to Employee's primary key type (assumed Integer)
    private Integer darboGrafikoIrasas; // This corresponds to WorkScheduleEntry's primary key type (assumed Integer)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeWorkScheduleEntriesId that = (EmployeeWorkScheduleEntriesId) o;
        return Objects.equals(employee, that.darboGrafikoIrasas) && Objects.equals(darboGrafikoIrasas, that.darboGrafikoIrasas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, darboGrafikoIrasas);
    }
}
