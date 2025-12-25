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
public class UserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "type must be filled")
    @Pattern(regexp = "^(buy|rent|sell)$", message = "type must be buy or rent or sell")
    @Column(columnDefinition = "varchar(20) not null")
    private String type;

    @NotEmpty(message = "status must be filled")
    @Pattern(regexp = "^(pending|accepted|rejected)$", message = "status must be pending or accepted or rejected")
    @Column(columnDefinition = "varchar(20) not null")
    private String status;

    @Column(columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JsonIgnore
    private Built built;

}