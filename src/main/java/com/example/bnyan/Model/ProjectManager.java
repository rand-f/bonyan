package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class ProjectManager {
    @Id
    private Integer id;

    @Column(columnDefinition = "varchar(50) not null")
    private String speciality="PROJECT_MANAGER";

    //--------------------------------- relations ------------------------------

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany()
    private Set<Project> project;

    @OneToMany
    @JsonIgnore
    private Set<SpecialistRequest>requests;
}
