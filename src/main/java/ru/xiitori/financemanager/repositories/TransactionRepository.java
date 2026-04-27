package ru.xiitori.financemanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.xiitori.financemanager.model.entity.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Set<Transaction> findByUserId(Long userId);


    Set<Transaction> getAllByUserIdAndCreatedAtBetween(
            Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
