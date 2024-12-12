package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.City;
import com.example.RestoranoIS.Services.UserService;
import com.example.RestoranoIS.Models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class HomeController {

    // Specialūs raktai
    private static final String EMPLOYEE_KEY = "Darbuotojas123";
    private static final String ADMIN_KEY = "Administratorius123";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Pagrindinis langas
    @GetMapping("")
    public String home(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login page if not logged in
        }

        boolean isEmployee = userService.isEmployee(loggedInUser.getId());
        boolean isAdmin = userService.isAdministrator(loggedInUser.getId());
        model.addAttribute("isEmployee", isEmployee);
        model.addAttribute("isAdmin", isAdmin);

        return "main"; // Render the main page if user is logged in
    }

    @GetMapping("/main")
    // Pagrindinis langas
    public String showMainPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login page if not logged in
        }

        boolean isEmployee = userService.isEmployee(loggedInUser.getId());
        boolean isAdmin = userService.isAdministrator(loggedInUser.getId());
        model.addAttribute("isEmployee", isEmployee);
        model.addAttribute("isAdmin", isAdmin);
        return "main"; // Render the main page if user is logged in
    }

    //Prisijungimo langas
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(String username, String password, HttpSession session, Model model) {
        User user = userService.findByEmail(username);
        if (user != null && passwordEncoder.matches(password, user.getSlaptazodis())) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/main";
        }
        model.addAttribute("error", "Neteisingas el. paštas arba slaptažodis!");
        return "login";
    }

    // Registracijos langas
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        List<City> cities = userService.getAllCities();
        model.addAttribute("cities", cities);
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(String vardas, String pavarde, String gimimoData, String elPastas, String slaptazodis,
                                      String lytis, String userType, String specialKey, String asmensKodas, String pareigos,
                                      String telefonas, String adresas, String slapyvardis, String miestas, Model model) {
        try {
            // 1. Patikrinimai (pvz., slapto rakto validacija)
            if (userType == null || userType.isEmpty()) {
                throw new IllegalArgumentException("Vartotojo tipas nėra pasirinktas!");
            }

            switch (userType.toLowerCase()) {
                case "klientas":
                    if (slapyvardis == null || slapyvardis.isEmpty()) {
                        throw new IllegalArgumentException("Klientui privaloma įvesti slapyvardį!");
                    }
                    break;

                case "darbuotojas":
                    if (specialKey == null || !specialKey.equals(EMPLOYEE_KEY)) {
                        throw new IllegalArgumentException("Neteisingas slaptas raktas darbuotojui!");
                    }
                    break;

                case "administratorius":
                    if (specialKey == null || !specialKey.equals(ADMIN_KEY)) {
                        throw new IllegalArgumentException("Neteisingas slaptas raktas administratoriui!");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Neteisingas vartotojo tipas!");
            }

            // 2. Duomenų apdorojimas (tik jei patikrinimai praeiti)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate gimimoDataFormatted = LocalDate.parse(gimimoData, formatter);

            String encodedPassword = passwordEncoder.encode(slaptazodis);

            // Sukuriame User objektą
            User user = new User(vardas, pavarde, gimimoDataFormatted, elPastas, encodedPassword, lytis);

            // 3. Išsaugojame vartotoją į duomenų bazę pagal vaidmenį
            userService.registerUserWithRole(user, userType, specialKey, asmensKodas, pareigos, telefonas, adresas, slapyvardis, miestas, model);

            model.addAttribute("message", "Registracija sėkminga!");
            return "redirect:/login"; // Peradresavimas į prisijungimo puslapį
        } catch (Exception e) {
            model.addAttribute("error", "Registracijos metu įvyko klaida: " + e.getMessage());
            return "register"; // Grąžiname atgal į registracijos puslapį su klaida
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main";
    }

    // Profilio langas
    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);
        return "profile";
    }

    @GetMapping("/edit-profile")
    public String showEditProfilePage() {
        return "edit-profile";
    }

    @GetMapping("/edit-profile/details")
    public String showEditProfilePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);
        return "edit-profile";
    }
  
    @GetMapping("/day-request")
    public String showDayRequestPage() {return "WorkSchedule/day-request";}
}
