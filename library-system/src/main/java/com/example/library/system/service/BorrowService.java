package com.example.library.system.service;

import com.example.library.system.entity.Book;
import com.example.library.system.entity.Borrow;
import com.example.library.system.entity.User;
import com.example.library.system.repository.BookRepository;
import com.example.library.system.repository.BorrowRepository;
import com.example.library.system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public BorrowService(BorrowRepository borrowRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // Borrow a book
    public Borrow borrowBook(Long userId, Long bookId) {
        User user = getUserById(userId);
        Book book = getBookById(bookId);

        if (book.getStock() <= 0) {
            throw new IllegalStateException("Book '" + book.getTitle() + "' is out of stock");
        }

        // Decrease stock
        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(LocalDateTime.now());
        borrow.setReturnDate(null);

        return borrowRepository.save(borrow);
    }

    // Return a book
    public Borrow returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow not found with ID: " + borrowId));

        if (borrow.getReturnDate() != null) {
            throw new IllegalStateException("Book has already been returned");
        }

        borrow.setReturnDate(LocalDateTime.now());

        Book book = borrow.getBook();
        book.setStock(book.getStock() + 1); // Increase stock
        bookRepository.save(book);

        return borrowRepository.save(borrow);
    }

    // Get all borrows of a user
    public List<Borrow> getUserBorrows(Long userId) {
        User user = getUserById(userId);
        return borrowRepository.findByUser(user);
    }

    // Get all borrows (admin)
    public List<Borrow> getAllBorrows() {
        return borrowRepository.findAll();
    }

    // Admin report: number of books borrowed per user
    public Map<String, Long> getBorrowCountPerUser() {
        List<Borrow> allBorrows = borrowRepository.findAll();
        return allBorrows.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getUser().getUsername(),
                        Collectors.counting()
                ));
    }

    // Admin report: detailed borrow info per user
    public Map<String, List<String>> getUserBorrowDetails() {
        List<Borrow> allBorrows = borrowRepository.findAll();
        return allBorrows.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getUser().getUsername(),
                        Collectors.mapping(
                                b -> b.getBook().getTitle() +
                                        " (Borrowed: " + b.getBorrowDate() +
                                        ", Returned: " + (b.getReturnDate() != null ? b.getReturnDate() : "Not yet") + ")",
                                Collectors.toList()
                        )
                ));
    }

    // Helper methods
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));
    }

    // Optional: Get user ID by username (for controller usage)
    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return user.getId();
    }
}
