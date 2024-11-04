package com.example.RestoranoIS.Controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WorkScheduleController {

    @GetMapping("/work-schedule")
    public String showWorkSchedulePage() {
        return "WorkSchedule/work-schedule";
    }

    @GetMapping("/work-schedule/week/{weekNumber}")
    public String showDetailedSchedule(@PathVariable int weekNumber, Model model) {
        model.addAttribute("weekNumber", weekNumber);
        model.addAttribute("schedule", getScheduleForWeek(weekNumber));
        return "WorkSchedule/detailed-schedule";
    }

    @GetMapping("/work-schedule/week/{weekNumber}/edit")
    public String editSchedule(@PathVariable int weekNumber, Model model) {
        model.addAttribute("weekNumber", weekNumber);
        model.addAttribute("schedule", getScheduleForWeek(weekNumber));
        return "WorkSchedule/edit-schedule";
    }

    @PostMapping("/work-schedule/week/{weekNumber}/save")
    public String saveSchedule(@PathVariable int weekNumber, @RequestParam List<String> workerEntries) {
        saveScheduleForWeek(weekNumber, workerEntries);
        return "redirect:/work-schedule/week/" + weekNumber;
    }

    @GetMapping("/work-schedule/week/{weekNumber}/remove")
    public String removeSchedule(@PathVariable int weekNumber) {
        deleteScheduleForWeek(weekNumber);
        return "redirect:/work-schedule";
    }

    @GetMapping("/work-schedule/week/{weekNumber}/confirm-remove")
    public String confirmRemoveSchedule(@PathVariable int weekNumber, Model model) {
        model.addAttribute("weekNumber", weekNumber);
        return "WorkSchedule/confirm-remove";
    }

    private List<String> getScheduleForWeek(int weekNumber) {
        return List.of("Šarūnas - 9:00 AM - 5:00 PM", "Vitalka - 10:00 AM - 6:00 PM");
    }

    private void saveScheduleForWeek(int weekNumber, List<String> workerEntries) {

    }

    private void deleteScheduleForWeek(int weekNumber) {

    }
}

