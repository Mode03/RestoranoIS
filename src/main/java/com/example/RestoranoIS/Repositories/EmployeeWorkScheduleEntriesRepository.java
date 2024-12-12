package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.EmployeeWorkScheduleEntries;
import com.example.RestoranoIS.Models.EmployeeWorkScheduleEntriesId;
import com.example.RestoranoIS.Models.WorkSchedule;
import com.example.RestoranoIS.Models.WorkScheduleEntry;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeWorkScheduleEntriesRepository extends JpaRepository<EmployeeWorkScheduleEntries, EmployeeWorkScheduleEntriesId> {
    @Query(value = "SELECT * FROM DARBUOTOJU_DARBO_GRAFIKU_IRASAI WHERE fk_Darbuotojas=?",nativeQuery = true)
    public List<EmployeeWorkScheduleEntries> findByEmployee(Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM DARBUOTOJU_DARBO_GRAFIKU_IRASAI WHERE fk_Darbuotojas= :id",nativeQuery = true)
    void deleteByEmployee(@Param("id") int id);
}