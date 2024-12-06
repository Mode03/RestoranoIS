package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByelPastas(String elPastas);
}
