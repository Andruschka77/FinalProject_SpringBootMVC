package com.example.FinalProject_SpringBootMVC.service;

import com.example.FinalProject_SpringBootMVC.model.User;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();
    private Long idCounter = 0L;

    public User createUser(User user) {
        var newUser = new User(
                ++idCounter,
                user.getName(),
                user.getEmail(),
                user.getAge()
        );
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User findUserById(Long userId) {
        return users.get(userId);
    }

    public User updateUser(Long userId, User user) {
        var updatedUser = new User(
                userId,
                user.getName(),
                user.getEmail(),
                user.getAge()
        );
        users.put(userId, updatedUser);
        return updatedUser;
    }

    public void deleteUser(Long userId) {
        users.remove(userId);
    }
}
