package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class InvalidCursorIdException extends BaseCustomException {

    public InvalidCursorIdException() {
        super("Invalid cursorId");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
