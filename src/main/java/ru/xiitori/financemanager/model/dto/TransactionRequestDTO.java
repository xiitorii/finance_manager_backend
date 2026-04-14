package ru.xiitori.financemanager.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDTO {

    private BigDecimal amount;
    private String type;
    private String description;
    private Long userId;
}
