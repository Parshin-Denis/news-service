package com.example.NewsService.AOP;

import com.example.NewsService.exception.WrongParamRequestException;
import com.example.NewsService.service.CommentService;
import com.example.NewsService.service.DataBaseNewsService;
import com.example.NewsService.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Aspect
@Component
public class Checker {

    @Autowired
    NewsService newsService;

    @Autowired
    CommentService commentService;

    @Before("@annotation(CheckUser)")
    public void checkBefore(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        Map<String, String> pathVariables = (Map<String, String>)
                request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Object paramUserId = request.getParameter("userId");
        long userId = Long.parseLong(paramUserId.toString());
        long ownerUserId;
        if (joinPoint.getSignature().getDeclaringType().getSimpleName()
                .equals(DataBaseNewsService.class.getSimpleName())) {
            ownerUserId = newsService.findById(Long.parseLong(pathVariables.get("id"))).getId();
        } else {
            ownerUserId = commentService.findById(Long.parseLong(pathVariables.get("id"))).getId();
        }
        if (ownerUserId != userId){
            throw new WrongParamRequestException("Введенный пользователь не является создателем контента. Изменение невозможно.");
        }
    }
}
