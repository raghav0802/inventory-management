package com.example.inventory.service;

import com.example.inventory.dto.SignupRequest;
import com.example.inventory.dto.UserDto;
import com.example.inventory.mapper.UserMapper;
import com.example.inventory.model.User;
import com.example.inventory.repository.UserRepository;
import com.example.inventory.util.PasswordUtil;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public UserDto signup(SignupRequest signupRequest) {
        String hashedPassword = PasswordUtil.hashPassword(signupRequest.getPassword());

        User user = User.builder()
                .username(signupRequest.getUsername())
                .password(hashedPassword) // store hash
                .role(signupRequest.getRole())
                .fullName(signupRequest.getFullName())
                .createdAt(LocalDate.from(LocalDateTime.now()))
                .build();

        User saved = repo.save(user);
        return UserMapper.toDto(saved);
    }

    public UserDto findByUsername(String username) {
        User user = repo.findByUsername(username);
        return user != null ? UserMapper.toDto(user) : null;
    }

    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(repo.findByUsername(username));
    }


    public List<UserDto> getAllUsers() {
        return repo.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(String id) {
        Optional<User> user = repo.findById(id);
        return user.isPresent() ? UserMapper.toDto(user.orElse(null)) : null;
    }
}
