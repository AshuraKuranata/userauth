package com.userauth.controllers;
import com.userauth.models.User; // import User Model from Java

import java.util.HashMap;
import java.util.List; // Allows use of List command from Java; gives capability to make List adjustments (esp related to data from database)
import java.util.Map;

import com.userauth.daos.UserDao; // pulls the Data Access Object (DAO)

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Bcrypt Authentication - pulling from Bcrypt.java
import com.userauth.security.Bcrypt;

// Dependencies to pull that allow RESTful API connection to a front end
// Requries Spring Boot Web Dependency added in pom.xml
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

// import org.springframework.web.bind.annotation.CrossOrigin; // Fix for CORS policy - opens specific origin of IP address
// @CrossOrigin(origins = "http://localhost:5173") // No longer needed, created a WebConfig.java file to cover all CORS 

@RestController
@RequestMapping("/auth")
public class UserAuthController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @GetMapping("/user/{username}")
    public List<User> getUser(@PathVariable String username) {
        return userDao.getUser(username);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        List<User> users = userDao.getUser(user.getUsername());
        Map<String, String> response = new HashMap<>();
        
        if (users.isEmpty()) {
            response.put("message", "User Not Found.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String storedHash = users.get(0).getPassword();
        System.out.println("Stored hash from DB: " + storedHash);

        if (storedHash == null || storedHash.isBlank()) {
            response.put("message", "Stored password is invalid.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        if (Bcrypt.checkpw(user.getPassword(), storedHash)) {
            response.put("message", "Login Successful!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid Credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Adding PW hashing 
    @PostMapping("/user/create")
    public String create(@RequestBody User user) {
        String hashedPassword = Bcrypt.hashpw(user.getPassword(), Bcrypt.gensalt());
        userDao.createUser(user.getUsername(), hashedPassword);
        return "Created new record: Username: " + user.getUsername() + ".";
    }

    // Old Creation Route
    // @PostMapping("/user/create")
    // public String create(@RequestBody User user) {
    //     userDao.createUser(user.getUsername(), user.getPassword());
    //     return "Created new record: Username: " + user.getUsername() + ".";
    // }

    @PutMapping("/user/{username}/update")
    public String updateUser(@PathVariable String username, @RequestBody User user) {
        String hashedPassword = Bcrypt.hashpw(user.getPassword(), Bcrypt.gensalt());
        userDao.updateUser(username, hashedPassword);
        return "Updated user record: " + username;
    }

    @DeleteMapping("/user/{username}/delete")
    public String deletePerson(@PathVariable String username) {
        userDao.deleteUser(username);
        return "Deleted record: " + username;
    }
}

