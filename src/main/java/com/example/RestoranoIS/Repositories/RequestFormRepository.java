package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.RequestForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestFormRepository extends JpaRepository<RequestForm, Integer> {

 List<RequestForm> findAllByFkDarbuotojas(int id);

}
