package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class TitleTooLongException extends BaseCustomException {

    public TitleTooLongException() {
        super("Title must not exceed 100 characters");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
