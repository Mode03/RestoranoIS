package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByIdNaudotojas(Integer idNaudotojas);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM DARBUOTOJAI WHERE id_Naudotojas= :id",nativeQuery = true)
    void deleteEmployeeById(@Param("id") int id);

    Employee findByIdNaudotojas(Integer idNaudotojas);
}
