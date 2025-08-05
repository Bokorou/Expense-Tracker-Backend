package com.example.FirstProject.controllers;

import com.example.FirstProject.entities.Transaction;
import com.example.FirstProject.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private static final Logger logger = Logger.getLogger(TransactionController.class.getName());

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/recent/user/{userId}")
    public ResponseEntity <List<Transaction>> getTransactionsByUserId(
            @PathVariable int userId,
            @RequestParam int startPage,
            @RequestParam int endPage,
            @RequestParam int size) {
        logger.info(("Getting transactions for userId: " + userId) + ", Page: (" + startPage + "," + endPage + ")");
        List<Transaction> recentTransactions = transactionService.getRecentTransactionByUserId(
                userId, startPage, endPage, size);

        return ResponseEntity.status(HttpStatus.OK).body(recentTransactions);
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity <List<Transaction>> getAllTransactionsByUseridAndYearOrMonth(
            @PathVariable int userid,
            @RequestParam int year,
            @RequestParam(required = false) Integer month){
        List<Transaction> transactionsList = null;
        if (month == null){
            transactionsList = transactionService.getAllTransactionsByUserIdAndYear(userid, year);
        }else{
            transactionsList = transactionService.getAllTransactionsByYearAndMonth(userid, year, month);
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactionsList);
    }

    @GetMapping("/years/{userId}")
    public ResponseEntity<List<Integer>> getDistinctTransactionYears(@PathVariable int userId){
        logger.info("Getting distinct years");

        List<Integer> yearList = transactionService.getDistinctTransactionYears(userId);

        return ResponseEntity .status(HttpStatus.OK).body(yearList);
    }

    @PostMapping
    public ResponseEntity <Transaction> createTransaction(@RequestBody Transaction transaction){
        logger.info("Creating Transaction");

        Transaction newTransaction = transactionService.createTransaction(transaction);

        if(newTransaction == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Transaction> deleteTransactionById(@PathVariable int transactionId){
        logger.info("Deleting transaction");
        transactionService.deleteTransactionById(transactionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction){
        logger.info("Updating ttransion with id " + transaction.getId());
        Transaction updatedTransaction = transactionService.editTransactionById(transaction);
        if(updatedTransaction == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
