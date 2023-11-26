package com.example.NewsService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpsertUserRequest {

    @NotBlank(message = "Необходимо ввести имя пользователя")
    @Size(min = 3, max = 30, message = "Имя должно быть больше {min} и меньше {max}")
    private String name;
}
