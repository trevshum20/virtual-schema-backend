package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
// For CORS
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private String corsAllowedOrigins = System.getenv("CORS_ALLOWED_ORIGINS");

    private String loginRedirectUrl = System.getenv("LOGIN_REDIRECT_URL");

    private String logoutRedirectUrl = System.getenv("LOGOUT_REDIRECT_URL");

    // If you have a DataSource bean for JDBC auth:
    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager jdbcUserDetailsManager() {
        JdbcUserDetailsManager jdbcManager = new JdbcUserDetailsManager(dataSource);
        jdbcManager.setUsersByUsernameQuery(
            "SELECT username, password, enabled FROM custom_user WHERE username = ?"
        );
        jdbcManager.setAuthoritiesByUsernameQuery(
            "SELECT username, role FROM custom_user WHERE username = ?"
        );
        return jdbcManager;
    }

    @Bean
    public DaoAuthenticationProvider authProvider(UserDetailsManager jdbcUserDetailsManager,
                                                  PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(jdbcUserDetailsManager);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // 1) The SecurityFilterChain that references .cors()
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, com.example.demo.JwtAuthenticationFilter jwtFilter) throws Exception {
        http
            // CORS still uses your existing corsConfigurationSource()
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Stateless API, no CSRF for JWT
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // No form login or basic auth when using JWT
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)

            // Authz rules: allow auth + health, protect everything else
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/health", "/actuator/health", "/error").permitAll()
                .anyRequest().authenticated()
            )

            // Install our JWT filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 2) The CorsConfigurationSource bean that configures allowed origins, etc.
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Change this list to your desired origins
        config.setAllowedOrigins(List.of(corsAllowedOrigins));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Accept-Encoding", "Accept-Language", "Connection", "Host", "Origin", "Referer", "User-Agent"));
        config.setAllowCredentials(true); // let cookies/session across domains

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
