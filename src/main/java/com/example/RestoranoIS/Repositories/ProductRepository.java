package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // aaa
}