package com.example.gourmet.repository;

import com.example.gourmet.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    
    // Custom queries for filtering
    List<Shop> findByCategory(String category);
    
    List<Shop> findByBudget(String budget);
    
    @Query("SELECT s FROM Shop s WHERE " +
           "(:category IS NULL OR :category = '' OR s.category = :category) AND " +
           "(:budget IS NULL OR :budget = '' OR s.budget = :budget) " +
           "ORDER BY s.createdAt DESC")
    List<Shop> searchShops(@Param("category") String category, @Param("budget") String budget);
}
