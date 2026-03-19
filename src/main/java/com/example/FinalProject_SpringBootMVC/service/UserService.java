package com.example.FinalProject_SpringBootMVC.service;

import com.example.FinalProject_SpringBootMVC.exception.ConflictException;
import com.example.FinalProject_SpringBootMVC.exception.ResourceNotFoundException;
import com.example.FinalProject_SpringBootMVC.exception.ValidationException;
import com.example.FinalProject_SpringBootMVC.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();
    private Long idCounter = 0L;

    public User createUser(User user) {
        if (user.getAge() < 5) {
            throw new ValidationException("Возраст пользователя не может быть меньше 5!");
        }
        var newUser = new User(
                ++idCounter,
                user.getName(),
                user.getEmail(),
                user.getAge(),
                new ArrayList<>()
        );
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User findUserById(Long userId) {
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", userId)
                );
    }

    public User updateUser(Long userId, User user) {
        var oldUser = users.get(userId);
        if (oldUser== null) {
            throw new ResourceNotFoundException("User", userId);
        }
        if (user.getAge() < 5) {
            throw new ValidationException("Возраст пользователя не может быть меньше 5!");
        }
        if (user.getEmail().equals(oldUser.getEmail())) {
            throw new ConflictException("У двух разных пользователей не может быть одинакового email!");
        }
        var updatedUser = new User(
                userId,
                user.getName(),
                user.getEmail(),
                user.getAge(),
                oldUser.getPets()
        );
        users.put(userId, updatedUser);
        return updatedUser;
    }

    public User findDeletedUser(Long userId) {
        var deletedUser = users.remove(userId);
        if (deletedUser == null) {
            throw new ResourceNotFoundException("User", userId);
        }
        return deletedUser;
    }

}