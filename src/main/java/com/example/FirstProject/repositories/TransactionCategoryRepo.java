package com.example.FirstProject.repositories;

import com.example.FirstProject.entities.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface TransactionCategoryRepo extends JpaRepository<TransactionCategory, Integer> {

    List<TransactionCategory> findAllByUserId(int userId);

    Optional<TransactionCategory> findById(int Id);
}
