package com.example.NewsService.service;

import com.example.NewsService.model.News;
import com.example.NewsService.model.NewsFilter;

import java.util.List;

public interface NewsService {

    List<News> filterBy(NewsFilter filter);

    List<News> findAll(int pageNumber, int pageSize);

    News findById(long id);

    News save(News news);

    News update(News news);

    void deleteById(long id);
}
