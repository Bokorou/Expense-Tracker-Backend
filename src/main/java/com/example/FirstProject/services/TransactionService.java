package com.example.FirstProject.services;

import com.example.FirstProject.entities.Transaction;
import com.example.FirstProject.entities.TransactionCategory;
import com.example.FirstProject.entities.User;
import com.example.FirstProject.repositories.TransactionCategoryRepo;
import com.example.FirstProject.repositories.TransactionRepo;
import com.example.FirstProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class TransactionService {

    private static final Logger logger = Logger.getLogger(TransactionService.class.getName());

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionCategoryRepo transactionCategoryRepo;

    @Autowired
    private UserRepository userRepository;

    public Transaction createTransaction(Transaction transaction){
        logger.info("Creating transaction");

        Optional<TransactionCategory> transactionCategoryOptional = Optional.empty();
        if(transaction.getTransactionCategory() != null){
            transactionCategoryOptional = transactionCategoryRepo.findById(
                    transaction.getTransactionCategory().getId()
            );
        }

        User user = userRepository.findById(transaction.getUser().getId()).get();

        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionCategory(
                transactionCategoryOptional.isEmpty() ? null : transactionCategoryOptional.get()
        );
        newTransaction.setUser(user);
        newTransaction.setTransactionName(transaction.getTransactionName());
        newTransaction.setTransactionDate(transaction.getTransactionDate());
        newTransaction.setTransactionAmount(transaction.getTransactionAmount());
        newTransaction.setTransactionType(transaction.getTransactionType());

        return transactionRepo.save(newTransaction);
    }

    public List<Transaction> getRecentTransactionByUserId(int userid, int startPage, int endPage, int size){
        logger.info("getting most recent transaction for " + userid);

        List<Transaction> combinedResult = new ArrayList<>();
        for(int page = startPage; page <= endPage; page++){
            Pageable pageable = PageRequest.of(page,size);
            List<Transaction> pageResults = transactionRepo.findAllByUserIdOrderByTransactionDateDesc(
                    userid,
                    pageable
            );
            combinedResult.addAll(pageResults);
        }
        return  combinedResult;
    }

    public List<Transaction> getAllTransactionsByUserIdAndYear(int userid, int year){
        logger.info("Getting all transaction in year: " + year + " for user: " + userid);
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return transactionRepo.findAllByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(userid,startDate,endDate);
    }

    public List<Integer> getDistinctTransactionYears(int userId){
        return transactionRepo.findDistinctYears(userId);
    }

    public  List<Transaction> getAllTransactionsByYearAndMonth(int userid, int year, int month){
        logger.info("Getting all transaction in year: " + year + " for user: " + userid);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth());
        return transactionRepo.findAllByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
                userid,startDate,endDate);
    }

    public void deleteTransactionById(int transactionCategoryId){
        logger.info("Deleting transaction category: " + transactionCategoryId);

        Optional<Transaction> transactionOptional = transactionRepo.findById(transactionCategoryId);

        if(transactionOptional.isEmpty()) return;

        transactionRepo.delete(transactionOptional.get());

    }

    public Transaction editTransactionById(Transaction transaction){
        logger.info("Updating transaction with id: " + transaction.getId());
        Optional<Transaction> transactionOptional = transactionRepo.findById(transaction.getId());

        if(transactionOptional.isEmpty())return null;
        return transactionRepo.save(transaction);
    }
}


















