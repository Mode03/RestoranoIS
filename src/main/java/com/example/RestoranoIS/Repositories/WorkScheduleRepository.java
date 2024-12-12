package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.DTO.WorkScheduleDTO;
import com.example.RestoranoIS.Models.WorkSchedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Integer> {


    @Query(value = """
        SELECT 
            g.id AS workScheduleId,
            g.savaites_pradzios_data AS weekStartDate,
            g.savaites_pabaigos_data AS weekEndDate,
            i.id AS scheduleEntryId,
            i.darbo_diena AS workDay,
            i.pradzios_laikas AS startTime,
            i.pabaigos_laikas AS endTime,
            d.fk_Darbuotojas AS employeeId,
            CONCAT(n.vardas, ' ', n.pavarde) AS employeeName
        FROM 
            DARBO_GRAFIKAI g
        JOIN 
            DARBO_GRAFIKU_IRASAI i ON g.id = i.fk_Darbo_grafikas
        JOIN 
            DARBUOTOJU_DARBO_GRAFIKU_IRASAI d ON i.id = d.fk_Darbo_grafiko_irasas
        JOIN 
            DARBUOTOJAI da ON d.fk_Darbuotojas = da.id_Naudotojas
        JOIN 
            NAUDOTOJAI n ON da.id_Naudotojas = n.id
        WHERE 
            g.id = :workScheduleId
        ORDER BY 
            i.pradzios_laikas, i.pabaigos_laikas, i.darbo_diena
        """, nativeQuery = true)
    List<WorkScheduleDTO> findWeekScheduleByWorkScheduleId(@Param("workScheduleId") Long workScheduleId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM DARBUOTOJU_DARBO_GRAFIKU_IRASAI WHERE fk_Darbuotojas = :employeeId AND fk_darbo_grafiko_irasas = :entryId;", nativeQuery = true)
    void deleteWorkScheduleEntry(@Param("employeeId") long employeeId, @Param("entryId") long entryId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO DARBUOTOJU_DARBO_GRAFIKU_IRASAI VALUES (:employeeId,:entryId)", nativeQuery = true)
    void insertWorkScheduleEntry(@Param("employeeId") long employeeId, @Param("entryId") long entryId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE darbo_grafiku_irasai SET pradzios_laikas = :startTime, pabaigos_laikas = :endTime WHERE id = :entryId", nativeQuery = true)
    void updateWorkScheduleTime(@Param("entryId") Long entryId,
                                @Param("startTime") LocalDateTime startTime,
                                @Param("endTime") LocalDateTime endTime);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM DARBO_GRAFIKU_IRASAI WHERE fk_Darbo_grafikas = :weekId", nativeQuery = true)
    void deleteWorkScheduleEntries(@Param("weekId") int weekId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM DARBO_GRAFIKAI WHERE id = :weekId", nativeQuery = true)
    void deleteWorkSchedule(@Param("weekId") int weekId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM DARBUOTOJU_DARBO_GRAFIKU_IRASAI WHERE fk_Darbo_grafiko_irasas = :weekId", nativeQuery = true)
    void deleteEmployeeWorkScheduleEntries(@Param("weekId") int weekId);

}
