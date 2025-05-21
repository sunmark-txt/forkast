package com.forkast.repository;

import com.forkast.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.repository.param.Param;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    
    List<Ingredient> findByUserEmailOrderByLastUsedDesc(String email);
    
    List<Ingredient> findByUserEmailOrderByUsageCountDesc(String email);
    
    @Query("SELECT i FROM Ingredient i WHERE i.user.email = :email AND i.name LIKE %:searchTerm%")
    List<Ingredient> searchByUserAndName(@Param("email") String email, @Param("searchTerm") String searchTerm);
} 