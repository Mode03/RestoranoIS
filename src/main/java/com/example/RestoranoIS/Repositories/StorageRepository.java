package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Integer> {
    // aaa
}