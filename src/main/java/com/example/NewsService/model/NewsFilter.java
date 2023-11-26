package com.example.NewsService.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class NewsFilter {

    @PositiveOrZero(message = "Значение параметра пагинации не должно быть меньше нуля")
    private Integer pageSize;

    @PositiveOrZero(message = "Значение параметра пагинации не должно быть меньше нуля")
    private Integer pageNumber;

    @Positive(message = "Значение ID пользователя должно быть больше нуля")
    private Long userId;

    @Positive(message = "Значение ID категории должно быть больше нуля")
    private Long categoryId;
}
