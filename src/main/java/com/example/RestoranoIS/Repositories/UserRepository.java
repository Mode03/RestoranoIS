package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByelPastas(String elPastas);
}
