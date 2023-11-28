package com.example.NewsService.mapper;

import com.example.NewsService.dto.*;
import com.example.NewsService.model.Comment;
import com.example.NewsService.model.News;
import com.example.NewsService.model.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DecoratedWith(NewsMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CommentMapper.class})
public interface NewsMapper {

    News requestToNews(UpsertNewsRequest request);

    News requestToNews(Long newsId, UpsertNewsRequest request);

    @Mapping(source = "news.category.name", target = "category")
    @Mapping(source = "news.user.name", target = "user")
    SingleNewsResponse singleNewsToResponse(News news);

    @Mapping(source = "news.category.name", target = "category")
    @Mapping(source = "news.user.name", target = "user")
    @Mapping(source = "news.comments", target = "commentsAmount")
    NewsResponse newsToResponse(News news);

    default int getCommentsAmount(List<Comment> comments){
        return comments.size();
    };

    List<NewsResponse> newsListToResponseList(List<News> newsList);

    default NewsListResponse newsListToNewsListResponse(List<News> newsList){
        NewsListResponse response = new NewsListResponse();
        response.setNews(newsListToResponseList(newsList));
        return response;
    }
}
