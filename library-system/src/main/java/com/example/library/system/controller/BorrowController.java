package com.example.library.system.controller;

import com.example.library.system.entity.Borrow;
import com.example.library.system.service.BorrowService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@CrossOrigin(origins = "http://localhost:4200")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }
    
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/borrow")
    public Borrow borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return borrowService.borrowBook(userId, bookId);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/return/{borrowId}")
    public Borrow returnBook(@PathVariable Long borrowId) {
        return borrowService.returnBook(borrowId);
    }

    
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/history/{userId}")
    public List<Borrow> getBorrowHistory(@PathVariable Long userId) {
        return borrowService.getBorrowHistoryByUser(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<Borrow> getAllBorrows() {
        return borrowService.getAllBorrows();
    }
}
