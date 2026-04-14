package ru.xiitori.financemanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.xiitori.financemanager.model.dto.TransactionRequestDTO;
import ru.xiitori.financemanager.model.dto.TransactionResponseDTO;
import ru.xiitori.financemanager.model.entity.Transaction;

import java.util.Collection;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "userId", source = "transaction.user.id")
    TransactionResponseDTO toDto(Transaction transaction);

    @Mapping(target = "user.id", source = "userId")
    Transaction toEntity(TransactionRequestDTO dto);

    Set<TransactionResponseDTO> toDtoSet(Collection<Transaction> transactions);

    Set<Transaction> toEntitySet(Collection<TransactionRequestDTO> dtos);
}
