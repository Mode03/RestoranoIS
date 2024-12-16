package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.DTO.WorkScheduleDTO;
import com.example.RestoranoIS.Models.*;
import com.example.RestoranoIS.Repositories.*;
import com.example.RestoranoIS.Services.UserService;
import com.example.RestoranoIS.Services.WorkScheduleService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class WorkScheduleController {

    @Autowired
    private WorkScheduleService scheduleService;

    private final UserRepository userRepository;
    private final UserService userService;
    private final WorkScheduleRepository workScheduleRepository;
    private final WorkScheduleEntryRepository workScheduleEntryRepository;
    private final EmployeeWorkScheduleEntriesRepository employeeWorkScheduleEntriesRepository;
    private final WorkScheduleService workScheduleService;
    private final EmployeeRepository employeeRepository;


    @Autowired
    public WorkScheduleController(UserRepository userRepository, UserService userService, WorkScheduleRepository workScheduleRepository, WorkScheduleEntryRepository workScheduleEntryRepository,EmployeeWorkScheduleEntriesRepository employeeWorkScheduleEntriesRepository, WorkScheduleService workScheduleService, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.workScheduleRepository = workScheduleRepository;
        this.workScheduleEntryRepository = workScheduleEntryRepository;
        this.employeeWorkScheduleEntriesRepository = employeeWorkScheduleEntriesRepository;
        this.workScheduleService = workScheduleService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/work-schedule")
    public String showWorkSchedulePage(Model model, HttpSession session) {
        List<WorkSchedule> workSchedules = workScheduleRepository.findAll();
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();
        boolean isEmployee = userService.isEmployee(loggedInUser.getId());
        boolean isAdmin = userService.isAdministrator(userId);
        model.addAttribute("isEmployee", isEmployee);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("workSchedules", workSchedules);
        return "WorkSchedule/work-schedule";
    }

    @GetMapping("/work-schedule/week/{id}")
    public String showDetailedSchedule(@PathVariable Long id, Model model, HttpSession session) {
        List<WorkScheduleDTO> scheduleDetails = workScheduleService.getWeekSchedule(id);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("id", id);
        model.addAttribute("scheduleDetails", scheduleDetails);
        return "WorkSchedule/detailed-schedule";
    }


    @GetMapping("/work-schedule/week/{id}/edit")
    public String editSchedule(@PathVariable Long id, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        List<WorkScheduleDTO> scheduleDetails = workScheduleService.getWeekSchedule(id);
        List<User> eUsers = userRepository.findAll();
        List<Employee> employees = employeeRepository.findAll();
        List<User> users = new ArrayList<>();
        for(int i = 0; i < eUsers.size(); i++) {
            for(int j = 0; j < employees.size(); j++) {
                if(eUsers.get(i).getId() == employees.get(j).getIdNaudotojas()){
                    users.add(eUsers.get(i));
                    break;
                }
            }
        }


        model.addAttribute("users",users);
        model.addAttribute("id", id);
        model.addAttribute("scheduleDetails", scheduleDetails);
        return "WorkSchedule/edit-schedule";
    }

    @PostMapping("/work-schedule/week/{weekNumber}/save")
    public String saveSchedule(
            @PathVariable String weekNumber,
            @RequestParam List<String> entryId,
            @RequestParam List<String> newId,
            @RequestParam List<String> employeeId,
            @RequestParam List<String> startTimeDate,
            @RequestParam List<String> startTimeTime,
            @RequestParam List<String> endTimeDate,
            @RequestParam List<String> endTimeTime) {

        Long id = Long.parseLong(weekNumber);

        for (int i = 0; i < entryId.size(); i++) {
            Long entryIdLong = Long.parseLong(entryId.get(i));
            int oldUser = Integer.parseInt(employeeId.get(i));
            int newUser = Integer.parseInt(newId.get(i));

            String startDateTimeString = startTimeDate.get(i) + "T" + startTimeTime.get(i);
            String endDateTimeString = endTimeDate.get(i) + "T" + endTimeTime.get(i);

            LocalDateTime startTime = LocalDateTime.parse(startDateTimeString);
            LocalDateTime endTime = LocalDateTime.parse(endDateTimeString);

            System.out.println("Entry ID: " + entryIdLong);
            System.out.println("Old User ID: " + oldUser);
            System.out.println("New User ID: " + newUser);
            System.out.println("Start Time: " + startTime);
            System.out.println("End Time: " + endTime);

            workScheduleRepository.deleteWorkScheduleEntry(oldUser, entryIdLong);
            workScheduleRepository.insertWorkScheduleEntry(newUser, entryIdLong);
            workScheduleRepository.updateWorkScheduleTime(entryIdLong,startTime,endTime);
        }

        return "redirect:/work-schedule/week/" + id;
    }



    @GetMapping("/work-schedule/week/{weekNumber}/remove")
    public String removeSchedule(@PathVariable int weekNumber,Model model,HttpSession session) {
        Optional<WorkSchedule> entry = workScheduleRepository.findById(weekNumber);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        if(entry.isEmpty()) throw new IllegalArgumentException("schedule not found: " + weekNumber);
        WorkSchedule schedule = entry.get();
        LocalDate endDate = schedule.getSavaitesPabaigosData().toLocalDate();
        LocalDate startDate = schedule.getSavaitesPradziosData().toLocalDate();
        System.out.println(LocalDate.now() + " " + startDate + " " +endDate);
        if (endDate.isAfter(LocalDate.now()) && startDate.isBefore(LocalDate.now())) {
            return "WorkSchedule/work-schedule-error";
        }

        List<WorkScheduleEntry> workScheduleEntries = workScheduleEntryRepository.findByFkDarboGrafikas_Id(weekNumber);

        workScheduleRepository.deleteWorkScheduleEntries(weekNumber);
        for(WorkScheduleEntry entr: workScheduleEntries) {
            workScheduleRepository.deleteEmployeeWorkScheduleEntries(entr.getId());
        }
        workScheduleRepository.deleteWorkSchedule(weekNumber);
        return "redirect:/work-schedule";
    }

    @GetMapping("/work-schedule/week/{weekNumber}/confirm-remove")
    public String confirmRemoveSchedule(@PathVariable int weekNumber, Model model) {
        model.addAttribute("weekNumber", weekNumber);
        return "WorkSchedule/confirm-remove";
    }
    @PostMapping("/work-schedule/week/generate")
    public String generateSchedule() {
        scheduleService.generateNextWeekSchedule();
        return "redirect:/work-schedule";
    }
}

