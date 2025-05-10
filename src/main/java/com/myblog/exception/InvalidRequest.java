package com.myblog.exception;

import lombok.Getter;

/**
 * status -> 400
 */
@Getter
public class InvalidRequest extends MyBlogException {

    private static final String MESSAGE = "不正なリクエストです。";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
