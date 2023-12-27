package com.example.NewsService.controller;

import com.example.NewsService.dto.CategoryListResponse;
import com.example.NewsService.dto.CategoryResponse;
import com.example.NewsService.dto.UpsertCategoryRequest;
import com.example.NewsService.mapper.CategoryMapper;
import com.example.NewsService.model.Category;
import com.example.NewsService.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public CategoryListResponse findAll(Pageable pageable){
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Get category by ID")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public CategoryResponse findById(@PathVariable long id){
        return categoryService.findById(id);
    }

    @Operation(summary = "Create category")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public CategoryResponse create(@RequestBody @Valid UpsertCategoryRequest request){
        return categoryService.save(request);
    }

    @Operation(summary = "Modify category by ID")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public CategoryResponse update(@PathVariable long id, @RequestBody @Valid UpsertCategoryRequest request){
        return categoryService.update(id, request);
    }

    @Operation(summary = "Delete category by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public void delete(@PathVariable long id){
        categoryService.deleteById(id);
    }
}
