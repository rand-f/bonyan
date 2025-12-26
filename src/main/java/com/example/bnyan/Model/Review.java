package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "rate must be filled")
    @Pattern(regexp = "^[1-5]$",
            message = "rate must be between 1 and 5")
    @Column(columnDefinition = "varchar(1) not null")
    private String rate;

    @NotEmpty(message = "comment must be filled")
    @Column(columnDefinition = "text not null")
    private String comment;

    @Column(columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JsonIgnore
    private Specialist specialist;
}
