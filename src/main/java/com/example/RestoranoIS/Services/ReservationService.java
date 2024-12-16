package com.example.RestoranoIS.Services;

import com.example.RestoranoIS.Models.*;
import com.example.RestoranoIS.Repositories.ClientRepository;
import com.example.RestoranoIS.Repositories.CustomerTableRepository;
import com.example.RestoranoIS.Repositories.ReservationRepository;
import com.example.RestoranoIS.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ReservationService {

    private final CustomerTableRepository customerTableRepository;

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final ClientRepository clientRepository;

    @Autowired
    public ReservationService(CustomerTableRepository customerTableRepository, UserRepository userRepository
            ,ReservationRepository reservationRepository,ClientRepository clientRepository){
        this.customerTableRepository=customerTableRepository;
        this.userRepository=userRepository;
        this.reservationRepository=reservationRepository;
        this.clientRepository=clientRepository;
    }

    public Reservation createReservation(LocalDateTime pradzia, LocalDateTime pabaiga, Integer zmoniuKiekis,String pageidavimas
            ,Client client,CustomerTable table) {


        Reservation reservation = new Reservation();
        reservation.setPradzia(pradzia);
        reservation.setPabaiga(pabaiga);
        reservation.setZmoniuKiekis(zmoniuKiekis);
        reservation.setPageidavimas(pageidavimas);
        reservation.setKlientas(client);
        reservation.setStaliukas(table);

        return reservationRepository.save(reservation);

    }
    public CustomerTable getCustomerTableById(Integer Id) {
        return customerTableRepository.findById(Id).orElse(null); // Grąžina null, jei nėra
    }

    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }
    public List<CustomerTable> getAllCustomerTables() {
        return customerTableRepository.findAll();
    }

    public List<Reservation> getReservationsByClientId(Integer clientId) {
        return reservationRepository.findByKlientas_idNaudotojas(clientId);
    }

    public CustomerTable getBiggestCustomerTable(){
        List<CustomerTable> staliukai = customerTableRepository.findAll();
        CustomerTable biggest = staliukai.get(1);
        for (CustomerTable item : staliukai){
            if(item.getVietuSkaicius()> biggest.getVietuSkaicius()){
                biggest = item;
            }
        }
        return biggest;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    public Reservation getReservationById(Integer id) {
        return reservationRepository.findById(id).orElse(null);
    }
    public void deleteReservationById(Integer id) {
        reservationRepository.deleteById(id);
    }

    public CustomerTable findOptimalStaliukas(LocalDateTime pradzia, LocalDateTime pabaiga, int zmoniuKiekis) {

        List<Reservation> existingReservations = reservationRepository.findAll();

        // Step 1: Find tables already reserved in the overlapping time
        List<CustomerTable> reservedTables = existingReservations.stream()
                .filter(reservation ->
                        (pradzia.isBefore(reservation.getPabaiga()) && pabaiga.isAfter(reservation.getPradzia())))
                .map(Reservation::getStaliukas)
                .toList();

        // Step 2: Get all tables and exclude reserved ones
        List<CustomerTable> availableTables = customerTableRepository.findAll().stream()
                .filter(table -> !reservedTables.contains(table))
                .toList();

        // Step 3: Find the smallest table that can accommodate the people
        return availableTables.stream()
                .filter(table -> table.getVietuSkaicius() >= zmoniuKiekis)
                .min(Comparator.comparingInt(CustomerTable::getVietuSkaicius)) // Find the smallest table
                .orElse(null);
    }
    public CustomerTable findOptimalStaliukas(LocalDateTime pradzia, LocalDateTime pabaiga, int zmoniuKiekis,Reservation currentReservation) {

        List<Reservation> existingReservations = reservationRepository.findAll();

        // Step 1: Find tables already reserved in the overlapping time
        List<CustomerTable> reservedTables = existingReservations.stream()
                .filter(reservation ->
                        !reservation.equals(currentReservation) && // Exclude the current reservation
                                (pradzia.isBefore(reservation.getPabaiga()) && pabaiga.isAfter(reservation.getPradzia())))
                .map(Reservation::getStaliukas)
                .toList();


        // Step 2: Get all tables and exclude reserved ones
        List<CustomerTable> availableTables = customerTableRepository.findAll().stream()
                .filter(table -> !reservedTables.contains(table))
                .toList();

        // Step 3: Find the smallest table that can accommodate the people
        return availableTables.stream()
                .filter(table -> table.getVietuSkaicius() >= zmoniuKiekis)
                .min(Comparator.comparingInt(CustomerTable::getVietuSkaicius)) // Find the smallest table
                .orElse(null);
        //() -> new RuntimeException("No suitable table available")
    }

}
