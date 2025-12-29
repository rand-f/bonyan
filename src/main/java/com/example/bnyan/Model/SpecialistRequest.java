package com.example.bnyan.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SpecialistRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "description can not be empty")
    @Size(max = 500, message = "description must not exceed 500 characters")
    @Column(columnDefinition = "varchar(500) not null")
    private String description;

    @NotNull(message = "expected start date is required")
    private LocalDate expectedStartDate;

    @NotNull(message = "offered price is required")
    @Positive(message = "offered price must be a positive value")
    private Double offeredPrice;

    @Pattern(regexp = "^(pending|accepted|rejected)$",message = "status must be: pending, excepted, or rejected")
    private String status;


    private LocalDate projectExpectedEndDate;
    private LocalDateTime created_at;

    //--------------------------------- relations ------------------------------


    @ManyToOne
    private Project project;
    @ManyToOne
    private Specialist specialist;
    @ManyToOne
    private ProjectManager projectManager;
}
