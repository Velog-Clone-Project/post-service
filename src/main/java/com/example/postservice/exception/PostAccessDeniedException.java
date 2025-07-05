package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class PostAccessDeniedException extends BaseCustomException {

    public PostAccessDeniedException() {
        super("Access denied ");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
