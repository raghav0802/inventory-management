package com.example.inventory.controller;

import com.example.inventory.dto.LoginRequest;
import com.example.inventory.dto.SignupRequest;
import com.example.inventory.dto.UserDto;
import com.example.inventory.model.User;
import com.example.inventory.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.inventory.util.JwtUtil;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequest signupRequest) {
        UserDto userDto = userService.signup(signupRequest);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        var user = userService.getUserByUsername(loginRequest.getUsername());

        if (user.isEmpty() || !com.example.inventory.util.PasswordUtil
                .verifyPassword(loginRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        String token = JwtUtil.generateToken(user.get().getUsername());
        return ResponseEntity.ok(token);
    }
}

