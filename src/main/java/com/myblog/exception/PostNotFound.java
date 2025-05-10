package com.myblog.exception;

/**
 * status -> 404
 */
public class PostNotFound extends MyBlogException {

    private static final String MESSAGE = "該当する投稿が存在しません。";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
