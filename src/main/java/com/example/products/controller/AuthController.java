package com.example.products.controller;

import com.example.products.model.User;
import com.example.products.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для обработки регистрации и входа
 */
@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Главная страница - перенаправление на продукты
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    /**
     * Страница входа
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "registered", required = false) String registered,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Неверное имя пользователя или пароль");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Вы успешно вышли из системы");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Регистрация успешна! Войдите в систему.");
        }
        return "login";
    }

    /**
     * Страница регистрации (GET)
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Обработка регистрации (POST)
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam(value = "role", defaultValue = "USER") String role,
                               Model model) {

        // Базовая валидация
        if (username == null || username.trim().length() < 3) {
            model.addAttribute("errorMessage", "Имя пользователя должно быть минимум 3 символа");
            model.addAttribute("user", new User());
            return "register";
        }

        if (password == null || password.length() < 4) {
            model.addAttribute("errorMessage", "Пароль должен быть минимум 4 символа");
            model.addAttribute("user", new User());
            return "register";
        }

        // Проверяем, существует ли пользователь
        if (userService.existsByUsername(username)) {
            model.addAttribute("errorMessage", "Пользователь с таким именем уже существует");
            User user = new User();
            user.setUsername(username);
            model.addAttribute("user", user);
            return "register";
        }

        try {
            // Регистрируем только как USER
            userService.registerUser(username, password, "USER");
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка регистрации: " + e.getMessage());
            User user = new User();
            user.setUsername(username);
            model.addAttribute("user", user);
            return "register";
        }
    }
}