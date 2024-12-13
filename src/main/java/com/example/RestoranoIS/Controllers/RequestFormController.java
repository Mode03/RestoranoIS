package com.example.RestoranoIS.Controllers;


import com.example.RestoranoIS.Models.Administrator;
import com.example.RestoranoIS.Models.RequestForm;
import com.example.RestoranoIS.Models.Statusas;
import com.example.RestoranoIS.Models.User;
import com.example.RestoranoIS.Repositories.AdministratorRepository;
import com.example.RestoranoIS.Repositories.RequestFormRepository;
import com.example.RestoranoIS.Services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class RequestFormController {

    @Autowired
    private EntityManager entityManager;
    private final UserService userService;
    private final AdministratorRepository administratorRepository;
    private final RequestFormRepository requestFormRepository;

    @Autowired
    RequestFormController(RequestFormRepository requestFormRepository, AdministratorRepository administratorRepository, UserService userService) {
        this.requestFormRepository = requestFormRepository;
        this.administratorRepository = administratorRepository;
        this.userService = userService;
    }

    @GetMapping("/day-request")
    public String showDayRequestPage(Model model, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        List<RequestForm> requestForms = requestFormRepository.findAllByFkDarbuotojas(loggedInUser.getId());
        model.addAttribute("requestForms", requestForms);
        return "WorkSchedule/day-request";
    }

    @PostMapping("/day-request/submit")
    public String submitDayRequest(HttpSession session,
            @RequestParam("pradziosData") LocalDate pradziosData,
            @RequestParam("pabaigosData") LocalDate pabaigosData,
            @RequestParam("priezastis") String priezastis) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        int darbuotojas = loggedInUser.getId();
        Statusas statusas = Statusas.laukiama;
        List<Administrator> admins = administratorRepository.findAll();
        int admin = admins.get(0).getIdNaudotojas();
        RequestForm requestForm = new RequestForm(pradziosData, pabaigosData, priezastis, statusas, darbuotojas,admin);
        requestFormRepository.save(requestForm);
        return "redirect:/day-request";
    }

    @GetMapping("/day-request-review")
    public String showDayRequestReviewPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        boolean isAdmin = userService.isAdministrator(loggedInUser.getId());
        if (!userService.isAdministrator(loggedInUser.getId())) {
            return "redirect:/access-denied";
        }
        Statusas statusas = Statusas.laukiama;
        List<RequestForm> forms = requestFormRepository.findAllByStatusasEquals(statusas);
        model.addAttribute("forms", forms);
        return "WorkSchedule/day-request-review";
    }

    @PostMapping("/day-request/reject")
    public String dayRequestReject(@RequestParam int formId) {
        Statusas statusas = Statusas.atmesta;
        System.out.println(formId + " " + statusas);

        requestFormRepository.foreignKeyOff();
        requestFormRepository.updateStatus(formId, statusas);
        requestFormRepository.foreignKeyOn();
        return "redirect:/day-request-review";
    }

    @PostMapping("/day-request/accept")
    public String dayRequestAccept(@RequestParam int formId) {
        Statusas statusas = Statusas.priimta;
        System.out.println(formId + " " + statusas);

        requestFormRepository.foreignKeyOff();
        requestFormRepository.updateStatus(formId, statusas);
        requestFormRepository.foreignKeyOn();
        return "redirect:/day-request-review";
    }


}

