package com.example.bnyan.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@org.springframework.context.annotation.Configuration
@EnableWebSecurity

public class Configuration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.csrf(csrf -> csrf.disable() )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                        .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/user/register-customer").permitAll()
                                            // ===== USER =====
                                        .requestMatchers("/api/v1/user/register-customer").permitAll()

                                        .requestMatchers(
                                                "/api/v1/user/get",
                                                "/api/v1/user/delete/**",
                                                "/api/v1/user/get-by-id/**",
                                                "/api/v1/user/get-by-username/**",
                                                "/api/v1/user/get-by-role/**"
                                        ).hasAuthority("ADMIN")

                                        .requestMatchers("/api/v1/user/update")
                                        .hasAnyAuthority("USER", "SPECIALIST", "ADMIN")

                                                // ===== BUILT =====
                                        .requestMatchers(
                                                "/api/v1/built/add",
                                                "/api/v1/built/update/**",
                                                "/api/v1/built/delete/**",
                                                "/api/v1/built/get-my-builts"
                                        ).hasAnyAuthority("USER", "SPECIALIST", "ADMIN")

                                        .requestMatchers(
                                                "/api/v1/built/get",
                                                "/api/v1/built/get-by-id/**",
                                                "/api/v1/built/get-by-status/**",
                                                "/api/v1/built/get-by-price-less-than-or-equal/**",
                                                "/api/v1/built/get-by-location/**"
                                        ).permitAll()

                                                // ===== USER REQUEST =====
                                        .requestMatchers("/api/v1/user-request/add/**").hasAuthority("USER")

                                        .requestMatchers("/api/v1/user-request/delete/**").hasAuthority("USER")

                                        .requestMatchers(
                                                "/api/v1/user-request/accept/**",
                                                "/api/v1/user-request/reject/**"
                                        ).hasAnyAuthority("SPECIALIST", "ADMIN")

                                // CUSTOMER
                                .requestMatchers(
                                        "/api/v1/customer/get-by-id",
                                        "/api/v1/customer/get-properties",
                                        "/api/v1/customer/on-going-projects",
                                        "/api/v1/customer/completed-projects",
                                        "/api/v1/customer/ask-ai"
                                ).hasAuthority("USER")

                                //Meeting
                                .requestMatchers("/api/v1/meeting/add").hasAuthority("USER")

                                .requestMatchers("/api/v1/meeting/get",
                                        "/api/v1/meeting/project/**",
                                        "/api/v1/meeting/delete/**").hasAuthority("ADMIN")

                                .anyRequest().authenticated()
                        )

                                .logout(logout -> logout.logoutUrl("/api/v1/user/logout").deleteCookies("JSESSIONID").invalidateHttpSession(true))
                .httpBasic(basic -> {}).build();
    }
}
