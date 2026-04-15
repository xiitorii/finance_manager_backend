package ru.xiitori.financemanager.model.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationDTO(
        @NotNull(message = "username is required")
        String username,

        @NotNull(message = "password is required")
        @Size(min = 8, message = "minimum length of the password must be at least {min}")
        String password,

        @NotNull(message = "repeat password is required")
        String repeatPassword
) {
}
