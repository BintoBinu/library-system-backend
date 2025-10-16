package com.example.library.system.repository;

import com.example.library.system.entity.Borrow;
import com.example.library.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    List<Borrow> findByUser(User user);
}
