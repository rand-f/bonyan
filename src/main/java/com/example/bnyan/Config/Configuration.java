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
                                        .requestMatchers("/api/v1/user/register-customer","/api/v1/specialist/register","/api/v1/project-manager/register").permitAll()

                                        .requestMatchers(
                                                "/api/v1/user/get",
                                                "/api/v1/user/delete/**",
                                                "/api/v1/user/get-by-id/**",
                                                "/api/v1/user/get-by-username/**",
                                                "/api/v1/user/get-by-role/**",
                                                "/api/v1/domain/get",
                                                "/api/v1/specialist/get",
                                                "/api/v1/specialist/delete/{specialist_id}",
                                                "/api/v1/project/get",
                                                "/api/v1/specialist-request/get",
                                                "/api/v1/project-manager/delete/{manager_id}"
                                        ).hasAuthority("ADMIN")

                                        .requestMatchers("/api/v1/user/update")
                                        .hasAnyAuthority("USER", "SPECIALIST", "ADMIN")

                                                // ===== BUILT =====
                                        .requestMatchers(
                                                "/api/v1/built/add",
                                                "/api/v1/built/update/**",
                                                "/api/v1/built/delete/**",
                                                "/api/v1/built/get-my-builts",
                                                "/api/v1/specialist/get/arch-eng",
                                                "/api/v1/specialist/get/civil-eng",
                                                "/api/v1/specialist/get/designer",
                                                "/api/v1/specialist/get/elect-eng",
                                                "/api/v1/specialist/get/gen-cont",
                                                "/api/v1/specialist/get/project-manager"
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
                                                "/api/v1/user-request/reject/**",
                                                "/api/v1/specialist/update/{spec_id}"
                                        ).hasAnyAuthority("SPECIALIST", "ADMIN")

                                // CUSTOMER
                                .requestMatchers("/api/v1/customer/register-customer").permitAll()

                                .requestMatchers(
                                        "/api/v1/customer/get-by-id",
                                        "/api/v1/customer/get-properties",
                                        "/api/v1/customer/on-going-projects",
                                        "/api/v1/customer/completed-projects",
                                        "/api/v1/customer/ask-ai",
                                        "/api/v1/project/get-my-projects",
                                        "/api/v1/project/add/{request_id}",
                                        "/api/v1/project/budget/{project_id}",
                                        "/api/v1/project/time-prediction/{project_id}",
                                        "/api/v1/project/update/{project_id}",
                                        "/api/v1/project/delete/{project_id}",
                                        "/api/v1/project/generate-image/{project_id}",
                                        "/api/v1/specialist-request/add/{project_id}/{specialist_id}",
                                        "/api/v1/specialist-request/add-specialist/{project_id}/{spec_id}",
                                        "/api/v1/specialist-request/add-manager/{project_id}/{manager_id}",
                                        "/api/v1/specialist-request/update/{request_id}",
                                        "/api/v1/specialist-request/delete/{request_id}"
                                ).hasAuthority("USER")

                                .requestMatchers(
                                        "/api/v1/domain/get-specialist",
                                        "/api/v1/domain/add",
                                        "/api/v1/domain/update/{domain_id}",
                                        "/api/v1/domain/delete/{domain_id}",
                                        "/api/v1/specialist/register",
                                        "/api/v1/specialist/assign-domain/{domain_id}",
                                        "/api/v1/specialist/accept-request/{request_id}",
                                        "/api/v1/specialist/reject-request/{request_id}",
                                        "/api/v1/specialist-request/reject/{request_id}",
                                        "/api/v1/specialist-request/accept/{request_id}",
                                        "/api/v1/project-manager/accept-request/{request_id}",
                                        "/api/v1/project-manager/reject-request/{request_id}"
                                ).hasAuthority("SPECIALIST")

                                .requestMatchers(
                                        "/api/v1/project/working-on-project/{project_id}",
                                        "/api/v1/project-manager/get"
                                ).hasAnyAuthority("USER", "ADMIN")

                                //Meeting
                                .requestMatchers("/api/v1/meeting/add").hasAuthority("USER")

                                .requestMatchers("/api/v1/meeting/get",
                                        "/api/v1/meeting/project/**",
                                        "/api/v1/meeting/delete/**").hasAuthority("ADMIN")

                                // ===== TASK =====
                                .requestMatchers("/api/v1/task/add",
                                        "/api/v1/task/update/**").hasAuthority("SPECIALIST")
                                .requestMatchers("/api/v1/task/get-by-project/**",
                                        "/api/v1/task/get-by-status/**",
                                        "/api/v1/task/get-due-today",
                                        "/api/v1/task/get-overdue",
                                        "/api/v1/task/get-upcoming",
                                        "/api/v1/task/get-by-date-range",
                                        "/api/v1/task/get-by-manager/**",
                                        "/api/v1/task/get-completed",
                                        "/api/v1/task/get-by-id/**").hasAnyAuthority("USER", "SPECIALIST")
                                .requestMatchers("/api/v1/task/delete/**").hasAuthority("ADMIN")

                                // ===== REVIEW =====
                                .requestMatchers("/api/v1/review/add").hasAuthority("USER")
                                .requestMatchers("/api/v1/review/get",
                                        "/api/v1/review/specialist/**").hasAnyAuthority("USER", "SPECIALIST")
                                .requestMatchers("/api/v1/review/delete/**").hasAuthority("ADMIN")

                                // ===== LAND =====
                                .requestMatchers("/api/v1/land/add",
                                        "/api/v1/land/update/**",
                                        "/api/v1/land/delete/**").hasAuthority("USER")
                                .requestMatchers("/api/v1/land/get",
                                        "/api/v1/land/get-by-id/**").hasAnyAuthority("USER", "ADMIN")

                                // ===== BUILD REQUEST =====
                                .requestMatchers("/api/v1/build-request/add").hasAuthority("USER")
                                .requestMatchers("/api/v1/build-request/approve/**",
                                        "/api/v1/build-request/reject/**").hasAnyAuthority("SPECIALIST", "ADMIN")
                                .requestMatchers("/api/v1/build-request/get",
                                        "/api/v1/build-request/get-by-id/**").hasAnyAuthority("USER", "ADMIN")


                                .anyRequest().authenticated()
                        )

                                .logout(logout -> logout.logoutUrl("/api/v1/user/logout").deleteCookies("JSESSIONID").invalidateHttpSession(true))
                .httpBasic(basic -> {}).build();
    }
}
