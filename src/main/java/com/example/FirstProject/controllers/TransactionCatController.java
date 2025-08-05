package com.example.FirstProject.controllers;


import com.example.FirstProject.entities.TransactionCategory;
import com.example.FirstProject.services.TransactionCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/transaction-category")
public class TransactionCatController {

    private static final Logger logger = Logger.getLogger(TransactionCatController.class.getName());

    @Autowired
    private TransactionCatService transactionCatService;

    @PostMapping
    public ResponseEntity<TransactionCategory> createTransCat(@RequestBody TransactionCategory transactionCategory){
        logger.info("Create Transaction category for: " + transactionCategory.getCategoryName());

        transactionCatService.createTransactionCat(
                transactionCategory.getUser().getId(),
                transactionCategory.getCategoryName(),
                transactionCategory.getCategoryColour()
        );

        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionCategory>> retrieveTransactionCat(@PathVariable int userId){
        logger.info("getting all transaction categories for: " + userId);
        List<TransactionCategory> transactionCategories = transactionCatService.getTransactionCategoriesByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(transactionCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionCategory> getTransactionCategoryById(@PathVariable int id){
        logger.info("getting transaction categories for: " + id);
        Optional<TransactionCategory> transactionCategoryOptional = transactionCatService.getTransCatById(id);
        if(transactionCategoryOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactionCategoryOptional.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<TransactionCategory> updateTransactionCat(@PathVariable int id,
                @RequestParam String newCategory, @RequestParam String newCategoryColour){

        TransactionCategory updatedTransactionCat = transactionCatService.updateTransactionCategoryById(id, newCategory, newCategoryColour);
        if(updatedTransactionCat == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedTransactionCat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionCategory> deleteTransactionCategory(@PathVariable int id) {


        if (!transactionCatService.deleteTransactionCategoryById(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
