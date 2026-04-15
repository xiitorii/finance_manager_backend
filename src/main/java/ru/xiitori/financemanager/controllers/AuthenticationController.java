package ru.xiitori.financemanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.xiitori.financemanager.model.dto.auth.LoginDTO;
import ru.xiitori.financemanager.model.dto.auth.RegistrationDTO;
import ru.xiitori.financemanager.services.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/registration")
    public ResponseEntity<Void> registration(
            @RequestBody @Valid RegistrationDTO registrationDTO
    ) {
        userService.register(registrationDTO);

        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginDTO dto
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.username(),
                        dto.password()
                )
        );

        return ResponseEntity
                .ok()
                .build();
    }
}
