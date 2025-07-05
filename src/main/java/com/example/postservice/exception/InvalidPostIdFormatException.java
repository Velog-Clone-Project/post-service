package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class InvalidPostIdFormatException extends BaseCustomException {

    public InvalidPostIdFormatException() {
        super("Invalid postId");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
