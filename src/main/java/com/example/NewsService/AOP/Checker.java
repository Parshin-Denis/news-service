package com.example.NewsService.AOP;

import com.example.NewsService.controller.NewsController;
import com.example.NewsService.exception.WrongUserException;
import com.example.NewsService.model.RoleType;
import com.example.NewsService.model.User;
import com.example.NewsService.service.CommentService;
import com.example.NewsService.service.NewsService;
import com.example.NewsService.service.UserService;
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
    @Autowired
    UserService userService;
    private HttpServletRequest request;
    private long userId;
    private long idFromPath;

    @Before("@annotation(CheckOwner)")
    public void checkBefore(JoinPoint joinPoint) {
        getRequestParams(joinPoint);

        if (request.getMethod().equals("DELETE") &&
                (request.isUserInRole(RoleType.ROLE_ADMIN.name()) || request.isUserInRole(RoleType.ROLE_MODERATOR.name()))
        ) {
            return;
        }

        long ownerUserId = joinPoint.getSignature().getDeclaringType().equals(NewsController.class) ?
                newsService.findById(idFromPath).getUser().getId() :
                commentService.findById(idFromPath).getUser().getId();

        if (ownerUserId != userId) {
            throw new WrongUserException("Вы не являетесь создателем контента и поэтому не можете его изменить.");
        }
    }

    @Before("@annotation(CheckUser)")
    public void checkUser(JoinPoint joinPoint) {
        getRequestParams(joinPoint);

        if (request.isUserInRole(RoleType.ROLE_ADMIN.name()) || request.isUserInRole(RoleType.ROLE_MODERATOR.name())) {
            return;
        }

        if (userId != idFromPath) {
            throw new WrongUserException("У вас нет прав для просмотра и изменения данных этого пользователя");
        }
    }

    private void getRequestParams(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        request = ((ServletRequestAttributes) requestAttributes).getRequest();
        Map<String, String> pathVariables = (Map<String, String>)
                request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String userName = request.getUserPrincipal().getName();
        User user = userService.findUserByName(userName);
        userId = user.getId();
        idFromPath = Long.parseLong(pathVariables.get("id"));
    }
}
