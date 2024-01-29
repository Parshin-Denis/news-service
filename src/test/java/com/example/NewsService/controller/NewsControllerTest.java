package com.example.NewsService.controller;

import com.example.NewsService.AbstractTest;
import com.example.NewsService.StringTestUtils;
import com.example.NewsService.dto.UpsertNewsRequest;
import com.example.NewsService.model.News;
import com.example.NewsService.repository.NewsRepository;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    public static final long ID_NEWS_TO_TEST = 1;

    @Test
    @WithMockUser
    public void whenGetAllNews_thenReturnNewsList() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/news"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_all_news.json");

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

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_news_by_id.json");

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

        String expectedResponse = StringTestUtils.readStringFromResource("response/create_news.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
        assertEquals(6, newsRepository.count());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "Admin",
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

        String expectedResponse = StringTestUtils.readStringFromResource("response/update_news.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "Admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenDeleteNews_thenReturnNoContent() throws Exception {
        assertEquals(5, newsRepository.count());

        mockMvc.perform(delete("/api/news/{id}", ID_NEWS_TO_TEST))
                .andExpect(status().isNoContent());

        assertEquals(4, newsRepository.count());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "User",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenNotOwnerDeleteNews_thenReturnNok() throws Exception {
        var response = mockMvc.perform(delete("/api/news/{id}", ID_NEWS_TO_TEST))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");
        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/bad_owner.json");
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}
