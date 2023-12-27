package com.example.NewsService.mapper;

import com.example.NewsService.dto.UpsertUserRequest;
import com.example.NewsService.dto.UserListResponse;
import com.example.NewsService.dto.UserResponse;
import com.example.NewsService.model.Role;
import com.example.NewsService.model.RoleType;
import com.example.NewsService.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {NewsMapper.class, CommentMapper.class})
public interface UserMapper {

    User requestToUser(UpsertUserRequest request);

    UserResponse userToResponse(User user);

    default RoleType roleToRoleType (Role role){
        return role.getRoleType();
    }

    default UserListResponse userListToResponseList(List<User> users){
        UserListResponse response = new UserListResponse();
        response.setUsers(users.stream()
                .map(this::userToResponse).collect(Collectors.toList()));
        return response;
    };
}
