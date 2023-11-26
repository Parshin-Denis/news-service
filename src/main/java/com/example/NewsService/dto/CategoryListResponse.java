package com.example.NewsService.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryListResponse {
    List<CategoryResponse> categories = new ArrayList<>();
}
