package com.example.NewsService.service;

import com.example.NewsService.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> findAll(Pageable pageable);

    User findById(long id);

    User save(User user);

    User update(User user);

    void deleteById(long id);
}
