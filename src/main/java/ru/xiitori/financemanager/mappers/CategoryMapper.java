package ru.xiitori.financemanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.xiitori.financemanager.model.dto.category.CategoryResponseDTO;
import ru.xiitori.financemanager.model.dto.category.CreateUpdateCategoryDTO;
import ru.xiitori.financemanager.model.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDTO toDto(Category entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    Category toEntity(CreateUpdateCategoryDTO dto);

    void update(CreateUpdateCategoryDTO source, @MappingTarget Category entity);
}
