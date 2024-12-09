package com.example.RestoranoIS.DTO;

import java.sql.Date;
import java.time.LocalDateTime;

public interface WorkScheduleDTO {
    Long getWorkScheduleId();
    Date getWeekStartDate();
    Date getWeekEndDate();
    Long getScheduleEntryId();
    String getWorkDay();
    LocalDateTime getStartTime(); // Changed to LocalDateTime
    LocalDateTime getEndTime();   // Changed to LocalDateTime
    Long getEmployeeId();
    String getEmployeeName();
}
