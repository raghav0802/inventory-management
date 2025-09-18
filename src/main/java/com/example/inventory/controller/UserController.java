package com.example.inventory.controller;

import com.example.inventory.model.User;
import com.example.inventory.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return service.create(user);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return service.getByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}

