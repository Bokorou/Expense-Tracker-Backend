package com.example.FirstProject.services;


import com.example.FirstProject.entities.User;
import com.example.FirstProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;


    //GET
    public Optional<User> getUserById(int userId){
        logger.info("getting the user by id: " + userId);
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email){
        logger.info("getting the user by email :" + email);
        return userRepository.findByEmail(email);
    }

    //POST

    public  User createUser(String name, String email, String password){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void deleteProduct(User user){
        userRepository.delete(user);
    }

}
