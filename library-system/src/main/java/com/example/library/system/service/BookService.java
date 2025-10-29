package com.example.library.system.service;

import com.example.library.system.entity.Book;
import com.example.library.system.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ✅ Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // ✅ Add a new book
    public Book addBook(Book book) {
        if (book.getStock() < 0) {
            throw new RuntimeException("Stock cannot be negative");
        }
        return bookRepository.save(book);
    }

    // ✅ Update an existing book
    public Book updateBook(Long id, Book updatedBook) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        existing.setTitle(updatedBook.getTitle());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setStock(updatedBook.getStock());

        return bookRepository.save(existing);
    }

    // ✅ Delete a book
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(id);
    }
}
