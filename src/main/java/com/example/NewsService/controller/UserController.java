package com.example.NewsService.controller;

import com.example.NewsService.dto.UpsertUserRequest;
import com.example.NewsService.dto.UserListResponse;
import com.example.NewsService.dto.UserResponse;
import com.example.NewsService.mapper.UserMapper;
import com.example.NewsService.model.User;
import com.example.NewsService.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserListResponse> findAll(@RequestParam int pageNumber, @RequestParam int pageSize){
        return ResponseEntity.ok(
                userMapper.userListToResponseList(
                        userService.findAll(pageNumber, pageSize)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable long id){
        return ResponseEntity.ok(
                userMapper.userToResponse(userService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UpsertUserRequest request){
        User newUser = userService.save(userMapper.requestToUser(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToResponse(newUser)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable long id, @RequestBody @Valid UpsertUserRequest request){
        User updatedUser = userService.update(userMapper.requestToUser(id, request));
        return ResponseEntity.ok(
                userMapper.userToResponse(updatedUser)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
