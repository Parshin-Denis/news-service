package com.example.NewsService.service;

import com.example.NewsService.dto.NewsListResponse;
import com.example.NewsService.dto.SingleNewsResponse;
import com.example.NewsService.dto.UpsertNewsRequest;
import com.example.NewsService.model.News;
import com.example.NewsService.model.NewsFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {

    List<News> filterBy(NewsFilter filter);

    List<News> findAll(Pageable pageable);

    News findById(long id);

    News save(News news, String userName);

    News update(long id, News news);

    void deleteById(long id);
}
