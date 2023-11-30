package com.gabsleo.portfolio.configurations;

import com.gabsleo.portfolio.security.AccessDeniedHandler;
import com.gabsleo.portfolio.security.AuthenticationEntrypoint;
import com.gabsleo.portfolio.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@SuppressWarnings("removal")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntrypoint authenticationEntrypoint;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfiguration(AccessDeniedHandler accessDeniedHandler, AuthenticationEntrypoint authenticationEntrypoint, AuthenticationProvider authenticationProvider, JwtAuthFilter jwtAuthenticationFilter) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntrypoint = authenticationEntrypoint;
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1", "/api/v1/auth/login")
                .permitAll()
                .requestMatchers("/api/v1/admin/**", "/api/v1/auth/register", "/api/v1/auth/hello-adm", "/api/v1/projects/**").hasAnyAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntrypoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }



}
