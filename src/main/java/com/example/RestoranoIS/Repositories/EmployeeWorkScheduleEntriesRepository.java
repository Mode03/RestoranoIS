package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.EmployeeWorkScheduleEntries;
import com.example.RestoranoIS.Models.EmployeeWorkScheduleEntriesId;
import com.example.RestoranoIS.Models.WorkSchedule;
import com.example.RestoranoIS.Models.WorkScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeWorkScheduleEntriesRepository extends JpaRepository<EmployeeWorkScheduleEntries, EmployeeWorkScheduleEntriesId> {


}