package com.example.NewsService.controller;

import com.example.NewsService.AOP.CheckOwner;
import com.example.NewsService.dto.CommentListResponse;
import com.example.NewsService.dto.CommentResponse;
import com.example.NewsService.dto.UpsertCommentRequest;
import com.example.NewsService.mapper.CommentMapper;
import com.example.NewsService.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<CommentResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody @Valid UpsertCommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentMapper.commentToResponse(
                        commentService.save(commentMapper.requestToComment(request), userDetails.getUsername())
                        )
                );
    }

    @Operation(summary = "Modify comment by ID")
    @PutMapping("/{id}")
    @CheckOwner
    public ResponseEntity<CommentResponse> update(@PathVariable long id,
                                                  @RequestBody @Valid UpsertCommentRequest request) {
        return ResponseEntity.ok(
                commentMapper.commentToResponse(
                        commentService.update(id, commentMapper.requestToComment(request))
                )
        );
    }

    @Operation(summary = "Delete comment by ID")
    @DeleteMapping("/{id}")
    @CheckOwner
    public ResponseEntity<Void> delete(@PathVariable long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
