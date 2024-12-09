package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.DishCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishCategoryRepository extends JpaRepository<DishCategory, Integer> {
}
