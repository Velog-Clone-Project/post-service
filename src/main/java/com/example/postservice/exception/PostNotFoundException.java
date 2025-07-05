package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class PostNotFoundException extends BaseCustomException {

    public PostNotFoundException() {
        super("Post not found");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
