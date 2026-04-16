package ru.xiitori.financemanager.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.xiitori.financemanager.model.dto.user.UserResponseDTO;
import ru.xiitori.financemanager.services.UserService;

import java.util.Set;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Set<UserResponseDTO>> getAllUsers() {
        var users = userService.getAll();

        return ResponseEntity.ok(users);
    }
}
