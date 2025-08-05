package com.example.FirstProject.services;

import com.example.FirstProject.entities.TransactionCategory;
import com.example.FirstProject.entities.User;
import com.example.FirstProject.repositories.TransactionCategoryRepo;
import com.example.FirstProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class TransactionCatService  {


    private static final Logger logger = Logger.getLogger(TransactionCatService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionCategoryRepo transactionCategoryRepo;

    public Optional<TransactionCategory> getTransCatById(int Id){
        return transactionCategoryRepo.findById(Id);
    }

    public TransactionCategory createTransactionCat(int userId, String categoryName, String categoryColour){
        logger.info("Creating transaction category with user: " + userId);

        //find the user with the userID
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) return null;

        TransactionCategory transactionCategory = new TransactionCategory();
        transactionCategory.setUser(user.get());
        transactionCategory.setCategoryName(categoryName);
        transactionCategory.setCategoryColour(categoryColour);

        return transactionCategoryRepo.save(transactionCategory);

    }

    public List<TransactionCategory> getTransactionCategoriesByUserId(int userId){
        logger.info("Getting all transaction category info for user: " + userId);

        return transactionCategoryRepo.findAllByUserId(userId);
    }

    public TransactionCategory updateTransactionCategoryById(int transactionCategoryId, String newCategory, String newCategoryColor){
        logger.info("Updating transaction category: " + transactionCategoryId);


        Optional<TransactionCategory> transactionCategoryOptional = transactionCategoryRepo.findById(transactionCategoryId);
        if(transactionCategoryOptional.isEmpty()){
            return null;
        }
        TransactionCategory updatedTransactionCat = transactionCategoryOptional.get();
        updatedTransactionCat.setCategoryName(newCategory);
        updatedTransactionCat.setCategoryColour(newCategoryColor);
        return transactionCategoryRepo.save(updatedTransactionCat);
    }

    public boolean deleteTransactionCategoryById(int transactionCategoryId){
        logger.info("Deleting transaction category: " + transactionCategoryId);

        Optional<TransactionCategory> transactionCategoryOptional = transactionCategoryRepo.findById(transactionCategoryId);

        if(transactionCategoryOptional.isEmpty()) return false;

        transactionCategoryRepo.delete(transactionCategoryOptional.get());

        return true;
    }
}
