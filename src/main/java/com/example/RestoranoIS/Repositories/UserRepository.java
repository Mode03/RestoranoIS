package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByelPastas(String elPastas);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM NAUDOTOJAI WHERE id= :id",nativeQuery = true)
    void deleteUserById(@Param("id") int id);
}
