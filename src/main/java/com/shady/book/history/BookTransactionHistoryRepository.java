package com.shady.book.history;

import com.shady.book.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Integer> {

    @Query("""
        SELECT  history
        FROM BookTransactionHistory history
        where history.user.id = :userId 
    
   """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);
}
