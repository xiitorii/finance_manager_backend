package ru.xiitori.financemanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.xiitori.financemanager.model.dto.auth.RegistrationDTO;
import ru.xiitori.financemanager.services.UserService;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<Void> registration(
            @RequestBody @Valid RegistrationDTO registrationDTO
    ) throws AccessDeniedException {
        if (userService.existsByUsername(registrationDTO.username())) {
            throw new AccessDeniedException("User already exists");
        }

        userService.register(registrationDTO);

        return ResponseEntity
                .ok()
                .build();
    }
}
