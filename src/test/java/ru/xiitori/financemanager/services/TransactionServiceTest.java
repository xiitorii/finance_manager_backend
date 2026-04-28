package ru.xiitori.financemanager.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import ru.xiitori.financemanager.mappers.TransactionMapper;
import ru.xiitori.financemanager.model.dto.transaction.TransactionRequestDTO;
import ru.xiitori.financemanager.model.dto.transaction.TransactionResponseDTO;
import ru.xiitori.financemanager.model.dto.transaction.TransactionUpdateDTO;
import ru.xiitori.financemanager.model.entity.Transaction;
import ru.xiitori.financemanager.model.entity.User;
import ru.xiitori.financemanager.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    private User user;
    private Transaction transaction;
    private TransactionRequestDTO requestDTO;
    private TransactionUpdateDTO updateDTO;
    private TransactionResponseDTO responseDTO;
    private Pageable pageable;
    private Page<Transaction> transactionPage;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setUser(user);

        requestDTO = new TransactionRequestDTO(new BigDecimal("100.00"), "INCOME", "Test transaction");
        updateDTO = new TransactionUpdateDTO(new BigDecimal("200.00"), "EXPENSE", "Updated transaction");

        responseDTO = new TransactionResponseDTO(1L, new BigDecimal("100.00"), ru.xiitori.financemanager.model.enums.TransactionType.INCOME, "Test transaction", LocalDateTime.now(), LocalDateTime.now(), 1L);

        pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        transactionPage = new PageImpl<>(java.util.List.of(transaction), pageable, 1);
    }

    @Test
    void getById_ShouldReturnTransaction_WhenExistsAndAccessAllowed() {
        when(transactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(responseDTO);

        TransactionResponseDTO result = transactionService.getById(1L, user);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(transactionRepository).findByIdAndUserId(1L, 1L);
        verify(transactionMapper).toDto(transaction);
    }

    @Test
    void getById_ShouldThrowEntityNotFoundException_WhenNotExists() {
        when(transactionRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transactionService.getById(1L, user));

        verify(transactionRepository).findByIdAndUserId(1L, 1L);
        verify(transactionMapper, never()).toDto(any());
    }

    @Test
    void getByUser_ShouldReturnPagedTransactions() {
        when(transactionRepository.findByUserId(1L, pageable)).thenReturn(transactionPage);
        when(transactionMapper.toDto(any(Transaction.class))).thenReturn(responseDTO);

        Page<TransactionResponseDTO> result = transactionService.getByUser(user, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(responseDTO, result.getContent().getFirst());
        verify(transactionRepository).findByUserId(1L, pageable);
        verify(transactionMapper, times(1)).toDto(transaction);
    }

    @Test
    void getByUserAndPeriod_ShouldReturnPagedTransactions() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        when(transactionRepository.getAllByUserIdAndCreatedAtBetween(1L, startDate, endDate, pageable)).thenReturn(transactionPage);
        when(transactionMapper.toDto(any(Transaction.class))).thenReturn(responseDTO);

        Page<TransactionResponseDTO> result = transactionService.getByUserAndPeriod(user, pageable, startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(responseDTO, result.getContent().getFirst());
        verify(transactionRepository).getAllByUserIdAndCreatedAtBetween(1L, startDate, endDate, pageable);
        verify(transactionMapper, times(1)).toDto(transaction);
    }

    @Test
    void create_ShouldSaveAndReturnTransaction() {
        Transaction mappedEntity = new Transaction();
        mappedEntity.setId(1L);
        mappedEntity.setUser(user);

        when(transactionMapper.toEntity(requestDTO)).thenReturn(mappedEntity);
        when(transactionRepository.save(mappedEntity)).thenReturn(mappedEntity);
        when(transactionMapper.toDto(mappedEntity)).thenReturn(responseDTO);

        TransactionResponseDTO result = transactionService.create(requestDTO, user);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertSame(mappedEntity.getUser(), user);
        verify(transactionMapper).toEntity(requestDTO);
        verify(transactionRepository).save(mappedEntity);
        verify(transactionMapper).toDto(mappedEntity);
    }

    @Test
    void update_ShouldUpdateAndReturnTransaction_WhenAccessAllowed() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(responseDTO);

        TransactionResponseDTO result = transactionService.update(updateDTO, 1L, user);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(transactionRepository).findById(1L);
        verify(transactionMapper).updateEntity(transaction, updateDTO);
        verify(transactionMapper).toDto(transaction);
    }

    @Test
    void update_ShouldThrowAccessDeniedException_WhenAccessDenied() {
        User otherUser = new User();
        otherUser.setId(2L);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        assertThrows(AccessDeniedException.class, () ->
                transactionService.update(updateDTO, 1L, otherUser));

        verify(transactionRepository).findById(1L);
        verify(transactionMapper, never()).updateEntity(any(), any());
    }

    @Test
    void delete_ShouldDeleteTransaction_WhenAccessAllowed() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        assertDoesNotThrow(() -> transactionService.delete(1L, user));

        verify(transactionRepository).findById(1L);
        verify(transactionRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowAccessDeniedException_WhenAccessDenied() {
        User otherUser = new User();
        otherUser.setId(2L);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        assertThrows(AccessDeniedException.class, () ->
                transactionService.delete(1L, otherUser));

        verify(transactionRepository).findById(1L);
        verify(transactionRepository, never()).deleteById(anyLong());
    }

    @Test
    void checkRights_ShouldNotThrow_WhenUserHasAccess() {
        assertDoesNotThrow(() -> transactionService.checkRights(user, transaction));
    }

    @Test
    void checkRights_ShouldThrowAccessDeniedException_WhenUserHasNoAccess() {
        User otherUser = new User();
        otherUser.setId(2L);

        assertThrows(AccessDeniedException.class, () ->
                transactionService.checkRights(otherUser, transaction));
    }
}
