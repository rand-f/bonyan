package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "meeting topic can not be empty")
    @Column(columnDefinition = "varchar(255) not null")
    private String topic;

    @NotNull(message = "start date can not be null")
    @Column(columnDefinition = "datetime not null")
    private LocalDateTime startDate;

    @NotNull(message = "duration can not be null")
    @Column(columnDefinition = "int not null")
    private Integer duration;

    @NotEmpty(message = "meeting link can not be empty")
    @Column(columnDefinition = "varchar(255) not null")
    private String link;

    @ManyToOne
    @JsonIgnore
    private Project project;

    @Column(columnDefinition = "datetime")
    private LocalDateTime createdAt;
}
