package com.forkast.service;

import com.forkast.exception.ResourceNotFoundException;
import com.forkast.model.Recipe;
import com.forkast.model.User;
import com.forkast.repository.RecipeRepository;
import com.forkast.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public List<Recipe> findRecipesByIngredients(List<String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return recipeRepository.findAll();
        }
        // Require at least 2 matching ingredients for a recipe to be considered
        return recipeRepository.findByIngredients(ingredients, 2);
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));
    }

    @Transactional
    public void bookmarkRecipe(Long recipeId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Recipe recipe = getRecipeById(recipeId);
        
        user.getBookmarkedRecipes().add(recipe);
        userRepository.save(user);
    }

    public List<Recipe> getBookmarkedRecipes(String userEmail) {
        return recipeRepository.findBookmarkedByUser(userEmail);
    }

    @Transactional
    public void removeBookmark(Long recipeId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Recipe recipe = getRecipeById(recipeId);
        
        user.getBookmarkedRecipes().remove(recipe);
        userRepository.save(user);
    }

    public List<Recipe> getRecommendedRecipes(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        
        // Get user's recent ingredients
        List<String> recentIngredients = user.getRecentIngredients().stream()
                .map(ingredient -> ingredient.getName())
                .collect(Collectors.toList());
        
        // Find recipes matching user's ingredients
        List<Recipe> matchingRecipes = findRecipesByIngredients(recentIngredients);
        
        // Filter out recipes that don't match user's preferences or contain allergens
        return matchingRecipes.stream()
                .filter(recipe -> matchesUserPreferences(recipe, user))
                .collect(Collectors.toList());
    }

    private boolean matchesUserPreferences(Recipe recipe, User user) {
        // Check if recipe contains any allergens
        boolean hasAllergens = recipe.getIngredients().stream()
                .anyMatch(ingredient -> user.getAllergies().contains(ingredient.getName()));
        
        if (hasAllergens) {
            return false;
        }
        
        // Check if recipe matches user preferences
        return recipe.getIngredients().stream()
                .anyMatch(ingredient -> user.getPreferences().contains(ingredient.getName()));
    }
} 