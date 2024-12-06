package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    // Ga
}
