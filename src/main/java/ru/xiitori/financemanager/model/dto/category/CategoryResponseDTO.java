package ru.xiitori.financemanager.model.dto.category;

import lombok.Data;
import ru.xiitori.financemanager.model.enums.TransactionType;

@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private TransactionType transactionType;
}
