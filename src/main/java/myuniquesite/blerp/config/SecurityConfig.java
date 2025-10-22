package myuniquesite.blerp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authProvider);
    }

    /**
     * API Security Filter Chain (JWT-based, Stateless)
     * Higher priority (@Order(1)) - checked first
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**") // Only apply to /api/** endpoints
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/**").permitAll(); // Public auth endpoints
                    auth.requestMatchers("/api/public/**").permitAll(); // Other public API endpoints
                    auth.requestMatchers(HttpMethod.GET, "/api/cars/**").permitAll(); // Allow public read access
                    auth.requestMatchers(HttpMethod.POST, "/api/cars/**").authenticated(); // Require auth for create
                    auth.requestMatchers(HttpMethod.PUT, "/api/cars/**").authenticated(); // Require auth for update
                    auth.requestMatchers(HttpMethod.DELETE, "/api/cars/**").authenticated(); // Require auth for delete
                    //auth.requestMatchers("/api/admin/**").hasRole("ADMIN"); // Admin-only API
                    //auth.requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN"); // User/Admin API
                    auth.anyRequest().authenticated(); // All other API endpoints require authentication
                })
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .build();
    }

    /**
     * Web Security Filter Chain (Session-based)
     * Lower priority (@Order(2)) - checked after API filter
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**") // Apply to all other endpoints (web pages)
                .csrf(withDefaults()) // Enable CSRF for session-based endpoints
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/", "/login", "/register", "/public/**",
                            "/css/**", "/js/**", "/images/**").permitAll(); // Public web resources
                    auth.anyRequest().authenticated(); // All other web pages require authentication
                })
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/", true) // Redirect after successful login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // Session-based
                .build();
    }
}


