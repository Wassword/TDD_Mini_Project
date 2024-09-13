package org.example.tdd_mini_project.ordernotfoundexception;


public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
