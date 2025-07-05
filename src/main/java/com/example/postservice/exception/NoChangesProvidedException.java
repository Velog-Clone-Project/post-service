package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;

public class NoChangesProvidedException extends BaseCustomException {

    public NoChangesProvidedException() {
        super("No changes provided");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
