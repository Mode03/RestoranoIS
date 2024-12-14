package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.CustomerTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTableRepository extends JpaRepository<CustomerTable,Integer> {
}
