package com.example.NewsService.service;

import com.example.NewsService.model.User;
import com.example.NewsService.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataBaseUserService implements UserService{

    private final UserRepository userRepository;

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
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User existedUser = findById(user.getId());
        existedUser.setName(user.getName());
        return userRepository.save(existedUser);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
