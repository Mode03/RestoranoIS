package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.Worker;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class PersonnelController {
    @GetMapping("/personnel-list")
    public String showPersonnelList(Model model) {
        List<Worker> workers = new ArrayList<>();
        model.addAttribute("workers", getWorkers());
        return "personnel-list";
    }

    @GetMapping("/personnel-list/view/{id}")
    public String viewPersonnelInfo(@PathVariable int id, Model model){
        model.addAttribute(getWorkerbyId(id));
        return "view-personnel";
    }
    @GetMapping("/personnel-list/edit/{id}")
    public String editPersonnelInfo(@PathVariable int id, Model model){
        model.addAttribute(getWorkerbyId(id));
        return "edit-personnel";
    }
    @GetMapping("personnel-list/create")
    public String createPersonnel(){
        return "create-personnel";
    }



    @PostMapping("/personnel-list/edit/save/{id}")
    public String savePersonnelInfo(@PathVariable int id,
                                    @RequestParam("Tab_nr") Integer tabNr,
                                    @RequestParam("Name") String name,
                                    @RequestParam("LastName") String lastName,
                                    @RequestParam("Address") String address,
                                    Model model){
        Worker worker = new Worker(tabNr, name, lastName, address);
        if (tabNr == null || name.isEmpty() || lastName.isEmpty() || address.isEmpty()) {
            model.addAttribute("error", "Reikia užpildyti visus laukus.");
            model.addAttribute(worker);
            return "edit-personnel";
        }

        model.addAttribute("worker", worker);
        return "view-personnel";
    }


    public ArrayList<Worker> getWorkers(){
        ArrayList<Worker> workers = new ArrayList<>();
        workers.add(new Worker(0, "John", "Doe", "123 Maple St"));
        workers.add(new Worker(1, "Jane", "Smith", "456 Oak St"));
        workers.add(new Worker(2, "Alice", "Johnson", "789 Pine St"));
        workers.add(new Worker(3, "Bob", "Brown", "321 Elm St"));
        return workers;
    }
    public Worker getWorkerbyId(int workerId) {
        return getWorkers().get(workerId);
    }

}
