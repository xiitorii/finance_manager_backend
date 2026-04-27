package ru.xiitori.financemanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.xiitori.financemanager.exceptions.AuthorizationException;
import ru.xiitori.financemanager.model.dto.transaction.TransactionRequestDTO;
import ru.xiitori.financemanager.model.dto.transaction.TransactionResponseDTO;
import ru.xiitori.financemanager.model.dto.transaction.TransactionUpdateDTO;
import ru.xiitori.financemanager.model.entity.User;
import ru.xiitori.financemanager.services.TransactionService;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Set<TransactionResponseDTO>> getAllTransactions(
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        if (!authentication.isAuthenticated() || user == null) {
            throw new AuthorizationException("You are not authorized to access this resource");
        }

        var transactions = transactionService.getByUser(user);

        return ResponseEntity
                .ok(transactions);
    }

    @GetMapping("/period")
    public ResponseEntity<Set<TransactionResponseDTO>> getTransactionsByPeriod(
            @RequestParam LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        var transactions = transactionService.getByUserAndPeriod(
                user, startDate, endDate
        );

        return ResponseEntity
                .ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(
            @PathVariable Long id
    ) {
        var response = transactionService.getById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody TransactionRequestDTO dto,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        var response = transactionService.create(dto, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @RequestBody TransactionUpdateDTO dto,
            @PathVariable Long id,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        var response = transactionService.update(dto, id, user);

        return ResponseEntity
                .ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            Authentication auth
    ) {
        var user = (User) auth.getPrincipal();

        transactionService.delete(id, user);

        return ResponseEntity
                .noContent()
                .build();
    }

}
