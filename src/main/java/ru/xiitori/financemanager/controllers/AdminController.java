package ru.xiitori.financemanager.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.xiitori.financemanager.model.dto.user.UserResponseDTO;
import ru.xiitori.financemanager.services.UserService;

import java.util.Set;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('admin')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Set<UserResponseDTO>> getAllUsers() {
        var users = userService.getAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id
    ) {
        var user = userService.getById(id);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Void> changeUserStatus(
            @PathVariable Long id,
            @RequestParam(name = "locked", required = false) Boolean locked,
            @RequestParam(name = "enabled", required = false) Boolean enabled
    ) {
        if (locked != null) {
            userService.changeLock(id, locked);
        }

        if (enabled != null) {
            userService.changeEnabled(id, enabled);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
