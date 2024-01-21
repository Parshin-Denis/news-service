package com.example.NewsService.controller;

import com.example.NewsService.AbstractTest;
import com.example.NewsService.dto.UpsertNewsRequest;
import com.example.NewsService.mapper.NewsMapper;
import com.example.NewsService.model.News;
import com.example.NewsService.repository.CategoryRepository;
import com.example.NewsService.repository.NewsRepository;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NewsControllerTest extends AbstractTest {

    @Autowired
    protected NewsRepository newsRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected NewsMapper newsMapper;

    public static final long ID_NEWS_TO_TEST = 4;

    @Test
    @WithMockUser
    public void whenGetAllNews_thenReturnNewsList() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/news"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(
                newsMapper.newsListToNewsListResponse(
                        newsRepository.findAll(PageRequest.of(0, 20)).getContent())
        );
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser
    public void whenGetNewsById_thenReturnNews() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/news/{id}", ID_NEWS_TO_TEST))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(
                newsMapper.singleNewsToResponse(
                        newsRepository.findById(ID_NEWS_TO_TEST).orElseThrow()
                )
        );
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "User",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenCreateNews_thenReturnNews() throws Exception {
        assertEquals(5, newsRepository.count());
        UpsertNewsRequest request = new UpsertNewsRequest();
        request.setCategoryId(2);
        request.setContent("News created");
        String actualResponse = mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        News newsCreated = new News();
        newsCreated.setContent(request.getContent());
        newsCreated.setUser(userRepository.findByName("User").orElseThrow());
        newsCreated.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow());
        String expectedResponse = objectMapper.writeValueAsString(
                newsMapper.singleNewsToResponse(newsCreated)
        );
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("id"));
        assertEquals(6, newsRepository.count());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "User",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenUpdateNews_thenReturnNews() throws Exception {
        News newsToUpdate = newsRepository.findById(ID_NEWS_TO_TEST).orElseThrow();
        UpsertNewsRequest request = new UpsertNewsRequest();
        request.setCategoryId(2);
        request.setContent("Updated news");
        String actualResponse = mockMvc.perform(put("/api/news/{id}", ID_NEWS_TO_TEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        newsToUpdate.setContent(request.getContent());
        newsToUpdate.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow());
        String expectedResponse = objectMapper.writeValueAsString(
                newsMapper.singleNewsToResponse(newsToUpdate)
        );
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "User",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenDeleteNews_thenReturnNoContent() throws Exception {
        assertEquals(5, newsRepository.count());
        mockMvc.perform(delete("/api/news/{id}", ID_NEWS_TO_TEST))
                .andExpect(status().isNoContent());
        assertEquals(4, newsRepository.count());
    }
}
