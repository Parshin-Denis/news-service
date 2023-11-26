package com.example.NewsService.controller;

import com.example.NewsService.dto.CategoryListResponse;
import com.example.NewsService.dto.CategoryResponse;
import com.example.NewsService.dto.UpsertCategoryRequest;
import com.example.NewsService.mapper.CategoryMapper;
import com.example.NewsService.model.Category;
import com.example.NewsService.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<CategoryListResponse> findAll(@RequestParam int pageNumber, @RequestParam int pageSize){
        return ResponseEntity.ok(
                categoryMapper.categoryListToResponseList(
                        categoryService.findAll(pageNumber, pageSize)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable long id){
        return ResponseEntity.ok(
                categoryMapper.categoryToResponse(categoryService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody @Valid UpsertCategoryRequest request){
        Category newCategory = categoryService.save(categoryMapper.requestToCategory(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryMapper.categoryToResponse(newCategory)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable long id, @RequestBody @Valid UpsertCategoryRequest request){
        Category updatedCategory = categoryService.update(categoryMapper.requestToCategory(id, request));
        return ResponseEntity.ok(
                categoryMapper.categoryToResponse(updatedCategory)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
