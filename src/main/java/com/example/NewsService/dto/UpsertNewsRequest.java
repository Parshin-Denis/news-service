package com.example.NewsService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpsertNewsRequest {

    @Positive(message = "Значение ID категории должно быть больше нуля")
    private long categoryId;

    @NotBlank(message = "Необходимо ввести содержание новости")
    private String content;
}
