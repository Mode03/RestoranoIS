package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.Reservation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReservationsController {

    private List<Reservation> reservations = new ArrayList<>();
    private int lastReservationId = 0;

    // Display all reservations
    @GetMapping("/reservations")
    public String showReservationsList(Model model) {
        model.addAttribute("reservations", reservations);
        return "Reservations/reservations-list";
    }

    // View a single reservation
    @GetMapping("/reservations/view/{id}")
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

    // Show the form to create a new reservation
    @GetMapping("/reservations/create")
    public String showCreateReservationForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        return "Reservations/create-reservation";
    }

    // Process the creation of a new reservation
    @PostMapping("/reservations/create/submit")
    public String createReservation(@RequestParam("customerName") String customerName,
                                    @RequestParam("date") LocalDate date,
                                    @RequestParam("people") int people,
                                    Model model) {
        Reservation newReservation = new Reservation(lastReservationId++, customerName, date, people);
        reservations.add(newReservation);
        model.addAttribute("reservations", reservations);
        return "redirect:/reservations";
    }

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
    }
}