package com.myblog.exception;

public class PostNotFound extends RuntimeException {

    private static final String MESSAGE = "該当する投稿が存在しません。";

    public PostNotFound() {
        super(MESSAGE);
    }
}
