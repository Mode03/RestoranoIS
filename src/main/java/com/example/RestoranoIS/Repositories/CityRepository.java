package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    City findBypavadinimas(String name);
}
