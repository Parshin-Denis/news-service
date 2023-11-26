package com.example.NewsService.service;

import com.example.NewsService.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll(int pageNumber, int pageSize);

    User findById(long id);

    User save(User user);

    User update(User user);

    void deleteById(long id);
}
