package com.example.products.repository;

import com.example.products.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Метод для поиска пользователя по имени
    Optional<User> findByUsername(String username);

    // Метод для проверки существования пользователя
    boolean existsByUsername(String username);
}