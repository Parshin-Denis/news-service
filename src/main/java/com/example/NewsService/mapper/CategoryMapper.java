package com.example.NewsService.mapper;

import com.example.NewsService.dto.*;
import com.example.NewsService.model.Category;
import com.example.NewsService.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {NewsMapper.class})
public interface CategoryMapper {

    Category requestToCategory(UpsertCategoryRequest request);

    @Mapping(source = "categoryId", target = "id")
    Category requestToCategory(Long categoryId, UpsertCategoryRequest request);

    CategoryResponse categoryToResponse(Category category);

    default CategoryListResponse categoryListToResponseList(List<Category> categories){
        CategoryListResponse response = new CategoryListResponse();
        response.setCategories(categories.stream()
                .map(this::categoryToResponse).collect(Collectors.toList()));
        return response;
    };
}
