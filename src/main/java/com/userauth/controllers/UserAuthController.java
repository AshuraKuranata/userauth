package com.userauth.controllers;
import com.userauth.models.User; // import User Model from Java
import java.util.List; // Allows use of List command from Java; gives capability to make List adjustments (esp related to data from database)

import com.userauth.daos.UserDao; // pulls the Data Access Object (DAO)

import org.springframework.beans.factory.annotation.Autowired;

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

    @PostMapping("/user/create")
    public String create(@RequestBody User user) {
        userDao.createUser(user.getUsername(), user.getPassword());
        return "Created new record: Username: " + user.getUsername() + ".";
    }

    @PutMapping("/user/{username}/update")
    public String updateUser(@PathVariable String username, @RequestBody User user) {
        userDao.updateUser(username, user.getPassword());
        return "Updated user record: " + username;
    }

    @DeleteMapping("/user/{username}/delete")
    public String deletePerson(@PathVariable String username) {
        userDao.deleteUser(username);
        return "Deleted record: " + username;
    }
}

