package myuniquesite.blerp.config;

import myuniquesite.blerp.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Completely disable CSRF for all requests
            .csrf(csrf -> csrf.disable())
            
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // ✅ API endpoints are public
                .requestMatchers("/api/**").permitAll()
                
                // ✅ Index page is public
                .requestMatchers("/", "/index").permitAll()
                
                // Public web pages/resources (login/register only)
                .requestMatchers("/login", "/register", "/css/**").permitAll()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Web login for website only
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            
            // Logout for website only
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            
            // Use UserDetailsService directly (modern approach)
            .userDetailsService(userDetailsService);

        return http.build();
    }
}