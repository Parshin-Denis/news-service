package com.example.NewsService.repository;

import com.example.NewsService.model.Role;
import com.example.NewsService.model.RoleType;
import com.example.NewsService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
