package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double budget;
    private String description;

    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDateTime created_at;

    //--------------------------------- relations ------------------------------

    //private Stage stage;

    @ManyToMany
    private Set<Specialist> specialists;

    @ManyToOne
    @MapsId
    @JsonIgnore
    private ProjectManager projectManager;

    @ManyToOne
    @MapsId
    @JsonIgnore
    private Customer customer;

    @OneToMany
    @JsonIgnore
    private Set<SpecialistRequest>requests;

}
