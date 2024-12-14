package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.Client;
import com.example.RestoranoIS.Models.CustomerTable;
import com.example.RestoranoIS.Models.Reservation;
import com.example.RestoranoIS.Models.User;
import com.example.RestoranoIS.Repositories.ClientRepository;
import com.example.RestoranoIS.Repositories.CustomerTableRepository;
import com.example.RestoranoIS.Repositories.UserRepository;
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
    private List<Reservation> reservations = new ArrayList<>();
    private int lastReservationId = 0;

    // Display all reservations
    @GetMapping("/reservations")
    public String showReservationsList(Model model) {
        model.addAttribute("reservations", reservations);
        return "Reservations/reservations-list";
    }

    // View a single reservation
    /*@GetMapping("/reservations/view/{id}")
    public String viewReservation(@PathVariable int id, Model model) {
        Reservation reservation = findReservationById(id);
        if (reservation != null) {
            model.addAttribute("reservation", reservation);
            return "Reservations/view-reservation";
        } else {
            model.addAttribute("info", "Reservation not found.");
            return "redirect:/reservations";
        }
    }
    */
    // Show the form to create a new reservation
    @GetMapping("/reservations/create")
    public String showCreateReservationForm(Model model) {
        Reservation reservation = new Reservation();
        Client client = new Client();
        List<CustomerTable> staliukai = customerTableRepository.findAll();

        reservation.setKlientas(client);
        model.addAttribute("reservation", reservation);
        model.addAttribute("staliukai", staliukai);
        return "Reservations/create-reservation";
    }


    // Process the creation of a new reservation
    @PostMapping("/reservations/create/submit")
    public String createReservation(@RequestParam("reservation") Reservation reservation,
                                    @RequestParam("pradzia") LocalDateTime pradzia,
                                    @RequestParam("pabaiga") LocalDateTime pabaiga,
                                    @RequestParam("zmoniukiekis") Integer zmoniuKiekis,
                                    @RequestParam("pageidavimas") String pageidavimas,
                                    Model model, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Integer userId = loggedInUser.getId();

        //Client client = reservation.set;

        //clientid= client.getIdNaudotojas();

        reservation.setPradzia(pradzia);
        reservation.setPabaiga(pabaiga);
        reservation.setZmoniuKiekis(zmoniuKiekis);
        reservation.setPageidavimas(pageidavimas);
        //reservation.setKlientas(client.getIdNaudotojas());
        //Reservation newReservation = new Reservation(lastReservationId++, customerName, date, people);
        //reservations.add(newReservation);
        model.addAttribute("reservations", reservations);
        return "redirect:/reservations";
    }
/*
    // Show the form to edit an existing reservation
    @GetMapping("/reservations/edit/{id}")
    public String showEditReservationForm(@PathVariable int id, Model model) {
        Reservation reservation = findReservationById(id);
        if (reservation != null) {
            model.addAttribute("reservation", reservation);
            return "Reservations/edit-reservation";
        } else {
            model.addAttribute("info", "Reservation not found.");
            return "redirect:/reservations";
        }
    }

    // Process the edit of a reservation
    @PostMapping("/reservations/edit/submit/{id}")
    public String editReservation(@PathVariable int id,
                                  @RequestParam("customerName") String customerName,
                                  @RequestParam("date") String date,
                                  @RequestParam("people") int people,
                                  Model model) {
        Reservation reservation = findReservationById(id);
        if (reservation != null) {
            reservation.setCustomerName(customerName);
            reservation.setDate(date);
            reservation.setPeople(people);
            model.addAttribute("reservation", reservation);
            return "redirect:/reservations/view/" + id;
        } else {
            model.addAttribute("info", "Reservation not found.");
            return "redirect:/reservations";
        }
    }

    // Show delete confirmation page
    @GetMapping("/reservations/delete/confirm/{id}")
    public String showDeleteConfirmation(@PathVariable int id, Model model) {
        Reservation reservation = findReservationById(id);
        if (reservation != null) {
            model.addAttribute("reservation", reservation);
            return "Reservations/delete-reservation";
        } else {
            model.addAttribute("info", "Reservation not found.");
            return "redirect:/reservations";
        }
    }

    // Process the final deletion after confirmation
    @GetMapping("/reservations/delete/{id}")
    public String confirmDeleteReservation(@PathVariable int id, Model model) {
        reservations.removeIf(reservation -> reservation.getId() == id);
        model.addAttribute("info", "Reservation deleted successfully.");
        return "redirect:/reservations";
    }

    // Helper method to find a reservation by ID
    private Reservation findReservationById(int id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElse(null);
    }*/
}