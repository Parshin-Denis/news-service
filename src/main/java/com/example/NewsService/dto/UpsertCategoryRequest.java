package com.example.NewsService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpsertCategoryRequest {

    @NotBlank(message = "Необходимо ввести название категории")
    @Size(min = 3, max = 50, message = "Имя должно быть больше {min} и меньше {max}")
    private String name;
}
