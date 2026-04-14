package ru.xiitori.financemanager.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.xiitori.financemanager.mappers.TransactionMapper;
import ru.xiitori.financemanager.model.dto.TransactionResponseDTO;
import ru.xiitori.financemanager.repositories.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;

    public TransactionResponseDTO getById(Long id) {
        var optional = transactionRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return mapper.mapToDto(optional);
    }
}
