package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class NoImageProvidedException extends BaseCustomException {

    public NoImageProvidedException() {
        super("No image file provided");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
