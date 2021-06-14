package com.example.demo.dtos;

public class ErrorDtoResponse {
    private String error;

    public ErrorDtoResponse(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
