package com.example.library.system.service;
import org.springframework.data.domain.Sort;
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

    
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    
    public List<User> findAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
