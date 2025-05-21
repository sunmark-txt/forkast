package com.forkast.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "steps")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Step order number is required")
    @Positive(message = "Step order number must be positive")
    @Column(nullable = false)
    private Integer orderNumber;

    @NotBlank(message = "Step description is required")
    @Size(min = 10, max = 1000, message = "Step description must be between 10 and 1000 characters")
    @Column(length = 1000, nullable = false)
    private String description;

    @Column
    private String imageUrl;

    @NotNull(message = "Recipe reference is required")
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "timer_id")
    private Timer timer;
}
