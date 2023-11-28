package com.example.NewsService.service;

import com.example.NewsService.dto.CategoryListResponse;
import com.example.NewsService.dto.CategoryResponse;
import com.example.NewsService.dto.UpsertCategoryRequest;
import com.example.NewsService.mapper.CategoryMapper;
import com.example.NewsService.model.Category;
import com.example.NewsService.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class DataBaseCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryListResponse findAll(Pageable pageable) {
        return categoryMapper.categoryListToResponseList(
                categoryRepository.findAll(pageable).getContent()
        );
    }

    @Override
    public CategoryResponse findById(long id) {
        return categoryMapper.categoryToResponse(getById(id));
    }

    @Override
    public Category getById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Категория с ID {0} не найдена", id)));
    }

    @Override
    public CategoryResponse save(UpsertCategoryRequest request) {
        return categoryMapper.categoryToResponse(
                categoryRepository.save(
                        categoryMapper.requestToCategory(request)
                )
        );
    }

    @Override
    public CategoryResponse update(long id, UpsertCategoryRequest request) {
        Category existedCategory = getById(id);
        existedCategory.setName(categoryMapper.requestToCategory(request).getName());
        return categoryMapper.categoryToResponse(categoryRepository.save(existedCategory));
    }

    @Override
    public void deleteById(long id) {
        categoryRepository.deleteById(id);
    }
}
