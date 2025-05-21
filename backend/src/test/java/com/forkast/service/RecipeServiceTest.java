package com.forkast.service;

import com.forkast.exception.ResourceNotFoundException;
import com.forkast.model.Recipe;
import com.forkast.model.User;
import com.forkast.repository.RecipeRepository;
import com.forkast.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe testRecipe;
    private User testUser;

    @BeforeEach
    void setUp() {
        testRecipe = new Recipe();
        testRecipe.setId(1L);
        testRecipe.setName("Test Recipe");
        testRecipe.setDescription("Test Description");

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
    }

    @Test
    void findRecipesByIngredients_WithValidIngredients_ShouldReturnRecipes() {
        List<String> ingredients = Arrays.asList("salt", "pepper");
        List<Recipe> expectedRecipes = Arrays.asList(testRecipe);
        when(recipeRepository.findByIngredientsNameIn(ingredients)).thenReturn(expectedRecipes);

        List<Recipe> result = recipeService.findRecipesByIngredients(ingredients);

        assertNotNull(result);
        assertEquals(expectedRecipes.size(), result.size());
        verify(recipeRepository).findByIngredientsNameIn(ingredients);
    }

    @Test
    void getRecipeById_WithValidId_ShouldReturnRecipe() {
        when(recipeRepository.findById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));

        Recipe result = recipeService.getRecipeById(testRecipe.getId());

        assertNotNull(result);
        assertEquals(testRecipe.getId(), result.getId());
        assertEquals(testRecipe.getName(), result.getName());
    }

    @Test
    void getRecipeById_WithInvalidId_ShouldThrowException() {
        when(recipeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.getRecipeById(999L));
    }

    @Test
    void bookmarkRecipe_WithValidIds_ShouldSucceed() {
        when(recipeRepository.findById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        recipeService.bookmarkRecipe(testRecipe.getId(), testUser.getEmail());

        verify(recipeRepository).save(testRecipe);
        assertTrue(testRecipe.getBookmarkedBy().contains(testUser));
    }

    @Test
    void bookmarkRecipe_WithInvalidRecipeId_ShouldThrowException() {
        when(recipeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> recipeService.bookmarkRecipe(999L, testUser.getEmail()));
    }

    @Test
    void getBookmarkedRecipes_WithValidUser_ShouldReturnRecipes() {
        List<Recipe> expectedRecipes = Arrays.asList(testRecipe);
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(recipeRepository.findByBookmarkedByEmail(testUser.getEmail())).thenReturn(expectedRecipes);

        List<Recipe> result = recipeService.getBookmarkedRecipes(testUser.getEmail());

        assertNotNull(result);
        assertEquals(expectedRecipes.size(), result.size());
        verify(recipeRepository).findByBookmarkedByEmail(testUser.getEmail());
    }

    @Test
    void removeBookmark_WithValidIds_ShouldSucceed() {
        when(recipeRepository.findById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        testRecipe.getBookmarkedBy().add(testUser);

        recipeService.removeBookmark(testRecipe.getId(), testUser.getEmail());

        verify(recipeRepository).save(testRecipe);
        assertFalse(testRecipe.getBookmarkedBy().contains(testUser));
    }
} 