package com.example.NewsService.dto;

import lombok.Data;

@Data
public class NewsResponse {
    private long id;

    private String content;

    private String category;

    private String user;

    private int commentsAmount;
}
