package com.example.NewsService.controller;

import com.example.NewsService.AOP.CheckOwner;
import com.example.NewsService.dto.ErrorResponse;
import com.example.NewsService.dto.NewsListResponse;
import com.example.NewsService.dto.SingleNewsResponse;
import com.example.NewsService.dto.UpsertNewsRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
            description = "Get all news. Return for each news: ID, Content, UserName, CategoryName, Comments amount",
            tags = {"news", "id"}
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
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public NewsListResponse findAll(Pageable pageable) {
        return newsMapper.newsListToNewsListResponse(newsService.findAll(pageable));
    }

    @Operation(summary = "Get filtered news")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filter")
    public NewsListResponse filterBy(@Valid NewsFilter filter) {
        return newsMapper.newsListToNewsListResponse(newsService.filterBy(filter));
    }

    @Operation(summary = "Get news by ID")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public SingleNewsResponse findById(@PathVariable long id) {
        return newsMapper.singleNewsToResponse(newsService.findById(id));
    }

    @Operation(summary = "Create news")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SingleNewsResponse create(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody @Valid UpsertNewsRequest request) {
        return newsMapper.singleNewsToResponse(
                newsService.save(
                        newsMapper.requestToNews(request), userDetails.getUsername()
                )
        );
    }

    @Operation(summary = "Modify news by ID")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    @CheckOwner
    public SingleNewsResponse update(@PathVariable long id,
                                     @RequestBody @Valid UpsertNewsRequest request) {
        return newsMapper.singleNewsToResponse(
                newsService.update(id, newsMapper.requestToNews(request))
        );
    }

    @Operation(summary = "Delete news by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @CheckOwner
    public void delete(@PathVariable long id) {
        newsService.deleteById(id);
    }

}
