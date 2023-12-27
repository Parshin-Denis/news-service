package com.example.NewsService.mapper;

import com.example.NewsService.dto.UpsertNewsRequest;
import com.example.NewsService.model.News;
import com.example.NewsService.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NewsMapperDelegate implements NewsMapper{
    @Autowired
    CategoryService categoryService;

    @Override
    public News requestToNews(UpsertNewsRequest request) {
        News news = new News();
        news.setContent(request.getContent());
        news.setCategory(categoryService.getById(request.getCategoryId()));
        return news;
    }

}
