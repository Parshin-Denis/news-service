package com.example.NewsService.service;

import com.example.NewsService.dto.CategoryListResponse;
import com.example.NewsService.dto.CategoryResponse;
import com.example.NewsService.dto.UpsertCategoryRequest;
import com.example.NewsService.model.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryListResponse findAll(Pageable pageable);

    CategoryResponse findById(long id);

    Category getById(long id);

    CategoryResponse save(UpsertCategoryRequest request);

    CategoryResponse update(long id, UpsertCategoryRequest request);

    void deleteById(long id);
}
