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
public class Built {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "location must be filled")
    @Column(columnDefinition = "varchar(100) not null")
    private String location;

    @NotEmpty(message = "description must be filled")
    @Column(columnDefinition = "varchar(255) not null")
    private String description;

    @NotNull(message = "size must be filled")
    @Column(columnDefinition = "varchar(50) not null")
    private String size;

    @NotNull(message = "price must be filled")
    @Column(columnDefinition = "double not null")
    private Double price;

    @NotEmpty(message = "status must be filled")
    @Pattern(regexp = "^(forRent|forSell|owned)$", message = "status must be forRent or forSell or owned")
    @Column(columnDefinition = "varchar(20) not null")
    private String status;

    @Column(columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore
    private User user;

}