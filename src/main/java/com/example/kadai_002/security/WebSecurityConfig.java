package com.example.kadai_002.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(authz -> authz
              .requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**").permitAll()
              .requestMatchers("/", "/signup/**", "/houses/**", "/reset").permitAll()
              .requestMatchers("/webhook/stripe").permitAll()
              .requestMatchers("/subscription/customer-portal").permitAll()
              .requestMatchers("/subscription", "/create-checkout-session").authenticated()
              .requestMatchers("/prime/**").hasRole("PRIME")
              .requestMatchers("/admin/**").hasRole("ADMIN")
              .anyRequest().authenticated()
          )
          .csrf(csrf -> csrf
              .ignoringRequestMatchers("/webhook/stripe")
          )
          .formLogin(form -> form
              .loginPage("/login")
              .loginProcessingUrl("/login")
              .defaultSuccessUrl("/?loggedIn")
              .failureUrl("/login?error")
              .permitAll()
          )
          .logout(logout -> logout
              .logoutSuccessUrl("/?loggedOut")
              .permitAll()
          );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}