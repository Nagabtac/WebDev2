package myuniquesite.blerp.controller;

import myuniquesite.blerp.dto.AuthRequest;
import myuniquesite.blerp.dto.AuthResponse;
import myuniquesite.blerp.service.UserService;
import myuniquesite.blerp.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // login.html
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Object());
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
        return "redirect:/login";
    }

    // JWT Authentication Endpoints
    @GetMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<?> loginInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("message", "Use POST method with JSON body containing username and password");
        info.put("example", "{\"username\":\"testuser\",\"password\":\"testpass\"}");
        return ResponseEntity.ok(info);
    }

    @PostMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails);

            AuthResponse response = new AuthResponse(
                jwt, 
                authRequest.username(), 
                "Login successful", 
                System.currentTimeMillis() + 86400000L // 24 hours from now
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthRequest authRequest) {
        try {
            userService.registerUser(authRequest.username(), authRequest.password());

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", authRequest.username());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/api/auth/validate")
    @ResponseBody
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtils.validateToken(token)) {
                    String username = jwtUtils.extractUsername(token);
                    Map<String, Object> response = new HashMap<>();
                    response.put("valid", true);
                    response.put("username", username);
                    return ResponseEntity.ok(response);
                }
            }
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
