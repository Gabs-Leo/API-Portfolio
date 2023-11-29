package com.gabsleo.portfolio.utils;

import java.util.ArrayList;
import java.util.List;

public class Response <T>{
    private T content;
    private List<String> errors = new ArrayList<>();

    public Response() {}
    public Response(T content, List<String> errors) {
        this.content = content;
        this.errors = errors;
    }

    public T getContent() {
        return content;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
