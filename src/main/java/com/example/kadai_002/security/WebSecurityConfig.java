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
        .authorizeHttpRequests((requests) -> requests
                // すべてのユーザーがアクセス可能
                .requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/", "/signup/**", "/houses", "/houses/{id}", "/houses/**","/stripe/webhook", "/reset").permitAll()
                
                // 有料会員のみアクセス可能
                .requestMatchers("/prime/**").hasRole("PRIME")

                // 管理者のみアクセス可能
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // その他のページは認証を要求
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")               // ログインページ
                .loginProcessingUrl("/login")      // ログインフォームの送信先
                .defaultSuccessUrl("/?loggedIn")   // ログイン成功時のリダイレクト先
                .failureUrl("/login?error")        // ログイン失敗時のリダイレクト先
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/?loggedOut")   // ログアウト時のリダイレクト先
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}