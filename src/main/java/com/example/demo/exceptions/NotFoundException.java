package com.example.demo.exceptions;

public class NotFoundException extends Exception{
    public NotFoundException(String emprestimoNotFoundError) {
        super(emprestimoNotFoundError);
    }
}
