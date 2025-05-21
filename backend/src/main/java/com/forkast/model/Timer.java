package com.forkast.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "timers")
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Timer duration is required")
    @Positive(message = "Timer duration must be positive")
    @Column(nullable = false)
    private Integer duration; // in seconds

    @NotBlank(message = "Timer description is required")
    @Column
    private String description;

    @OneToOne(mappedBy = "timer")
    private Step step;
}
