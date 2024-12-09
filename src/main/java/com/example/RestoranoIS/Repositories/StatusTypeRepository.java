package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusTypeRepository extends JpaRepository<StatusType, Integer> {
}
