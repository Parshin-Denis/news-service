package com.example.NewsService.repository;

import com.example.NewsService.model.Comment;
import com.example.NewsService.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByNewsId(long newsId);
}
