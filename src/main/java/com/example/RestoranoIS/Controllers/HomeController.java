package com.example.RestoranoIS.Controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping("")
    public String home(){
        return "main";
    }

    //Prisijungimo langas
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin() {
        // Prisijungimo logika
        return "redirect:/main";
    }

    // Pagrindinis langas
    @GetMapping("/main")
    public String showMainPage() {
        return "main";
    }

    // Registracijos langas
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration() {
        // Registravimosi logika
        return "redirect:/main";
    }
    // Profilio langas
    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }

    @GetMapping("/edit-profile")
    public String showEditProfilePage() {
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String updateProfile() {
        // Logika redagavimui
        return "redirect:/edit-profile";
    }
  
    @GetMapping("/day-request")
    public String showDayRequestPage() {return "day-request";}
}
