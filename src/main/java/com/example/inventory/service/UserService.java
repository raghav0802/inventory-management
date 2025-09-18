package com.example.inventory.service;

import com.example.inventory.model.User;
import com.example.inventory.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public Optional<User> getByUsername(String username) {
        return Optional.ofNullable(repo.findByUsername(username));
    }

    public User create(User user) {
        user.setCreatedAt(LocalDate.now());
        return repo.save(user);
    }
}
