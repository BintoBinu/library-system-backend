package com.example.library.system.controller;

import com.example.library.system.service.BorrowService;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final BorrowService borrowService;
    private final SessionRegistry sessionRegistry;

    public AdminController(BorrowService borrowService, SessionRegistry sessionRegistry) {
        this.borrowService = borrowService;
        this.sessionRegistry = sessionRegistry;
    }

   
    @GetMapping("/logged-in-users")
    public List<String> getLoggedInUsers() {
        return sessionRegistry.getAllPrincipals().stream()
                .map(Object::toString) // usually the username
                .collect(Collectors.toList());
    }

   
    @GetMapping("/borrow-count")
    public Map<String, Long> getBorrowCountPerUser() {
        return borrowService.getBorrowCountPerUser();
    }

    
    @GetMapping("/borrow-report")
    public Map<String, List<String>> getUserBorrowDetails() {
        return borrowService.getUserBorrowDetails();
    }
}
