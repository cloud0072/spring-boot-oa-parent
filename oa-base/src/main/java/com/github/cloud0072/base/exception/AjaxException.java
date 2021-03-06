package com.github.cloud0072.base.exception;

/**
 * 抛出是自动返回json
 *
 * @author cloud0072
 */
public class AjaxException extends RuntimeException {

    public AjaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public AjaxException(String message) {
        super(message);
    }

    public AjaxException(Exception e) {
        super(e);
    }

}
