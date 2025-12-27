package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BuildRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "status must be filled")
    @Pattern(regexp = "^(approved|rejected|processing)$",
            message = "status must be approved or rejected or processing")
    @Column(columnDefinition = "varchar(20) not null")
    private String status;

    @Column(columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @OneToOne
    @JsonIgnore
    private Land land;

   @OneToOne
   @JsonIgnore
   private Project project;

}