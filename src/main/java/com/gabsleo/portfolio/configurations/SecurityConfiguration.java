package com.gabsleo.portfolio.configurations;

import com.gabsleo.portfolio.security.AccessDeniedHandler;
import com.gabsleo.portfolio.security.AuthenticationEntrypoint;
import com.gabsleo.portfolio.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.core.Ordered;
import java.util.Arrays;

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
                .requestMatchers("/api/v1", "/api/v1/auth/login", "/api/v1/emails", "/api/v1/projects/**")
                .permitAll()
                .requestMatchers(
                        "/api/v1/admin/**",
                        "/api/v1/auth/register",
                        "/api/v1/auth/hello-adm",
                        "/api/v1/projects/**").hasAnyAuthority("ADMIN")
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

    @Bean
    public CorsFilter corsFilter(){
        var config = new CorsConfiguration();
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        var corsFilter = new CorsFilter(source);
            var filter = new FilterRegistrationBean<CorsFilter>(corsFilter);
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return corsFilter;
    }

}
