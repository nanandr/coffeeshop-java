package com.nandanarafiardika.mycoffeeshopdrinklist.model;

import java.util.List;

public class CoffeeResponse {
    private String status;
    private String message;
    List<CoffeeDrink> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CoffeeDrink> getData() {
        return data;
    }

    public void setData(List<CoffeeDrink> data) {
        this.data = data;
    }
}
