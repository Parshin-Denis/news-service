package com.example.NewsService.service;

import com.example.NewsService.model.Comment;
import com.example.NewsService.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataBaseCommentService implements CommentService{

    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> findAllByNews(long newsId) {
        return commentRepository.findAllByNewsId(newsId);
    }

    @Override
    public Comment findById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Комментарий с ID {0} не найден", id)));
    }

    @Override
    public Comment save(Comment comment, String userName) {
        comment.setUser(userService.findUserByName(userName));
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(long id, Comment comment) {
        Comment existedComment = findById(id);
        existedComment.setContent(comment.getContent());
        existedComment.setNews(comment.getNews());
        return commentRepository.save(existedComment);
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
