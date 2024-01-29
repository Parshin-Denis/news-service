package com.example.NewsService.controller;

import com.example.NewsService.AbstractTest;
import com.example.NewsService.StringTestUtils;
import com.example.NewsService.dto.UpsertUserRequest;
import com.example.NewsService.model.RoleType;
import com.example.NewsService.repository.UserRepository;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends AbstractTest {

    @Autowired
    UserRepository userRepository;

    public static final long ID_USER_TO_TEST = 1;

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void whenGetAllUsers_thenReturnUserList() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_all_users.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "Admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenGetUserById_thenReturnUser() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/user/{id}", ID_USER_TO_TEST))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_user_by_id.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateUser_thenReturnNewUser() throws Exception {
        assertEquals(2, userRepository.count());
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("newUser");
        request.setPassword("12345");
        request.setRoles(Collections.singletonList(RoleType.ROLE_USER));
        String actualResponse = mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/create_user.json");

        assertEquals(3, userRepository.count());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("password"));
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "Admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("updatedUser");
        request.setPassword("12345");
        request.setRoles(Collections.singletonList(RoleType.ROLE_USER));

        String actualResponse = mockMvc.perform(put("/api/user/{id}", ID_USER_TO_TEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/update_user.json");
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("password"));
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "Admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenDeleteUser_thenReturnNoContent() throws Exception {
        assertEquals(2, userRepository.count());
        mockMvc.perform(delete("/api/user/{id}", ID_USER_TO_TEST))
                .andExpect(status().isNoContent());
        assertEquals(1, userRepository.count());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "User",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenGetUserWithNotCorrectRole_thenReturnNok() throws Exception{
        var response = mockMvc.perform(get("/api/user/{id}", ID_USER_TO_TEST))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");
        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource("response/bad_user.json");
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}
