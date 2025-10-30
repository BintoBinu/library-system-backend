package com.example.library.system.service;

import com.example.library.system.entity.User;
import com.example.library.system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //  Register a new user (ADMIN or STUDENT)
    public User register(String username, String password, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("STUDENT")) {
            throw new RuntimeException("Invalid role. Use ADMIN or STUDENT");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role.toUpperCase());

        return userRepository.save(user);
    }

    //  Login validation
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    // Fetch user by ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    //  List all users (for admin)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
