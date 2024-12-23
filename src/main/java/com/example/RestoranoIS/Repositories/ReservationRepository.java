package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {

    List<Reservation> findAllByKlientasIdNaudotojas(Integer idNaudotojas);

    List<Reservation> findByKlientas_idNaudotojas(Integer clientId);
}
