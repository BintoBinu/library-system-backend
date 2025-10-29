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

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowService(BorrowRepository borrowRepository,
                         BookRepository bookRepository,
                         UserRepository userRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // ✅ Borrow a book
    public Borrow borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getStock() <= 0) {
            throw new RuntimeException("Book not available for borrowing");
        }

        // Update stock
        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        // Create borrow record
        Borrow borrow = new Borrow(user, book);
        return borrowRepository.save(borrow);
    }

    // ✅ Return a book
    public Borrow returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        if (borrow.isReturned()) {
            throw new RuntimeException("Book already returned");
        }

        Book book = borrow.getBook();
        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        borrow.setReturned(true);
        borrow.setReturnDate(LocalDateTime.now());

        return borrowRepository.save(borrow);
    }

    // ✅ Borrow history of a student
    public List<Borrow> getBorrowHistoryByUser(Long userId) {
        return borrowRepository.findByStudentId(userId);
    }

    // ✅ All borrow records (for admin)
    public List<Borrow> getAllBorrows() {
        return borrowRepository.findAll();
    }
}
