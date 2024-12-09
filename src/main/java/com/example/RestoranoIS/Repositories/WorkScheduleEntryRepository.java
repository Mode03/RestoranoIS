package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.WorkScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkScheduleEntryRepository extends JpaRepository<WorkScheduleEntry, Integer> {
    List<WorkScheduleEntry> findByFkDarboGrafikas_Id(Integer id);


}
