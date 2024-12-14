package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.Client;
import com.example.RestoranoIS.Models.CustomerTable;
import com.example.RestoranoIS.Models.Reservation;
import com.example.RestoranoIS.Models.User;
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
    public String showReservationsList(Model model) {


        model.addAttribute("reservations", reservationService.getAllReservations());
        return "Reservations/reservations-list";
    }

    // View a single reservation
    @GetMapping("/reservations/view/{id}")
    public String viewReservation(@PathVariable("id") Integer id, Model model) {
        // Fetch the reservation by ID
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
    public String createReservation(//@RequestParam("reservation") Reservation reservation,
                                    @RequestParam("pradzia") LocalDateTime pradzia,
                                    @RequestParam("pabaiga") LocalDateTime pabaiga,
                                    @RequestParam("zmoniuKiekis") Integer zmoniuKiekis,
                                    @RequestParam(value ="pageidavimas", required = false) String pageidavimas,
                                    @RequestParam("staliukoNr") Integer staliukas,
                                    Model model, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Integer userId = loggedInUser.getId();

        Client client ;
        CustomerTable table;
        client = userService.getClientByUserId(loggedInUser.getId());
        table= reservationService.getCustomerTableById(staliukas);
        Reservation reservation = reservationService.createReservation(pradzia,pabaiga,zmoniuKiekis,pageidavimas,client,table);

        reservationService.saveOrder(reservation);
        //reservation.setKlientas(client.getIdNaudotojas());
        //Reservation newReservation = new Reservation(lastReservationId++, customerName, date, people);
        //reservations.add(newReservation);
        //model.addAttribute("reservations", reservations);
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
    }*/


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