package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.StorageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageTypeRepository extends JpaRepository<StorageType, Integer> {
}
