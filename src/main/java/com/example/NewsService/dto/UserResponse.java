package com.example.NewsService.dto;

import com.example.NewsService.model.RoleType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserResponse {

    private long id;

    private String name;

    private String password;

    private List<NewsResponse> news = new ArrayList<>();

    private List<CommentResponse> comments = new ArrayList<>();

    private List<RoleType> roles = new ArrayList<>();
}
