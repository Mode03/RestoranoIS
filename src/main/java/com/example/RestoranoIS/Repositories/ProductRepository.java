package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM MAISTO_PRODUKTAI WHERE id= :id",nativeQuery = true)
    void deleteProductById(@Param("id") int id);
}
