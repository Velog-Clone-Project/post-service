package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class LikeNotFoundException extends BaseCustomException {

    public LikeNotFoundException() {
        super("Like does not exist");
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
