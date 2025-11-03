package com.example.products.config;

import com.example.products.model.User;
import com.example.products.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Класс для инициализации тестовых данных при запуске приложения
 * Создает пользователей ADMIN и USER, если они еще не существуют
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Создаем администратора, если его нет
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.addRole("ADMIN");
            userRepository.save(admin);
            System.out.println("✅ Создан пользователь ADMIN: admin / admin");
        }

        // Создаем обычного пользователя, если его нет
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.addRole("USER");
            userRepository.save(user);
            System.out.println("✅ Создан пользователь USER: user / user");
        }

        System.out.println("=================================================");
        System.out.println("📝 ТЕСТОВЫЕ АККАУНТЫ:");
        System.out.println("   Администратор - admin:admin");
        System.out.println("   Пользователь - user:user");
        System.out.println("=================================================");
    }
}
