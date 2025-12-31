package com.example.bnyan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<UserRequest> userRequests;

   @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "customer")
    private List<Land> lands;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<BuildRequest> buildRequests;

    @OneToMany(mappedBy = "customer")
    private Set<Project>projects;


}