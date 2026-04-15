package ru.xiitori.financemanager.model.dto.transaction;

import ru.xiitori.financemanager.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        BigDecimal amount,
        TransactionType type,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long userId
) {
}
