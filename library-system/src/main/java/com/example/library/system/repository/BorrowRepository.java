package com.example.library.system.repository;

import com.example.library.system.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    // Find all borrow records for a given student
    List<Borrow> findByStudentId(Long studentId);

    // Optional: Find active borrows (not yet returned)
    List<Borrow> findByStudentIdAndReturnedFalse(Long studentId);
}
