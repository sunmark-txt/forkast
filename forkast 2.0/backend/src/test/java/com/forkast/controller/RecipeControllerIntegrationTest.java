package com.forkast.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forkast.model.Recipe;
import com.forkast.model.User;
import com.forkast.repository.RecipeRepository;
import com.forkast.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    private Recipe testRecipe;
    private User testUser;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        userRepository.save(testUser);

        testRecipe = new Recipe();
        testRecipe.setName("Test Recipe");
        testRecipe.setDescription("Test Description");
        testRecipe.setImageUrl("http://example.com/image.jpg");
        testRecipe.setPreparationTime(30);
        testRecipe.setCookingTime(45);
        testRecipe.setServings(4);
        testRecipe.setDifficulty("MEDIUM");
        recipeRepository.save(testRecipe);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getRecipesByIngredients_WithValidIngredients_ShouldReturnRecipes() throws Exception {
        List<String> ingredients = Arrays.asList("salt", "pepper");

        mockMvc.perform(get("/recipes")
                .param("ingredients", ingredients.toArray(new String[0])))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getRecipesByIngredients_WithEmptyIngredients_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/recipes")
                .param("ingredients", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getRecipe_WithValidId_ShouldReturnRecipe() throws Exception {
        mockMvc.perform(get("/recipes/{id}", testRecipe.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testRecipe.getId()))
                .andExpect(jsonPath("$.name").value(testRecipe.getName()));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getRecipe_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/recipes/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void bookmarkRecipe_WithValidId_ShouldSucceed() throws Exception {
        mockMvc.perform(post("/recipes/{id}/bookmark", testRecipe.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void bookmarkRecipe_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(post("/recipes/{id}/bookmark", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getBookmarkedRecipes_ShouldReturnBookmarkedRecipes() throws Exception {
        // First bookmark a recipe
        mockMvc.perform(post("/recipes/{id}/bookmark", testRecipe.getId()))
                .andExpect(status().isOk());

        // Then get bookmarked recipes
        mockMvc.perform(get("/recipes/bookmarks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(testRecipe.getId()));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void removeBookmark_WithValidId_ShouldSucceed() throws Exception {
        // First bookmark a recipe
        mockMvc.perform(post("/recipes/{id}/bookmark", testRecipe.getId()))
                .andExpect(status().isOk());

        // Then remove the bookmark
        mockMvc.perform(delete("/recipes/{id}/bookmark", testRecipe.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void removeBookmark_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/recipes/{id}/bookmark", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
} 