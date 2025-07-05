package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class InvalidFileTypeException extends BaseCustomException {

    public InvalidFileTypeException() {
        super("Unsupported file type");
    }

    @Override
    public int getStatusCode() {
        return 415;
    }
}
