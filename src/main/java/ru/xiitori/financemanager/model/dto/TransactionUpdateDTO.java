package ru.xiitori.financemanager.model.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionUpdateDTO(
        @Positive(message = "The value can't less then Zero")
        BigDecimal amount,

        @Pattern(regexp = "^(INCOME|EXPENSE)$", message = "Type must be either INCOME or EXPENSE")
        String type,

        @Size(max = 500, message = "Description cannot exceed {max} characters")
        String description
) {
}
