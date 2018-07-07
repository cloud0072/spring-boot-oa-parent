package com.caolei.system.utils;

import org.springframework.http.HttpStatus;

public class Result<T> {

    private String message;
    private HttpStatus code;
    private T data;

    Result(String message, HttpStatus code) {
        this.message = message;
        this.code = code;
    }

    Result(String message, HttpStatus code, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
