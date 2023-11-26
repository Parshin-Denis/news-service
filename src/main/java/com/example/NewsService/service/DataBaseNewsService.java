package com.example.NewsService.service;

import com.example.NewsService.AOP.CheckUser;
import com.example.NewsService.exception.WrongParamRequestException;
import com.example.NewsService.model.News;
import com.example.NewsService.model.NewsFilter;
import com.example.NewsService.repository.NewsRepository;
import com.example.NewsService.repository.NewsSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataBaseNewsService implements NewsService{

    private final NewsRepository newsRepository;

    @Override
    public List<News> filterBy(NewsFilter filter) {
        return newsRepository.findAll(NewsSpecification.withFilter(filter),
                PageRequest.of(
                        filter.getPageNumber(), filter.getPageSize()
                )).getContent();
    }

    @Override
    public List<News> findAll(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize < 0){
            throw new WrongParamRequestException("Значение параметров пагинации не могут быть меньше нуля");
        }
        return newsRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public News findById(long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Новость с ID {0} не найдена", id)));
    }

    @Override
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    @CheckUser
    public News update(News news) {
        News existedNews = findById(news.getId());
        existedNews.setContent(news.getContent());
        existedNews.setUser(news.getUser());
        existedNews.setCategory(news.getCategory());
        return newsRepository.save(existedNews);
    }

    @Override
    @CheckUser
    public void deleteById(long id) {
        newsRepository.deleteById(id);
    }
}
