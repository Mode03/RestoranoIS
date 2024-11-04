package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.Worker;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;


@Controller
public class PersonnelController {
    ArrayList<Worker> workersTest = new ArrayList<>();
    int lastIndex = 0;

    @GetMapping("/personnel-list")
    public String showPersonnelList(Model model) {
        model.addAttribute("workers", workersTest);
        return "Personnel/personnel-list";
    }

    @GetMapping("/personnel-list/view/{id}")
    public String viewPersonnelInfo(@PathVariable int id, Model model){
//        model.addAttribute(getWorkerbyId(id));
        model.addAttribute(workersTest.get(getWorkerIndex(id)));
        return "Personnel/view-personnel";
    }
    @GetMapping("/personnel-list/edit/{id}")
    public String editPersonnelInfo(@PathVariable int id, Model model){
        model.addAttribute(workersTest.get(getWorkerIndex(id)));
        return "Personnel/edit-personnel";
    }

    @GetMapping("/personnel-list/edit/delete/confirm/{id}")
    public String confirmDeletePersonnel(@PathVariable int id, Model model){
        model.addAttribute(workersTest.get(getWorkerIndex(id)));
        return "Personnel/delete-personnel";
    }

    @GetMapping("/personnel-list/create")
    public String createPersonnel(){
        return "Personnel/create-personnel";
    }

    @PostMapping("/personnel-list/create/submit")
    public String submitCreatedPersonnel(@RequestParam("Name") String name,
                                         @RequestParam("LastName") String lastName,
                                         @RequestParam("Address") String address,
                                         Model model){

        //ArrayList<Worker> workers = getWorkers();
        Worker worker = new Worker(lastIndex, name, lastName, address);
        lastIndex++;
        workersTest.add(worker);
        model.addAttribute("workers", workersTest);
        return "Personnel/personnel-list";
    }

    @GetMapping("/personnel-list/edit/delete/{id}")
    public String deletePersonnel(@PathVariable int id, Model model){
        int index = getWorkerIndex(id);
        workersTest.remove(index);
        model.addAttribute("workers", workersTest);
        return "Personnel/personnel-list";
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
            return "Personnel/edit-personnel";
        }
        workersTest.set(getWorkerIndex(id), worker);
        model.addAttribute("worker", worker);
        return "Personnel/view-personnel";
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
