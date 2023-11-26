package com.example.NewsService.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SingleNewsResponse {
    private long id;

    private String content;

    private String category;

    private String user;

    private List<CommentResponse> comments = new ArrayList<>();
}
