package com.example.RestoranoIS.Repositories;

import com.example.RestoranoIS.Models.StorageProducts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageProductsRepository extends JpaRepository<StorageProducts, Integer> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SANDELIU_MAISTO_PRODUKTAI WHERE fk_Maisto_produktas= :id",nativeQuery = true)
    void deleteByProduct(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SANDELIU_MAISTO_PRODUKTAI VALUES (:idP, :idS)", nativeQuery = true)
    void insertStorageProduct(@Param("idS") int idS, @Param("idP") int idP);

    @Query(value = """
        SELECT
            ROW_NUMBER() OVER () AS id,
            fk_Maisto_produktas,
            fk_Sandelis
            FROM SANDELIU_MAISTO_PRODUKTAI
            WHERE fk_Sandelis = :storageId
            """, nativeQuery = true)
    List<StorageProducts> getStorageProductsByStorage(@Param("storageId") Integer storageId);

    @Query(value = """
        SELECT
            ROW_NUMBER() OVER () AS id,
            fk_Maisto_produktas,
            fk_Sandelis
            FROM SANDELIU_MAISTO_PRODUKTAI
            WHERE fk_Maisto_produktas = :productId
            """, nativeQuery = true)
    StorageProducts getStorageProductsByProduct(@Param("productId") Integer productId);
}
