package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class AlreadyLikedException extends BaseCustomException {

    public AlreadyLikedException() {
        super("Already liked");
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
