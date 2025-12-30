package com.example.bnyan.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "username must be filled")
    @Column(columnDefinition = "varchar(30) not null unique")
    private String username;

    // user name like Ahmed Ali
    @NotEmpty(message = "username must be filled")
    @Pattern(regexp = "^[A-Za-z]{3,20}( [A-Za-z]{3,20})+$",
            message = "fullName must contain only letters and a space between first and last name")
    @Column(columnDefinition = "varchar(40) not null")
    private String fullName;

    @NotEmpty(message = "password must be filled")
    @Column(columnDefinition = "varchar(255) not null")
    private String password;

    @NotEmpty(message = "email must be filled")
    @Email(message = "email must be valid")
    @Column(columnDefinition = "varchar(50) not null unique")
    private String email;

    @NotEmpty(message = "phoneNumber must be filled")
    @Pattern(regexp = "^05\\d{8}$", message = "phoneNumber must start with 05 and be 10 digits")
    @Column(columnDefinition = "varchar(10) not null")
    private String phoneNumber;

    @NotEmpty(message = "role must be filled")
    @Pattern(regexp = "^(USER|ADMIN|SPECIALIST)$", message = "role must be user or admin or specialist")
    @Column(columnDefinition = "varchar(20) not null")
    private String role;

    @Column(columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Customer customer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Specialist specialist;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ProjectManager projectManager;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}