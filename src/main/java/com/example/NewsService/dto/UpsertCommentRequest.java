package com.example.NewsService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpsertCommentRequest {

    @Positive(message = "Значение ID новости должно быть больше нуля")
    private long newsId;

    @NotBlank(message = "Необходимо ввести содержание новости")
    private String content;
}
