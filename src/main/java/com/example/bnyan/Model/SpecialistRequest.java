package com.example.bnyan.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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

    private String description;
    private LocalDate expectedStartDate;
    private Double offeredPrice;

    @Pattern(regexp = "^(pending|excepted|rejected)$",message = "status must be:")
    private String status;


    private LocalDate projectExpectedEndDate;
    private LocalDateTime created_at;

    //--------------------------------- relations ------------------------------

    //private Land land;

    @ManyToOne
    private Project project;
    @ManyToOne
    private Specialist specialist;
}
