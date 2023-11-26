package com.example.NewsService.service;

import com.example.NewsService.exception.WrongParamRequestException;
import com.example.NewsService.model.Category;
import com.example.NewsService.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataBaseCategoryService implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize < 0){
            throw new WrongParamRequestException("Значение параметров пагинации не могут быть меньше нуля");
        }
        return categoryRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Category findById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Категория с ID {0} не найдена", id)));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        Category existedCategory = findById(category.getId());
        existedCategory.setName(category.getName());
        return categoryRepository.save(existedCategory);
    }

    @Override
    public void deleteById(long id) {
        categoryRepository.deleteById(id);
    }
}
