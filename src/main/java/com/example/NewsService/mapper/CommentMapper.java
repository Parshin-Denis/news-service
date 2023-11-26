package com.example.NewsService.mapper;

import com.example.NewsService.dto.*;
import com.example.NewsService.model.Comment;
import com.example.NewsService.model.News;
import com.example.NewsService.service.NewsService;
import com.example.NewsService.service.UserService;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CommentMapper {

    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;

    public Comment requestToComment(UpsertCommentRequest request){
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setNews(newsService.findById(request.getNewsId()));
        comment.setUser(userService.findById(request.getUserId()));
        return comment;
    };

    public Comment requestToComment(Long commentId, UpsertCommentRequest request){
        Comment comment = requestToComment(request);
        comment.setId(commentId);
        return comment;
    }

    @Mapping(source = "comment.user.name", target = "user")
    public abstract CommentResponse commentToResponse(Comment comment);

    public abstract List<CommentResponse> commentListToResponseList(List<Comment> commentList);

    public CommentListResponse commentListToCommentListResponse(List<Comment> commentList){
        CommentListResponse response = new CommentListResponse();
        response.setComments(commentListToResponseList(commentList));
        return response;
    }
}
