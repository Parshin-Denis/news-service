package com.example.NewsService.controller;

import com.example.NewsService.AOP.CheckUser;
import com.example.NewsService.dto.CommentListResponse;
import com.example.NewsService.dto.CommentResponse;
import com.example.NewsService.dto.UpsertCommentRequest;
import com.example.NewsService.mapper.CommentMapper;
import com.example.NewsService.model.Comment;
import com.example.NewsService.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @Operation(summary = "Get all comments for news by its ID")
    @GetMapping
    public ResponseEntity<CommentListResponse> findAllByNews(@RequestParam long newsId) {
        return ResponseEntity.ok(
                commentMapper.commentListToCommentListResponse(
                        commentService.findAllByNews(newsId)
                )
        );
    }

    @Operation(summary = "Get comment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(
                commentMapper.commentToResponse(commentService.findById(id))
        );
    }

    @Operation(summary = "Create comment")
    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody @Valid UpsertCommentRequest request) {
        Comment newComment = commentService.save(commentMapper.requestToComment(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentMapper.commentToResponse(newComment)
                );
    }

    @Operation(summary = "Modify comment by ID")
    @PutMapping("/{id}")
    @CheckUser
    public ResponseEntity<CommentResponse> update(@PathVariable long id,
                                                  @RequestParam long userId,
                                                  @RequestBody @Valid UpsertCommentRequest request) {
        Comment updatedComment = commentService.update(commentMapper.requestToComment(id, request));
        return ResponseEntity.ok(
                commentMapper.commentToResponse(updatedComment)
        );
    }

    @Operation(summary = "Delete comment by ID")
    @DeleteMapping("/{id}")
    @CheckUser
    public ResponseEntity<Void> delete(@PathVariable long id, @RequestParam long userId) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
