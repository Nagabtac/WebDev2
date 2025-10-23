package myuniquesite.blerp.controller;

import myuniquesite.blerp.dto.AuthRequest; // You might need to create this DTO
import myuniquesite.blerp.service.UserService;
import myuniquesite.blerp.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller specifically for handling API authentication (JWT login/register).
 */
@RestController
@RequestMapping("/api") // Base path for API authentication endpoints
@CrossOrigin(origins = "*") // Allow requests from any origin (adjust for production)
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    // Spring Boot will automatically inject these beans if they exist
    public AuthController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Handles POST requests to /api/login.
     * Authenticates the user and returns a JWT token if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        try {
            // Attempt authentication using Spring Security's AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
            );

            // If authentication is successful, get user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Generate a JWT token for the authenticated user
            String jwt = jwtUtils.generateToken(userDetails);

            // Create the response body containing the token and a success message
            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            response.put("message", "Login successful");

            // Return 200 OK with the token
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // If authentication fails (e.g., bad credentials)
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid username or password");
            // Return 401 Unauthorized
            return ResponseEntity.status(401).body(error);
        }
    }

    /**
     * Handles POST requests to /api/register.
     * Registers a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthRequest authRequest) {
        try {
            // Call the user service to create the new user
            userService.registerUser(authRequest.username(), authRequest.password());

            // Create the response body for successful registration
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", authRequest.username());

            // Return 200 OK
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // If registration fails (e.g., username already exists)
            Map<String, String> error = new HashMap<>();
            error.put("message", "Registration failed: " + e.getMessage());
            // Return 400 Bad Request
            return ResponseEntity.badRequest().body(error);
        }
    }
}
