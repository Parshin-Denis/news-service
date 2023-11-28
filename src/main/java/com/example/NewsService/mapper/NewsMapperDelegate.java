package com.example.NewsService.mapper;

import com.example.NewsService.dto.UpsertNewsRequest;
import com.example.NewsService.model.News;
import com.example.NewsService.service.CategoryService;
import com.example.NewsService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NewsMapperDelegate implements NewsMapper{
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;

    @Override
    public News requestToNews(UpsertNewsRequest request) {
        News news = new News();
        news.setContent(request.getContent());
        news.setUser(userService.findById(request.getUserId()));
        news.setCategory(categoryService.getById(request.getCategoryId()));
        return news;
    }

    @Override
    public News requestToNews(Long id, UpsertNewsRequest request) {
        News news = requestToNews(request);
        news.setId(id);
        return news;
    }

}
