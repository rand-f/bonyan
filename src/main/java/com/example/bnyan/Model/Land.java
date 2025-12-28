package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Land {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "location must be filled")
    @Column(columnDefinition = "varchar(100) not null")
    private String location;

    @NotNull(message = "size must be filled")
    @Column(columnDefinition = "double not null")
    private Double size;

    @NotEmpty(message = "authorization status must be filled")
    @Pattern(regexp = "^(authorized|notAuthorized)$",
            message = "authorization must be authorized or notAuthorized")
    @Column(columnDefinition = "varchar(20) not null")
    private String authorizationStatus;

    @Column(columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @OneToOne(mappedBy = "land")
    @JsonIgnore
    private Project project;
}