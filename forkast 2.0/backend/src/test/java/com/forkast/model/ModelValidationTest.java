package com.forkast.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ModelValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void userValidation_WithValidData_ShouldPass() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setPreferences(new HashSet<>());
        user.setAllergies(new HashSet<>());

        var violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void userValidation_WithInvalidEmail_ShouldFail() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setPassword("password123");

        var violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void recipeValidation_WithValidData_ShouldPass() {
        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setDescription("Test Description");
        recipe.setImageUrl("http://example.com/image.jpg");
        recipe.setIngredients(new ArrayList<>());
        recipe.setSteps(new ArrayList<>());
        recipe.setPreparationTime(30);
        recipe.setCookingTime(45);
        recipe.setServings(4);
        recipe.setDifficulty("MEDIUM");

        var violations = validator.validate(recipe);
        assertTrue(violations.isEmpty());
    }

    @Test
    void recipeValidation_WithInvalidData_ShouldFail() {
        Recipe recipe = new Recipe();
        recipe.setName("A"); // Too short
        recipe.setDescription("Test Description");
        recipe.setPreparationTime(-1); // Negative time

        var violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() >= 2);
    }

    @Test
    void ingredientValidation_WithValidData_ShouldPass() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Salt");
        ingredient.setCategory("Spices");
        ingredient.setUnit("grams");
        ingredient.setUsageCount(0);

        var violations = validator.validate(ingredient);
        assertTrue(violations.isEmpty());
    }

    @Test
    void stepValidation_WithValidData_ShouldPass() {
        Step step = new Step();
        step.setOrderNumber(1);
        step.setDescription("This is a valid step description that meets the minimum length requirement");
        step.setImageUrl("http://example.com/step.jpg");

        var violations = validator.validate(step);
        assertTrue(violations.isEmpty());
    }

    @Test
    void stepValidation_WithInvalidData_ShouldFail() {
        Step step = new Step();
        step.setOrderNumber(0); // Invalid order number
        step.setDescription("Too short"); // Too short description

        var violations = validator.validate(step);
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() >= 2);
    }

    @Test
    void timerValidation_WithValidData_ShouldPass() {
        Timer timer = new Timer();
        timer.setDuration(300); // 5 minutes
        timer.setDescription("Cook until golden brown");

        var violations = validator.validate(timer);
        assertTrue(violations.isEmpty());
    }

    @Test
    void timerValidation_WithInvalidData_ShouldFail() {
        Timer timer = new Timer();
        timer.setDuration(-1); // Negative duration
        timer.setDescription(""); // Empty description

        var violations = validator.validate(timer);
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() >= 2);
    }
} 