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
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "title must be filled")
    @Column(columnDefinition = "varchar(100) not null")
    private String title;

    @NotEmpty(message = "description must be filled")
    @Column(columnDefinition = "text not null")
    private String description;

    @NotEmpty(message = "status must be filled")
    @Pattern(regexp = "^(pending|inProgress|completed)$",
            message = "status must be pending or inProgress or completed")
    @Column(columnDefinition = "varchar(20) not null")
    private String status;

    @NotNull(message = "start date must be filled")
    @Column(columnDefinition = "datetime not null")
    private LocalDateTime startDate;

    @NotNull(message = "end date must be filled")
    @Column(columnDefinition = "datetime not null")
    private LocalDateTime endDate;

    @NotNull(message = "budget must be filled")
    @Column(columnDefinition = "double not null")
    private Double budget;

    @Column(columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore
    private Project project;

}