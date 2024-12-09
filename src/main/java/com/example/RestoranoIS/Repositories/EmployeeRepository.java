package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByIdNaudotojas(Integer idNaudotojas);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO darbuotojai (adresas, alga, asmens_kodas, atostogu_dienos, fk_miestas, nuo_kada_dirba, pareigos, telefonas, id_naudotojas) " +
            "VALUES (:adresas, :alga, :asmensKodas, :atostoguDienos, :fkMiestas, :nuoKadaDirba, :pareigos, :telefonas, :idNaudotojas)", nativeQuery = true)
    void insertEmployee(@Param("adresas") String adresas,
                        @Param("alga") double alga,
                        @Param("asmensKodas") String asmensKodas,
                        @Param("atostoguDienos") int atostoguDienos,
                        @Param("fkMiestas") int fkMiestas,
                        @Param("nuoKadaDirba") LocalDate nuoKadaDirba,
                        @Param("pareigos") String pareigos,
                        @Param("telefonas") String telefonas,
                        @Param("idNaudotojas") int idNaudotojas);
}
