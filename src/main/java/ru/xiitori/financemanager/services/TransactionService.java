package ru.xiitori.financemanager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.xiitori.financemanager.repositories.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
}
