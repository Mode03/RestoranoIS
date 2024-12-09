package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    boolean existsByIdNaudotojas(Integer idNaudotojas);

}