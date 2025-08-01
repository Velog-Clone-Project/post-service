package com.example.postservice.exception;

import com.example.common.exception.BaseCustomException;
import feign.FeignException;

public class ExternalServiceException extends BaseCustomException {
    public ExternalServiceException(FeignException e) {
        super("Post service unavailable" + e);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
