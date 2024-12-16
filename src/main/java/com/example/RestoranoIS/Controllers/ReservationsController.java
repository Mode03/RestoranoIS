package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.*;
import com.example.RestoranoIS.Repositories.ClientRepository;
import com.example.RestoranoIS.Repositories.CustomerTableRepository;
import com.example.RestoranoIS.Repositories.ReservationRepository;
import com.example.RestoranoIS.Repositories.UserRepository;
import com.example.RestoranoIS.Services.OrderService;
import com.example.RestoranoIS.Services.ReservationService;
import com.example.RestoranoIS.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReservationsController {

    @Autowired
    private CustomerTableRepository customerTableRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    private ReservationRepository reservationRepository;

    private final UserService userService;
    private final ReservationService reservationService;

    @Autowired
    public ReservationsController(UserService userService, ReservationService reservationService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }



    // Display all reservations
    @GetMapping("/reservations")
    public String showReservationsList(Model model,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        model.addAttribute("isAdmin", isAdmin);

        // Patikriname, ar naudotojas yra administratorius
        if (userService.isAdministrator(userId)) {
            // Jei administratorius, rodomi visos rezervacijos
            List<Reservation> reservations = reservationService.getAllReservations();
            model.addAttribute("reservations", reservations);
        } else if (userService.isClient(userId)) {
            // Jei klientas, rodomi tik jo rezervacijos
            List<Reservation> reservations = reservationService.getReservationsByClientId(userId);
            model.addAttribute("reservations", reservations);
        } else {
            // Jei naudotojas nėra nei administratorius, nei klientas
            return "redirect:/access-denied";
        }

        return "Reservations/reservations-list";
    }

    // View a single reservation
    @GetMapping("/reservations/view/{id}")
    public String viewReservation(@PathVariable("id") Integer id, Model model,HttpSession session) {
        // Fetch the reservation by ID

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Reservation reservation = reservationService.getReservationById(id);

        if (reservation != null) {
            model.addAttribute("reservation", reservation);
            return "Reservations/view-reservation"; // The Thymeleaf template to display
        } else {
            model.addAttribute("info", "Rezervacija nerasta.");
            return "redirect:/reservations";
        }
    }


    // Show the form to create a new reservation
    @GetMapping("/reservations/create")
    public String showCreateReservationForm(Model model,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Reservation reservation = new Reservation();
        Client client = new Client();


        CustomerTable staliukas = reservationService.getBiggestCustomerTable();
        reservation.setKlientas(client);
        model.addAttribute("reservation", reservation);
        model.addAttribute("staliukas", staliukas);
        return "Reservations/create-reservation";
    }


    // Process the creation of a new reservation
    @PostMapping("/reservations/create/submit")
    public String createReservation(//@RequestParam("reservation") Reservation reservation,
                                    @RequestParam("pradzia") LocalDateTime pradzia,
                                    @RequestParam("pabaiga") LocalDateTime pabaiga,
                                    @RequestParam("zmoniuKiekis") Integer zmoniuKiekis,
                                    @RequestParam(value ="pageidavimas", required = false) String pageidavimas,
                                    Model model, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Ensure user is logged in
        }

        // Fetch the client linked to the logged-in user
        Client client = userService.getClientByUserId(loggedInUser.getId());


        // Calculate the optimal table based on availability and capacity
        CustomerTable optimalTable = reservationService.findOptimalStaliukas(pradzia, pabaiga, zmoniuKiekis);
        if (optimalTable == null) {
            model.addAttribute("info", "Nėra laisvų staliukų pasirinktui laikui ir žmonių kiekiui!");
            return "Reservations/create-reservation"; // Redirect back to form with error
        }

        // Create and save the reservation
        Reservation reservation = reservationService.createReservation(pradzia, pabaiga, zmoniuKiekis, pageidavimas, client, optimalTable);
        reservationService.saveReservation(reservation);

        return "redirect:/reservations";
    }

    @GetMapping("/reservations/edit/{id}")
    public String showEditReservationForm(@PathVariable Integer id, Model model) {
        // Fetch the reservation by ID
        Reservation reservation = reservationService.getReservationById(id);

        if (reservation != null) {
            model.addAttribute("reservation", reservation);

            // Fetch all tables for the dropdown
            List<CustomerTable> staliukai = reservationService.getAllCustomerTables();
            model.addAttribute("staliukai", staliukai);

            return "Reservations/edit-reservation";
        } else {
            model.addAttribute("info", "Rezervacija nerasta.");
            return "redirect:/reservations";
        }
    }


    // Process the edit of a reservation
    @PostMapping("/reservations/edit/submit/{id}")
    public String editReservation(
            @PathVariable Integer id,
            @ModelAttribute Reservation reservation,
            Model model) {

        Reservation existingReservation = reservationService.getReservationById(id);

        if (existingReservation != null) {
            // Update the reservation fields
            existingReservation.setPradzia(reservation.getPradzia());
            existingReservation.setPabaiga(reservation.getPabaiga());
            existingReservation.setZmoniuKiekis(reservation.getZmoniuKiekis());
            existingReservation.setPageidavimas(reservation.getPageidavimas());

            CustomerTable optimalTable = reservationService.findOptimalStaliukas(
                    reservation.getPradzia(),
                    reservation.getPabaiga(),
                    reservation.getZmoniuKiekis(),
                    reservation
            );

            if (optimalTable == null) {
                // No suitable table available, show an error
                model.addAttribute("info", "Nėra laisvų staliukų pasirinktui laikui ir žmonių kiekiui!");
                return "Reservations/edit-reservation";
            }

            // Assign the optimal table
            existingReservation.setStaliukas(optimalTable);

            // Save the updated reservation
            reservationService.saveReservation(existingReservation);

            return "redirect:/reservations/view/" + id;
        } else {
            model.addAttribute("info", "Reservation not found.");
            return "redirect:/reservations";
        }
    }


    // Process the final deletion after confirmation
    @GetMapping("/reservations/delete/{id}")
    public String confirmDeleteReservation(@PathVariable("id") Integer id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation != null) {
            model.addAttribute("reservation", reservation);
            return "Reservations/delete-reservation"; // Render the delete confirmation page
        } else {
            model.addAttribute("info", "Nerasta rezervacija");
            return "redirect:/reservations";
        }
    }

    @PostMapping("/reservations/delete/{id}")
    public String deleteReservation(@PathVariable("id") Integer id, Model model) {
        reservationService.deleteReservationById(id);
        model.addAttribute("info", "Rezervacija ištrinta sėkmingai");
        return "redirect:/reservations";
    }


}