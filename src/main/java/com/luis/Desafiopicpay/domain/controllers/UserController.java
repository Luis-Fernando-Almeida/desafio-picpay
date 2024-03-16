package com.luis.Desafiopicpay.domain.controllers;

import com.luis.Desafiopicpay.domain.user.User;
import com.luis.Desafiopicpay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        User obj = service.findUserById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user){
        User newUser = service.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
