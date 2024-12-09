package com.example.RestoranoIS.Services;

import com.example.RestoranoIS.DTO.WorkScheduleDTO;
import com.example.RestoranoIS.Repositories.WorkScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkScheduleService {
    private final WorkScheduleRepository repository;

    public WorkScheduleService(WorkScheduleRepository repository) {
        this.repository = repository;
    }

    public List<WorkScheduleDTO> getWeekSchedule(Long workScheduleId) {
        return repository.findWeekScheduleByWorkScheduleId(workScheduleId);
    }
}