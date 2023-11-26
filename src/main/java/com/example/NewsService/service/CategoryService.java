package com.example.NewsService.service;

import com.example.NewsService.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll(int pageNumber, int pageSize);

    Category findById(long id);

    Category save(Category category);

    Category update(Category category);

    void deleteById(long id);
}
