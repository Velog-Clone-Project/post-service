package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class CommentNotFoundException extends BaseCustomException {
    public CommentNotFoundException() {
        super("Comment not found");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
