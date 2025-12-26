package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Specialist {
    @Id
    private Integer id;

    // I think it's better to have created_at in the user model since all users have it
    // private LocalDateTime created_at;

    @Pattern(regexp = "^(ARCHITECTURAL_ENGINEER|MECHANICAL_ENGINEER|ELECTRICAL_ENGINEER|CIVIL_ENGINEER|PROJECT_MANAGER|DESIGNER|GENERAL_CONTRACTOR)$",message = "specialty must be: ARCHITECTURAL_ENGINEER, MECHANICAL_ENGINEER, ELECTRICAL_ENGINEER, CIVIL_ENGINEER, PROJECT_MANAGER|DESIGNER or GENERAL_CONTRACTOR")
    @Column(columnDefinition = "varchar(50) not null")
    private String speciality;

    //--------------------------------- relations ------------------------------

    @ManyToMany(mappedBy = "specialists")
    private Set<Domain>domains;

    @ManyToMany
    @JsonIgnore
    private Set<Project>projects;

    //@OneToMany
    //private Set<Review>reviews;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany
    @JsonIgnore
    private Set<SpecialistRequest>requests;
}
