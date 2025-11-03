package com.example.products.controller;

import com.example.products.model.Product;
import com.example.products.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления товарами
 * Некоторые методы доступны только для ADMIN
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Главная страница - список товаров
     * Доступна всем авторизованным пользователям
     */
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("product", new Product());
        return "products";
    }

    /**
     * Добавление нового товара
     * Только для ADMIN
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            return "products";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    /**
     * Форма редактирования товара
     * Только для ADMIN
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        model.addAttribute("product", product);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("editMode", true);
        return "products";
    }

    /**
     * Обновление товара
     * Только для ADMIN
     */
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("editMode", true);
            return "products";
        }
        productService.updateProduct(id, product);
        return "redirect:/products";
    }

    /**
     * Удаление товара
     * Только для ADMIN
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}