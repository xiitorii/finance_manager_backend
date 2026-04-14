package ru.xiitori.financemanager.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.xiitori.financemanager.model.dto.TransactionResponseDTO;
import ru.xiitori.financemanager.services.TransactionService;

import java.util.Set;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Set<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity
                .ok(transactionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(
            @PathVariable Long id
    ) {
        var response = transactionService.getById(id);

        return ResponseEntity.ok(response);
    }
}
