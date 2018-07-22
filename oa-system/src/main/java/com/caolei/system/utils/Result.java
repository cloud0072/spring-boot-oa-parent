package com.caolei.system.utils;

public class Result<T> {

    private String message;
    private int code;
    private T data;

    Result(String message, int code) {
        this.message = message;
        this.code = code;
    }

    Result(String message, int code, T data) {
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
