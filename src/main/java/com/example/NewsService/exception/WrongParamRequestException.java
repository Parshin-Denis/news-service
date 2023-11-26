package com.example.NewsService.exception;

public class WrongParamRequestException extends RuntimeException {
    public WrongParamRequestException(String message) {
        super(message);
    }
}
