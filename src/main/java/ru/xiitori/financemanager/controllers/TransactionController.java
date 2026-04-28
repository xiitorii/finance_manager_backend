package ru.xiitori.financemanager.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.xiitori.financemanager.model.dto.transaction.TransactionRequestDTO;
import ru.xiitori.financemanager.model.dto.transaction.TransactionResponseDTO;
import ru.xiitori.financemanager.model.dto.transaction.TransactionUpdateDTO;
import ru.xiitori.financemanager.model.entity.User;
import ru.xiitori.financemanager.services.TransactionService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactions(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        var transactions = transactionService.getByUser(user, pageable);

        return ResponseEntity
                .ok(transactions);
    }

    @GetMapping("/by-period")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactionsByPeriod(
            @RequestParam LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        var transactions = transactionService.getByUserAndPeriod(
                user, pageable, startDate, endDate
        );

        return ResponseEntity
                .ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();
        var response = transactionService.getById(id, user);

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
