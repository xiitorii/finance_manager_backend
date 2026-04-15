package ru.xiitori.financemanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.xiitori.financemanager.model.dto.transaction.TransactionRequestDTO;
import ru.xiitori.financemanager.model.dto.transaction.TransactionResponseDTO;
import ru.xiitori.financemanager.model.dto.transaction.TransactionUpdateDTO;
import ru.xiitori.financemanager.model.entity.Transaction;
import ru.xiitori.financemanager.model.enums.TransactionType;

import java.util.Collection;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "userId", source = "transaction.user.id")
    TransactionResponseDTO toDto(Transaction transaction);

    Transaction toEntity(TransactionRequestDTO dto);

    Set<TransactionResponseDTO> toDtoSet(Collection<Transaction> transactions);

    Set<Transaction> toEntitySet(Collection<TransactionRequestDTO> dtos);

    default void updateEntity(Transaction entity, TransactionUpdateDTO dto) {
        if (dto.description() != null) {
            entity.setDescription(dto.description());
        }
        if (dto.amount() != null) {
            entity.setAmount(dto.amount());
        }
        if (dto.type() != null) {
            entity.setType(TransactionType.valueOf(dto.type()));
        }
    }
}
