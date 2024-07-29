package com.elice.moduleuser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 인증과정 없이 요청 가능한 url
    String[] urlsToBePermittedAll = {"/hello", "/login", "/h2-console/**"};

    // 인증 과정이 필요하여
    // 인증 없이 요청한 경우 로그인 페이지로 리다이렉션 합니다.
    String[] urlsToBeAuthenticated = {"/logout"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(urlsToBePermittedAll).permitAll() // /hello URL에 대해 접근 허용
                        .anyRequest().authenticated() // 다른 요청은 인증 필요
                )
                .formLogin(withDefaults()) // 기본 로그인 페이지 사용
                .logout(withDefaults()); // 로그아웃도 허용

        return http.build();
    }
}
