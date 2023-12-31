package com.example.NewsService.service;

import com.example.NewsService.dto.SingleNewsResponse;
import com.example.NewsService.dto.UpsertNewsRequest;
import com.example.NewsService.mapper.NewsMapper;
import com.example.NewsService.model.News;
import com.example.NewsService.model.NewsFilter;
import com.example.NewsService.repository.NewsRepository;
import com.example.NewsService.repository.NewsSpecification;
import com.example.NewsService.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataBaseNewsService implements NewsService{

    private final NewsRepository newsRepository;

    private final UserService userService;


    @Override
    public List<News> filterBy(NewsFilter filter) {
        return newsRepository.findAll(NewsSpecification.withFilter(filter),
                PageRequest.of(
                        filter.getPageNumber(), filter.getPageSize()
                )).getContent();
    }

    @Override
    public List<News> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable).getContent();
    }

    @Override
    public News findById(long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Новость с ID {0} не найдена", id)));
    }

    @Override
    public News save(News news, String userName) {
        news.setUser(userService.findUserByName(userName));
        return newsRepository.save(news);
    }

    @Override
    public News update(long id, News news) {
        News existedNews = findById(id);
        existedNews.setContent(news.getContent());
        existedNews.setCategory(news.getCategory());
        return newsRepository.save(existedNews);
    }

    @Override
    public void deleteById(long id) {
        newsRepository.deleteById(id);
    }
}
