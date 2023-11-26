package com.example.NewsService.controller;

import com.example.NewsService.dto.*;
import com.example.NewsService.mapper.NewsMapper;
import com.example.NewsService.model.News;
import com.example.NewsService.model.NewsFilter;
import com.example.NewsService.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "News API")
public class NewsController {

    private final NewsService newsService;

    private final NewsMapper newsMapper;

    @Operation(
            summary = "Get all news",
            description = "Get all news. Return for each news: ID, Content, UserName, CategoryName, Comments amount"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = NewsListResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @GetMapping
    public ResponseEntity<NewsListResponse> findAll(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(
                newsMapper.newsListToNewsListResponse(
                        newsService.findAll(pageNumber, pageSize)
                )
        );
    }

    @Operation(
            summary = "Get filtered news",
            description = "Get all news filtered by category and user." +
                    " Return for each news: ID, Content, UserName, CategoryName, Comments amount"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = NewsListResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/filter")
    public ResponseEntity<NewsListResponse> filterBy(@Valid NewsFilter filter) {
        return ResponseEntity.ok(
                newsMapper.newsListToNewsListResponse(newsService.filterBy(filter))
        );
    }

    @Operation(
            summary = "Get news by ID",
            description = "Get news by ID. Return ID, Content, UserName, CategoryName, List of Comments",
            tags = {"news", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = SingleNewsResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SingleNewsResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(
                newsMapper.singleNewsToResponse(newsService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<SingleNewsResponse> create(@RequestBody @Valid UpsertNewsRequest request) {
        News newNews = newsService.update(newsMapper.requestToNews(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsMapper.singleNewsToResponse(newNews)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SingleNewsResponse> update(@PathVariable long id,
                                                     @RequestParam long userId,
                                                     @RequestBody @Valid UpsertNewsRequest request) {
        News updatedNews = newsService.update(newsMapper.requestToNews(id, request));
        return ResponseEntity.ok(
                newsMapper.singleNewsToResponse(updatedNews)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id, @RequestParam long userId) {
        newsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
