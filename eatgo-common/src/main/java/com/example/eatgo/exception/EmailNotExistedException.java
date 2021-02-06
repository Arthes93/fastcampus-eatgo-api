package com.example.eatgo.exception;

public class EmailNotExistedException extends RuntimeException{
    public EmailNotExistedException(){
        super("Email is Not registered");
    }
}
