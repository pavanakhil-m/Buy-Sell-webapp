package com.labweek.menumate.repository;

import com.labweek.menumate.entity.NewProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<NewProductEntity, Long> {
    // Custom query methods can be added if needed
    List<NewProductEntity> findByCategory(String category);
   // List<NewProductEntity> findByUserName(String ntId);
    List<NewProductEntity> findByNtId(String ntId);



    // Fetch recently listed products, ordered by dateListed descending
    List<NewProductEntity> findAllByOrderByDateListedDesc();

    // Search for a keyword in productName, description, or category
    @Query("SELECT p FROM NewProductEntity p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<NewProductEntity> searchProductsByName(@Param("keyword") String keyword);

    // New methods for sorting by price
    List<NewProductEntity> findAllByOrderByPriceAsc(); // Low to High
    List<NewProductEntity> findAllByOrderByPriceDesc(); // High to Low

}