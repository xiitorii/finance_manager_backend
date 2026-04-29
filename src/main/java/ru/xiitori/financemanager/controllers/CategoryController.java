package ru.xiitori.financemanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.xiitori.financemanager.model.dto.category.CategoryResponseDTO;
import ru.xiitori.financemanager.model.dto.category.CreateUpdateCategoryDTO;
import ru.xiitori.financemanager.model.entity.User;
import ru.xiitori.financemanager.services.CategoryService;

import java.util.Set;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Set<CategoryResponseDTO>> getCategories(
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        var categories = categoryService.getByUser(user);

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        var category = categoryService.getById(id, user);

        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @RequestBody @Valid CreateUpdateCategoryDTO request,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        var saved = categoryService.create(request, user);

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CreateUpdateCategoryDTO request,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        var updated = categoryService.update(id, request, user);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        categoryService.deleteById(id, user);

        return ResponseEntity.noContent().build();
    }
}
