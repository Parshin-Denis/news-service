package com.example.NewsService.service;

import com.example.NewsService.model.Role;
import com.example.NewsService.model.User;
import com.example.NewsService.repository.RoleRepository;
import com.example.NewsService.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataBaseUserService implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Пользователь с ID {0} не найден", id)));
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Пользватель с именем {0} не найден", name)));
    }

    @Override
    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().forEach(role -> role.setUser(user));
        return userRepository.save(user);
    }

    @Override
    public User update(long id, User user) {
        User existedUser = findById(id);
        existedUser.setName(user.getName());
        existedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> rolesToDelete = new ArrayList<>();
        List<Role> rolesToAdd = new ArrayList<>();
        existedUser.getRoles().forEach(role -> {
            if (user.getRoles().stream().map(Role::getRoleType).noneMatch(r -> r == role.getRoleType())){
                rolesToDelete.add(role);
            }
        });
        user.getRoles().forEach(role -> {
            if (existedUser.getRoles().stream().map(Role::getRoleType).noneMatch(r -> r == role.getRoleType())) {
                rolesToAdd.add(role);
            }
        });
        rolesToDelete.forEach(role -> {
            existedUser.getRoles().remove(role);
            roleRepository.delete(role);
        });
        rolesToAdd.forEach(role -> {
            role.setUser(existedUser);
            existedUser.getRoles().add(role);
            roleRepository.save(role);
        });
        return userRepository.save(existedUser);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
