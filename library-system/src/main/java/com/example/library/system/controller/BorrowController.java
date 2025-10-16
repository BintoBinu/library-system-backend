package com.example.library.system.controller;

import com.example.library.system.entity.Borrow;
import com.example.library.system.service.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    // Borrow a book
    @PostMapping("/{bookId}")
    public ResponseEntity<Borrow> borrowBook(@PathVariable Long bookId, Authentication authentication) {
        Long userId = borrowService.getUserIdByUsername(authentication.getName());
        Borrow borrow = borrowService.borrowBook(userId, bookId);
        return ResponseEntity.ok(borrow);
    }

    // Return a book
    @PostMapping("/return/{borrowId}")
    public ResponseEntity<Borrow> returnBook(@PathVariable Long borrowId) {
        Borrow borrow = borrowService.returnBook(borrowId);
        return ResponseEntity.ok(borrow);
    }

    // List all borrows of logged-in student
    @GetMapping("/my")
    public ResponseEntity<List<Borrow>> getMyBorrows(Authentication authentication) {
        Long userId = borrowService.getUserIdByUsername(authentication.getName());
        List<Borrow> borrows = borrowService.getUserBorrows(userId);
        return ResponseEntity.ok(borrows);
    }

    // List all borrows (admin only)
    @GetMapping("/all")
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        List<Borrow> borrows = borrowService.getAllBorrows();
        return ResponseEntity.ok(borrows);
    }
}
