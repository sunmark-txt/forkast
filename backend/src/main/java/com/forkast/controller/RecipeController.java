package com.forkast.controller;

import com.forkast.model.Recipe;
import com.forkast.service.RecipeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<Recipe>> getRecipesByIngredients(
            @RequestParam(required = false) @NotEmpty(message = "Ingredients list cannot be empty") List<String> ingredients) {
        return ResponseEntity.ok(recipeService.findRecipesByIngredients(ingredients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }

    @PostMapping("/{id}/bookmark")
    public ResponseEntity<?> bookmarkRecipe(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        recipeService.bookmarkRecipe(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<List<Recipe>> getBookmarkedRecipes(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(recipeService.getBookmarkedRecipes(userDetails.getUsername()));
    }

    @DeleteMapping("/{id}/bookmark")
    public ResponseEntity<?> removeBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        recipeService.removeBookmark(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
