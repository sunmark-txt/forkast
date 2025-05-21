package com.forkast.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Recipe name is required")
    @Size(min = 3, max = 100, message = "Recipe name must be between 3 and 100 characters")
    @Column(nullable = false)
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @NotBlank(message = "Image URL is required")
    @Column(nullable = false)
    private String imageUrl;

    @NotNull(message = "Ingredients list cannot be null")
    @Size(min = 1, message = "Recipe must have at least one ingredient")
    @ManyToMany
    @JoinTable(
        name = "recipe_ingredients",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients = new ArrayList<>();

    @NotNull(message = "Steps list cannot be null")
    @Size(min = 1, message = "Recipe must have at least one step")
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Step> steps = new ArrayList<>();

    @Positive(message = "Preparation time must be positive")
    @Column
    private Integer preparationTime; // in minutes

    @Positive(message = "Cooking time must be positive")
    @Column
    private Integer cookingTime; // in minutes

    @Positive(message = "Servings must be positive")
    @Column
    private Integer servings;

    @NotBlank(message = "Difficulty level is required")
    @Column
    private String difficulty; // EASY, MEDIUM, HARD

    @ManyToMany(mappedBy = "bookmarkedRecipes")
    private List<User> bookmarkedBy = new ArrayList<>();
}
