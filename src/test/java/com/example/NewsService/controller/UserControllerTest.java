package com.example.NewsService.controller;

import com.example.NewsService.AbstractTest;
import com.example.NewsService.dto.UpsertUserRequest;
import com.example.NewsService.dto.UserResponse;
import com.example.NewsService.mapper.UserMapper;
import com.example.NewsService.model.Role;
import com.example.NewsService.model.RoleType;
import com.example.NewsService.model.User;
import com.example.NewsService.repository.UserRepository;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends AbstractTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    protected UserMapper userMapper;

    public static final long ID_USER_TO_TEST = 1;

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void whenGetAllUsers_thenReturnUserList() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(
                userMapper.userListToResponseList(
                        userRepository.findAll(PageRequest.of(0, 20)).getContent())
        );
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
        String expectedResponse = objectMapper.writeValueAsString(
                userMapper.userToResponse(
                        userRepository.findById(ID_USER_TO_TEST).orElseThrow())
        );
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateUser_thenReturnUser() throws Exception {
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

        UserResponse userResponse = new UserResponse();
        userResponse.setName(request.getName());
        userResponse.setPassword(passwordEncoder.encode(request.getPassword()));
        userResponse.setRoles(request.getRoles());
        String expectedResponse = objectMapper.writeValueAsString(userResponse);
        assertEquals(3, userRepository.count());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("id", "password"));
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "Admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenUpdateUser_thenReturnUser() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("updatedUser");
        request.setPassword("12345");
        request.setRoles(Collections.singletonList(RoleType.ROLE_USER));
        User userToUpdate = userRepository.findById(ID_USER_TO_TEST).orElseThrow();
        String actualResponse = mockMvc.perform(put("/api/user/{id}", ID_USER_TO_TEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        userToUpdate.setName(request.getName());
        userToUpdate.setPassword(passwordEncoder.encode(request.getPassword()));
        userToUpdate.setRoles(request.getRoles()
                .stream()
                .map(Role::from)
                .collect(Collectors.toList()));
        String expectedResponse = objectMapper.writeValueAsString(
                userMapper.userToResponse(userToUpdate)
        );
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
}
