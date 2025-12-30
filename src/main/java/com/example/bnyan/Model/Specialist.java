package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Specialist {

    @Id
    private Integer id;

    //@Pattern(regexp = "^(ARCHITECTURAL_ENGINEER|MECHANICAL_ENGINEER|ELECTRICAL_ENGINEER|CIVIL_ENGINEER|PROJECT_MANAGER|DESIGNER|CONTRACTOR)$",message = "specialty must be: ARCHITECTURAL_ENGINEER, MECHANICAL_ENGINEER, ELECTRICAL_ENGINEER, CIVIL_ENGINEER, PROJECT_MANAGER|DESIGNER or GENERAL_CONTRACTOR")
    @Column(columnDefinition = "varchar(50) not null")
    private String speciality;

    // payment details
    @Column(columnDefinition = "VARCHAR(100) UNIQUE")
    private String stripeAccountId;

    private Boolean verified;

    //--------------------------------- relations ------------------------------

    @ManyToMany(mappedBy = "specialists")
    private Set<Domain>domains;

    @ManyToMany
    @JsonIgnore
    private Set<Project>projects;

    @OneToMany
    private Set<Review>reviews;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany
    @JsonIgnore
    private Set<SpecialistRequest>requests;
}
