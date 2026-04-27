package ru.xiitori.financemanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.xiitori.financemanager.model.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
