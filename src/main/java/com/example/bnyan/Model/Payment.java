package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(columnDefinition = "VARCHAR(100) UNIQUE NOT NULL")
        private String stripePaymentIntentId;

        @Column(columnDefinition = "INT NOT NULL")
        private Integer amount;

        @Column(columnDefinition = "INT NOT NULL")
        private Integer platformFee;

        @Column(columnDefinition = "INT NOT NULL")
        private Integer specialistAmount;

        @Column(columnDefinition = "VARCHAR(20) NOT NULL")
        @Pattern(
                regexp = "^(PENDING|PAID|FAILED|REFUNDED)$",
                message = "Status must be PENDING, PAID, FAILED, or REFUNDED"
        )
        private String status;

        @Column(columnDefinition = "TIMESTAMP NOT NULL", updatable = false)
        private LocalDateTime createdAt;


        // who paid
        @ManyToOne
        @JsonIgnore
        private Customer payer;

        // who receives (specialist USER)
        @ManyToOne
        @JsonIgnore
        private Specialist payee;


        @PrePersist
        void onCreate() {
                this.createdAt = LocalDateTime.now();
        }

}
