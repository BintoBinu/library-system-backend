package com.example.library.system.controller;

import com.example.library.system.entity.Borrow;
import com.example.library.system.entity.User;
import com.example.library.system.service.BorrowService;
import com.example.library.system.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final UserService userService;
    private final BorrowService borrowService;

    public AdminController(UserService userService, BorrowService borrowService) {
        this.userService = userService;
        this.borrowService = borrowService;
    }

    // ✅ Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    // ✅ Get all borrow records (for admin)
    @GetMapping("/borrows")
    public List<Borrow> getAllBorrowRecords() {
        return borrowService.getAllBorrows();
    }

    // ✅ Get detailed borrow info per user — what books they borrowed & returned
    @GetMapping("/users/borrow-details")
    public List<Map<String, Object>> getAllUserBorrowDetails() {
        List<User> users = userService.findAllUsers();
        List<Map<String, Object>> result = new ArrayList<>();

        for (User user : users) {
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("userId", user.getId());
            userDetails.put("username", user.getUsername());
            userDetails.put("role", user.getRole());

            // Fetch borrow history for each user
            List<Borrow> borrows = borrowService.getBorrowHistoryByUser(user.getId());

            // Prepare book info list
            List<Map<String, Object>> borrowInfo = new ArrayList<>();
            for (Borrow borrow : borrows) {
                Map<String, Object> info = new HashMap<>();
                info.put("bookId", borrow.getBook().getId());
                info.put("bookTitle", borrow.getBook().getTitle());
                info.put("borrowDate", borrow.getBorrowDate());
                info.put("returnDate", borrow.getReturnDate());
                info.put("isReturned", borrow.getReturnDate() != null);
                borrowInfo.add(info);
            }

            userDetails.put("borrowedBooks", borrowInfo);
            result.add(userDetails);
        }

        return result;
    }
}
