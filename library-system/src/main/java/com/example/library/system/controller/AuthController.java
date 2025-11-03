package com.example.library.system.controller;

import com.example.library.system.config.JwtUtil;
import com.example.library.system.dto.LoginRequestDTO;
import com.example.library.system.dto.UserDTO;
import com.example.library.system.entity.User;
import com.example.library.system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user.getUsername(), user.getPassword(), user.getRole());
            return ResponseEntity.ok(new UserDTO(savedUser.getUsername(), savedUser.getRole()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        return userService.login(request.getUsername(), request.getPassword())
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

                    Map<String, Object> response = new HashMap<>();
                    response.put("userId", user.getId());
                    response.put("username", user.getUsername());
                    response.put("role", user.getRole());
                    response.put("token", token);

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid username or password")));
    }
}
