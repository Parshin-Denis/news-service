package com.example.NewsService.service;

import com.example.NewsService.dto.NewsListResponse;
import com.example.NewsService.model.News;
import com.example.NewsService.model.NewsFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {

    List<News> filterBy(NewsFilter filter);

    List<News> findAll(Pageable pageable);

    News findById(long id);

    News save(News news);

    News update(News news);

    void deleteById(long id);
}
