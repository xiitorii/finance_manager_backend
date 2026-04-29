package ru.xiitori.financemanager.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xiitori.financemanager.mappers.CategoryMapper;
import ru.xiitori.financemanager.model.dto.category.CategoryResponseDTO;
import ru.xiitori.financemanager.model.dto.category.CreateUpdateCategoryDTO;
import ru.xiitori.financemanager.model.entity.User;
import ru.xiitori.financemanager.repositories.CategoryRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public Set<CategoryResponseDTO> getByUser(User user) {
        var categories = categoryRepository.findByUserId(user.getId());

        return categories.stream()
                .map(mapper::toDto)
                .collect(Collectors.toSet());
    }

    public CategoryResponseDTO getById(Long id, User user) {
        var category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(EntityNotFoundException::new);

        return mapper.toDto(category);
    }

    @Transactional
    public CategoryResponseDTO create(CreateUpdateCategoryDTO dto, User user) {
        var entity = mapper.toEntity(dto);
        entity.setUser(user);

        var saved = categoryRepository.save(entity);

        return mapper.toDto(saved);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CreateUpdateCategoryDTO dto, User user) {
        var entity = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(EntityNotFoundException::new);

        mapper.update(dto, entity);

        return mapper.toDto(entity);
    }

    @Transactional
    public void deleteById(Long id, User user) {
        var category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(EntityNotFoundException::new);
        
        categoryRepository.delete(category);
    }
}
