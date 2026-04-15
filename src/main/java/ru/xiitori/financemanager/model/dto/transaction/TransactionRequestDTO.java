package ru.xiitori.financemanager.model.dto.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        @NotNull(message = "Transaction Amount is required.")
        @Positive(message = "The value can't less then Zero")
        BigDecimal amount,

        @NotNull(message = "Type of transaction should not empty")
        @Pattern(regexp = "^(INCOME|EXPENSE)$", message = "Type must be either INCOME or EXPENSE")
        String type,

        @Size(max = 500, message = "Description cannot exceed {max} characters")
        String description
) {
}
