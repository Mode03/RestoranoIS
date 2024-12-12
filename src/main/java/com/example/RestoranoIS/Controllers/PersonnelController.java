package com.example.RestoranoIS.Controllers;
import com.example.RestoranoIS.Models.*;
import com.example.RestoranoIS.Repositories.*;
import com.example.RestoranoIS.Services.OrderService;
import com.example.RestoranoIS.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Controller
public class PersonnelController {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AdministratorRepository administratorRepository;
    @Autowired private WorkScheduleRepository scheduleRepository;
    @Autowired private WorkScheduleEntryRepository entryRepository;
    @Autowired private EmployeeWorkScheduleEntriesRepository employeeWorkScheduleEntriesRepository;
    private static final String EMPLOYEE_KEY = "Darbuotojas123";
    private static final String ADMIN_KEY = "Administratorius123";
    @Autowired private CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService;
    @Autowired
    public PersonnelController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/personnel-list")
    public String showPersonnelList(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        if (!userService.isAdministrator(userId)) {
            return "redirect:/access-denied";
        }
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees",employees);
        return "Personnel/personnel-list";
    }

    @GetMapping("/personnel-list/view/{id}")
    public String viewPersonnelInfo(@PathVariable int id, Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        if (!userService.isAdministrator(userId)) {
            return "redirect:/access-denied";
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            model.addAttribute("employee", optionalEmployee.get());
        } else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
        return "Personnel/view-personnel";
    }
    @GetMapping("/personnel-list/edit/{id}")
    public String editPersonnelInfo(@PathVariable int id, Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        if (!userService.isAdministrator(userId)) {
            return "redirect:/access-denied";
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            model.addAttribute("employee", optionalEmployee.get());
        } else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
        return "Personnel/edit-personnel";
    }

    @GetMapping("/personnel-list/edit/delete/confirm/{id}")
    public String confirmDeletePersonnel(@PathVariable int id, Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        if (!userService.isAdministrator(userId)) {
            return "redirect:/access-denied";
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            model.addAttribute("employee", optionalEmployee.get());
        } else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
        return "Personnel/delete-personnel";
    }

    @GetMapping("/personnel-list/create")
    public String createPersonnel(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        if (!userService.isAdministrator(userId)) {
            return "redirect:/access-denied";
        }

        Employee employee = new Employee();

        User user = new User();
        employee.setUser(user);
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        model.addAttribute("employee", employee);
        model.addAttribute("user", user);
        return "Personnel/create-personnel";
    }

    @PostMapping("/personnel-list/create/submit")
    @Transactional
    public String submitCreatedPersonnel(@ModelAttribute("employee") Employee employee,
                                         @RequestParam("selected") String selected,
                                         Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        if (!userService.isAdministrator(userId)) {
            return "redirect:/access-denied";
        }



        employee.setNuoKadaDirba(LocalDate.now());
        employee.setAlga(0);
        employee.setAtostoguDienos(0);
        User user = employee.getUser();
        String encodedPassword = passwordEncoder.encode(user.getSlaptazodis());
        user.setSlaptazodis(encodedPassword);
        userRepository.save(user);
        employee.setUser(user);
        employee.setIdNaudotojas(user.getId());
        employeeRepository.save(employee);

        if(selected.equals("on")){
            Administrator administrator = new Administrator(employee.getIdNaudotojas(), ADMIN_KEY);
            administratorRepository.save(administrator);
        }
        return "redirect:/personnel-list";
    }


    @GetMapping("/personnel-list/edit/delete/{id}")
    public String deletePersonnel(@PathVariable int id, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        if (!userService.isAdministrator(userId)) {
            return "redirect:/access-denied";
        }

        Optional<Employee> employee = employeeRepository.findById(id);
        boolean isAdmin = userService.isAdministrator(id);
        if (employee.isPresent()) {
            List<EmployeeWorkScheduleEntries> workScheduleEntries = employeeWorkScheduleEntriesRepository.findByEmployee(id);
            employeeWorkScheduleEntriesRepository.deleteByEmployee(id);
            for(EmployeeWorkScheduleEntries entry: workScheduleEntries) {
                int entryId = entry.getDarboGrafikoIrasas().getId();
                entryRepository.deleteById(entryId);
            }
            if(isAdmin){
                administratorRepository.deleteById(id);
                administratorRepository.flush();
            }
            employeeRepository.deleteEmployeeById(id);
            userRepository.deleteUserById(id);
        }
        return "redirect:/personnel-list";
    }

    @PostMapping("/personnel-list/edit/save/{id}")
    public String savePersonnelInfo(@ModelAttribute("employee") Employee employee, Model model, HttpSession session, @PathVariable int id){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        if (!userService.isAdministrator(userId)) {
            return "redirect:/access-denied";
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();
                User employeeUser = employee.getUser();
                existingUser.setVardas(employeeUser.getVardas());
                existingUser.setPavarde(employeeUser.getPavarde());
                existingUser.setElPastas(employeeUser.getElPastas());
                existingUser.setLytis(employeeUser.getLytis());

                userRepository.save(existingUser);
            } else {
                throw new IllegalArgumentException("Employee not found with ID: " + id);
            }
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setAdresas(employee.getAdresas());
            existingEmployee.setAlga(employee.getAlga());
            existingEmployee.setTelefonas(employee.getTelefonas());
            existingEmployee.setAtostoguDienos(employee.getAtostoguDienos());
            employeeRepository.save(existingEmployee);
            model.addAttribute("employee", existingEmployee);
        } else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
        return "redirect:/personnel-list/view/{id}";
    }

    @GetMapping("/personnel-list/salary")
    public String salaryPage(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        if (!userService.isAdministrator(userId)) {
            return "redirect:/access-denied";
        }

        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees",employees);
        return "Personnel/personnel-salary";
    }

    @PostMapping("/personnel-list/salary/calculate")
    public String getOrderForm(@RequestParam("selected") String selected, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        if (!userService.isAdministrator(userId)) {
            return "redirect:/access-denied";
        }

        String[] split = selected.split(",");
        int baseSalary = 8;
        List<Employee> selectedEmployees = new ArrayList<>();
        for (String value: split) {
            int id = Integer.parseInt(value);
            Optional<Employee> current = employeeRepository.findById(id);
            if(current.isEmpty()) throw new IllegalArgumentException("Employee not found with ID: " + id);
            Employee existingEmployee = current.get();
            List<EmployeeWorkScheduleEntries> entries = employeeWorkScheduleEntriesRepository.findByEmployee(id);
            if(entries.isEmpty()){
                existingEmployee.setAlga(0);
                employeeRepository.save(existingEmployee);
                selectedEmployees.add(existingEmployee);
                continue;
            }
            int hoursWorked = 0;
            for (EmployeeWorkScheduleEntries entry: entries) {
                WorkScheduleEntry schedule = entry.getDarboGrafikoIrasas();
                int month = LocalDateTime.now().getMonthValue();
                int day = LocalDateTime.now().getDayOfMonth();
                LocalDateTime startTime = schedule.getPradziosLaikas();
                LocalDateTime endTime = schedule.getPabaigosLaikas();
                if(day < 10){
                 month -= 1;
                }
                if(startTime.getMonthValue() == month){
                    hoursWorked += (endTime.getHour() - startTime.getHour());
                }
            }
            int newSalary = hoursWorked * baseSalary;

            existingEmployee.setAlga(newSalary);
            employeeRepository.save(existingEmployee);
            selectedEmployees.add(existingEmployee);
        }
        model.addAttribute("employees",selectedEmployees);

        return "Personnel/personnel-salary-view";
    }

}
