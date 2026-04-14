package ru.xiitori.financemanager.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xiitori.financemanager.mappers.TransactionMapper;
import ru.xiitori.financemanager.model.dto.TransactionResponseDTO;
import ru.xiitori.financemanager.repositories.TransactionRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;

    public TransactionResponseDTO getById(Long id) {
        var optional = transactionRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return mapper.toDto(optional);
    }

    public Set<TransactionResponseDTO> getAll() {
        var transactions = transactionRepository.findAll();
        return mapper.toDtoSet(transactions);
    }
}
