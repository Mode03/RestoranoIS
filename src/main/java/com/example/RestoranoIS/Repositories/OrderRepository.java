package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByClient_idNaudotojas(Integer idNaudotojas);
}
