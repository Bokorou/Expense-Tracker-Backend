package com.example.FirstProject.controllers;

//this class makes up the api that we use to communicate to our client, to the service layer which then communicates to our database.

import com.example.FirstProject.entities.User;
import com.example.FirstProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/user") //Used to define the url path for our controller. Helps map the HTTP request to specific handler methods in the controller. Sets up the routes for the API
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    /*
    @GetMapping
    public ResponseEntity<User> getUserById(@RequestParam int userId){
        logger.info("Getting User by ID: " + userId);
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
          else
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());

    }
     */

    @GetMapping
    public ResponseEntity<User> getUserByEmail(@RequestParam String email){
        logger.info("Getting Email by ID: " + email);
        Optional<User> userOptional = userService.getUserByEmail(email);
        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        else
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
    }

    @PostMapping("/login") //helps isolate which methods to call based on request from client
    public ResponseEntity<User> loginUser(@RequestParam String email, @RequestParam String password){

        Optional<User> userOptional = userService.getUserByEmail(email);
        if(userOptional.isEmpty()){
            //404 user not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //401 check if passwords don't match
        if(!password.equals(userOptional.get().getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Creating new user");

        User newUser = userService.createUser(user.getName(), user.getEmail(), user.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    //Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        User userToDelete = userService.getUserById(id)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        userService.deleteProduct(userToDelete);

        return ResponseEntity.ok("User deleted");
    }



}
