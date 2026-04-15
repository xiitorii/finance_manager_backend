package ru.xiitori.financemanager.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xiitori.financemanager.mappers.TransactionMapper;
import ru.xiitori.financemanager.model.dto.transaction.TransactionRequestDTO;
import ru.xiitori.financemanager.model.dto.transaction.TransactionResponseDTO;
import ru.xiitori.financemanager.model.dto.transaction.TransactionUpdateDTO;
import ru.xiitori.financemanager.model.entity.Transaction;
import ru.xiitori.financemanager.model.entity.User;
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

    @Transactional
    public TransactionResponseDTO create(
            TransactionRequestDTO dto,
            User user
    ) {
        var entity = mapper.toEntity(dto);
        entity.setUser(user);

        var saved = transactionRepository.save(entity);

        return mapper.toDto(saved);
    }

    @Transactional
    public TransactionResponseDTO update(
            TransactionUpdateDTO dto,
            Long transactionId,
            User user
    ) {
        var entity = transactionRepository.findById(transactionId)
                .orElseThrow(EntityNotFoundException::new);

        checkRights(user, entity);

        mapper.updateEntity(entity, dto);

        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(
            Long id,
            User user
    ) {
        var entity = transactionRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        checkRights(user, entity);

        transactionRepository.deleteById(id);
    }

    public void checkRights(
            User user,
            Transaction transaction
    ) {
        if (!user.getId().equals(transaction.getUser().getId())) {
            throw new AccessDeniedException(
                    "You are not allowed to edit this record");
        }
    }
}
