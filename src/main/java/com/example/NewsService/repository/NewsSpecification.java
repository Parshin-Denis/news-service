package com.example.NewsService.repository;

import com.example.NewsService.model.News;
import com.example.NewsService.model.NewsFilter;
import org.springframework.data.jpa.domain.Specification;

public interface NewsSpecification {
    static Specification<News> withFilter(NewsFilter newsFilter){
        return Specification.where(byUserId(newsFilter.getUserId()))
                .and(byCategoryId(newsFilter.getCategoryId()));
    }

    static Specification<News> byUserId(Long userId){
        return ((root, query, cb) -> {
            if (userId == null){
                return null;
            }
            return cb.equal(root.get("user").get("id"), userId);
        });
    }

    static Specification<News> byCategoryId(Long categoryId){
        return ((root, query, cb) -> {
            if (categoryId == null){
                return null;
            }
            return cb.equal(root.get("category").get("id"), categoryId);
        });
    };
}
