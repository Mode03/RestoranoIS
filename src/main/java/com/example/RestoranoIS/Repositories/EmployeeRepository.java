package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByIdNaudotojas(Integer idNaudotojas);
}
