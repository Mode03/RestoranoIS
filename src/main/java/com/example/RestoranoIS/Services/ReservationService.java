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



       // return orderRepository.save(order);
    }
    public CustomerTable getCustomerTableById(Integer Id) {
        return customerTableRepository.findById(Id).orElse(null); // Grąžina null, jei nėra
    }
    public void saveOrder(Reservation reservation) {
        // Užsakymo išsaugojimas duomenų bazėje
        reservationRepository.save(reservation);
    }
}
