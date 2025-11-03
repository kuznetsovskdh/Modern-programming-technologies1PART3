package com.example.products.service;

import com.example.products.model.User;
import com.example.products.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Регистрация нового пользователя
    public User registerUser(String username, String password, String role) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        User user = new User();
        user.setUsername(username);
        // Шифруем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(password));
        user.addRole(role);

        return userRepository.save(user);
    }

    // Поиск пользователя по имени
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));
    }

    // Проверка существования пользователя
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}