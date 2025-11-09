package com.company.inventory.simple_inventory.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/login", "/error", "/", "/css/**", "/js/**", "/img/**").permitAll()

                        // Admin routes
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // User routes
                        .requestMatchers("/user/**").hasRole("USER")


                        .anyRequest().authenticated()
                )

                // Login setup
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email") //login with email
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                // Logout setup
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )


                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}