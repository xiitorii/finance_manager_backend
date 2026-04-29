package ru.xiitori.financemanager.model.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.xiitori.financemanager.model.enums.TransactionType;

@Data
public class CreateUpdateCategoryDTO {
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Transaction type must be specified")
    private TransactionType transactionType;
}
