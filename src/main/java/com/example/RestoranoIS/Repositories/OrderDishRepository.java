package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.OrderDish;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDishRepository extends JpaRepository<OrderDish, Integer> {

    List<OrderDish> findByOrderId(Integer orderId);

    void deleteByOrderId(Integer orderId);
}
