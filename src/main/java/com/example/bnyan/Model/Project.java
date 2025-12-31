package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

    @Positive(message = "budget must be a positive value")
    //@NotNull(message = "budget can not be empty")
    @Column(columnDefinition = "double")
    private Double budget;

    //@NotEmpty(message = "project description can not be empty")
    @Column(columnDefinition = "varchar(500)")
    private String description;

    //@NotNull(message = "start date is required")
    @Column
    private LocalDate startDate;

   // @Positive(message = "duration must be a positive value")
   @Column
    private Integer duration;

    @Pattern(regexp = "^(preparing|on going|completed)")
    private String status;

   // @NotNull(message = "expected end date is required")
    private LocalDate expectedEndDate;
    private LocalDateTime created_at;

    //--------------------------------- relations ------------------------------

    @OneToMany(mappedBy = "project")
    private Set<Task> task;

    @ManyToMany
    private Set<Specialist> specialists;

    @ManyToOne
    @JsonIgnore
    private ProjectManager projectManager;

    @ManyToOne
    @MapsId
    @JsonIgnore
    private Customer customer;

    @OneToMany
    @JsonIgnore
    private Set<SpecialistRequest>requests;

    @OneToOne(mappedBy = "project",cascade = CascadeType.ALL)
    @JoinColumn()
    @JsonIgnore
    private BuildRequest buildRequest;

    @OneToOne
    private Land land;

    @OneToMany(mappedBy = "project")
    private Set<Meeting> meeting;
}
