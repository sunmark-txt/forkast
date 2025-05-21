package com.forkast.repository;

import com.forkast.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE i.name IN :ingredients GROUP BY r HAVING COUNT(DISTINCT i) >= :minMatchCount")
    List<Recipe> findByIngredients(@Param("ingredients") List<String> ingredients, @Param("minMatchCount") int minMatchCount);
    
    @Query("SELECT r FROM Recipe r JOIN r.bookmarkedBy u WHERE u.email = :email")
    List<Recipe> findBookmarkedByUser(@Param("email") String email);
    
    List<Recipe> findByDifficulty(String difficulty);
} 