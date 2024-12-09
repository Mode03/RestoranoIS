package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.City;
import com.example.RestoranoIS.Models.User;
import com.example.RestoranoIS.Models.Worker;
import com.example.RestoranoIS.Models.Employee;
import com.example.RestoranoIS.Repositories.CityRepository;
import com.example.RestoranoIS.Repositories.UserRepository;
import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
//JPA REQUIRED
import com.example.RestoranoIS.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
//JPA REQUIRED
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

//TODO: TURBUT REIKS ECNRYPTINTI SLAPTAZODI? KAI SUKURIU VARTOTOJA

@Controller
public class PersonnelController {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CityRepository cityRepository;
    ArrayList<Worker> workersTest;
    int lastIndex = 0;
    @GetMapping("/personnel-list")
    public String showPersonnelList(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees",employees);
        return "Personnel/personnel-list";
    }

    @GetMapping("/personnel-list/view/{id}")
    public String viewPersonnelInfo(@PathVariable int id, Model model){
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            model.addAttribute("employee", optionalEmployee.get());
        } else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
        return "Personnel/view-personnel";
    }
    @GetMapping("/personnel-list/edit/{id}")
    public String editPersonnelInfo(@PathVariable int id, Model model){
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            model.addAttribute("employee", optionalEmployee.get());
        } else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
        return "Personnel/edit-personnel";
    }

    @GetMapping("/personnel-list/edit/delete/confirm/{id}")
    public String confirmDeletePersonnel(@PathVariable int id, Model model){
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            model.addAttribute("employee", optionalEmployee.get());
        } else {
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }
        return "Personnel/delete-personnel";
    }

    @GetMapping("/personnel-list/create")
    public String createPersonnel(Model model){
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
    public String submitCreatedPersonnel(@ModelAttribute("employee") Employee employee, Model model){
        employee.setNuoKadaDirba(LocalDate.now());
        User user = employee.getUser();
        userRepository.save(user);
        userRepository.flush();

        employeeRepository.insertEmployee(employee.getAdresas(), employee.getAlga(), employee.getAsmensKodas(),
                employee.getAtostoguDienos(), employee.getMiestas().getId(), employee.getNuoKadaDirba(),
                employee.getPareigos(), employee.getTelefonas(), employee.getUser().getId());

        //employee.setUser(user);
        //employee.setIdNaudotojas(user.getId());
        //employeeRepository.save(employee);
        return "redirect:/personnel-list";
    }

    @GetMapping("/personnel-list/edit/delete/{id}")
    public String deletePersonnel(@PathVariable int id){
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            // Delete the employee
            employeeRepository.deleteById(id);
        }
        return "redirect:/personnel-list";
    }

    @PostMapping("/personnel-list/edit/save/{id}")
    public String savePersonnelInfo(@ModelAttribute("employee") Employee employee, Model model, @PathVariable int id){
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
                existingUser.setSlaptazodis(employeeUser.getSlaptazodis());

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
    public String salaryPage(Model model){
        model.addAttribute("workers", workersTest);
        return "Personnel/personnel-salary";
    }

    @PostMapping("/personnel-list/salary/calculate")
    public String getOrderForm(@RequestParam("selected") String selected, Model model){
        //System.out.println(selected);
        //String[] selectedArray = selected.split(",");
        model.addAttribute("info", selected);
        model.addAttribute("workers", workersTest);

        return "Personnel/personnel-salary";
    }

    @GetMapping("/personnel-list/populate")
    public String populateData(Model model){
        workersTest.add(new Worker(lastIndex, "John", "Doe", "123 Maple St"));
        lastIndex++;
        workersTest.add(new Worker(lastIndex, "Jane", "Smith", "456 Oak St"));
        lastIndex++;
        workersTest.add(new Worker(lastIndex, "Alice", "Johnson", "789 Pine St"));
        lastIndex++;
        workersTest.add(new Worker(lastIndex, "Bob", "Brown", "321 Elm St"));
        lastIndex++;
        model.addAttribute("workers", workersTest);
        return "Personnel/personnel-list";
    }

    public int getWorkerIndex(int workerId){
        for(int i = 0; i < workersTest.size(); i++){
            if(workersTest.get(i).getTab_nr() == workerId){
                return i;
            }
        }
        return -1;
    }

}
