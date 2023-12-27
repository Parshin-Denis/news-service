package com.example.NewsService.dto;

import com.example.NewsService.model.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpsertUserRequest {

    @NotBlank(message = "Необходимо ввести имя пользователя")
    @Size(min = 3, max = 30, message = "Имя должно быть больше {min} и меньше {max}")
    private String name;

    @NotBlank(message = "Необходимо ввести пароль")
    private String password;

    private List<RoleType> roles = new ArrayList<>();
}
