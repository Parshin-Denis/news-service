package com.example.NewsService.controller;

import com.example.NewsService.AOP.CheckUser;
import com.example.NewsService.dto.UpsertUserRequest;
import com.example.NewsService.dto.UserListResponse;
import com.example.NewsService.dto.UserResponse;
import com.example.NewsService.mapper.UserMapper;
import com.example.NewsService.model.RoleType;
import com.example.NewsService.model.User;
import com.example.NewsService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    @Operation(summary = "Get all users")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserListResponse> findAll(Pageable pageable) {
        return ResponseEntity.ok(
                userMapper.userListToResponseList(
                        userService.findAll(pageable)
                )
        );
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @CheckUser
    public ResponseEntity<UserResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(
                userMapper.userToResponse(userService.findById(id))
        );
    }

    @Operation(summary = "Create user")
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UpsertUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToResponse(
                                userService.create(userMapper.requestToUser(request))
                        )
                );
    }

    @Operation(summary = "Modify user by ID")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @CheckUser
    public ResponseEntity<UserResponse> update(@PathVariable long id,
                                               @RequestBody @Valid UpsertUserRequest request) {
        return ResponseEntity.ok(
                userMapper.userToResponse(
                        userService.update(id, userMapper.requestToUser(request))
                )
        );
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @CheckUser
    public ResponseEntity<Void> delete(@PathVariable long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
