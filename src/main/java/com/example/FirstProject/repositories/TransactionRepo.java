package com.example.FirstProject.repositories;

import com.example.FirstProject.entities.Transaction;
import jakarta.websocket.server.PathParam;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction,Integer> {

    List<Transaction> findAllByUserIdOrderByTransactionDateDesc(int userId, Pageable pageable);
    List<Transaction> findAllByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
            int userId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("SELECT DISTINCT YEAR(t.transactionDate) FROM Transaction t WHERE t.user.id = :userId ")
    List<Integer> findDistinctYears(@PathParam("userId") int userId);
}
