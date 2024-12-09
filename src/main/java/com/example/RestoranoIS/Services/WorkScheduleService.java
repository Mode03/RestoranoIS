package com.example.RestoranoIS.Services;

import com.example.RestoranoIS.DTO.WorkScheduleDTO;
import com.example.RestoranoIS.Models.Employee;
import com.example.RestoranoIS.Models.EmployeeWorkScheduleEntries;
import com.example.RestoranoIS.Models.WorkSchedule;
import com.example.RestoranoIS.Models.WorkScheduleEntry;
import com.example.RestoranoIS.Repositories.EmployeeRepository;
import com.example.RestoranoIS.Repositories.EmployeeWorkScheduleEntriesRepository;
import com.example.RestoranoIS.Repositories.WorkScheduleEntryRepository;
import com.example.RestoranoIS.Repositories.WorkScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

@Service
public class WorkScheduleService {
    private final WorkScheduleRepository repository;
    @Autowired
    private WorkScheduleRepository workScheduleRepository;
    @Autowired
    private WorkScheduleEntryRepository workScheduleEntryRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeWorkScheduleEntriesRepository employeeWorkScheduleEntriesRepository;


    public WorkScheduleService(WorkScheduleRepository repository, EmployeeRepository employeeRepository, EmployeeWorkScheduleEntriesRepository employeeWorkScheduleEntriesRepository, WorkScheduleRepository workScheduleRepository, WorkScheduleEntryRepository workScheduleEntryRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        this.employeeWorkScheduleEntriesRepository = employeeWorkScheduleEntriesRepository;
        this.workScheduleRepository = workScheduleRepository;
        this.workScheduleEntryRepository = workScheduleEntryRepository;

    }

    public List<WorkScheduleDTO> getWeekSchedule(Long workScheduleId) {
        return repository.findWeekScheduleByWorkScheduleId(workScheduleId);
    }

    public void generateNextWeekSchedule(){
        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate nextFriday = nextMonday.plusDays(4);

        Date monday = Date.valueOf(nextMonday);
        Date friday = Date.valueOf(nextFriday);


        WorkSchedule workSchedule = new WorkSchedule();
        workSchedule.setSavaitesPradziosData(monday);
        workSchedule.setSavaitesPabaigosData(friday);
        workScheduleRepository.save(workSchedule);

        List<Employee> employees = employeeRepository.findAll();
        int employeeCount = employees.size();
        int employeeIndex = 0;

        for (LocalDate date = nextMonday; !date.isAfter(nextFriday); date = date.plusDays(1)) {
            // Create shifts (8am to 8pm)
            for (int hour = 8; hour < 20; hour+=6) {
                WorkScheduleEntry irasas = new WorkScheduleEntry();
                irasas.setDarboDiena(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
                irasas.setPradziosLaikas(LocalDateTime.of(date, LocalTime.of(hour, 0)));
                irasas.setPabaigosLaikas(LocalDateTime.of(date, LocalTime.of(hour + 6, 0)));
                irasas.setFkDarboGrafikas(workSchedule);
                workScheduleEntryRepository.save(irasas);

                // Assign a worker to the shift
                EmployeeWorkScheduleEntries darbuotojoIrasas = new EmployeeWorkScheduleEntries();
                darbuotojoIrasas.setEmployee(employees.get(employeeIndex));
                darbuotojoIrasas.setDarboGrafikoIrasas((irasas));
                employeeWorkScheduleEntriesRepository.save(darbuotojoIrasas);

                // Rotate worker assignment
                employeeIndex = (employeeIndex + 1) % employeeCount;
            }
        }

    }

}