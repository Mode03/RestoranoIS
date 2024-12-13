package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Employee;
import com.example.RestoranoIS.Models.RequestForm;
import com.example.RestoranoIS.Models.Statusas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RequestFormRepository extends JpaRepository<RequestForm, Integer> {

 List<RequestForm> findAllByFkDarbuotojas(Employee employee);


  List<RequestForm> findAllByStatusasEquals(Statusas status);

 @Modifying
 @Transactional
 @Query("UPDATE RequestForm rf SET rf.statusas = :statusas WHERE rf.id = :id")
 void updateStatus(@Param("id") Integer id, @Param("statusas") Statusas statusas);

 @Modifying
 @Transactional
 @Query(value = "SET FOREIGN_KEY_CHECKS = 1",nativeQuery = true)
 void foreignKeyOn();

 @Modifying
 @Transactional
 @Query(value = "SET FOREIGN_KEY_CHECKS = 0",nativeQuery = true)
 void foreignKeyOff();
}


