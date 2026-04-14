package ru.xiitori.financemanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.xiitori.financemanager.model.dto.TransactionResponseDTO;
import ru.xiitori.financemanager.model.entity.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "userId", source = "transaction.user.id")
    TransactionResponseDTO mapToDto(Transaction transaction);
//
//    Transaction mapToEntity(TransactionRequestDTO dto);
}
