package com.example.NewsService.dto;

import lombok.Data;

@Data
public class CommentResponse {
    private long id;

    private String user;

    private String content;
}
