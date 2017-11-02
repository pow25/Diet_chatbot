package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    private String name;
    private int price;
    private String[] ingredients;

    public Quote() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    public String getIngredients() {
        return Arrays.toString(ingredients);
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String printString() {
        return "{" +
                "name='" + name + "\'" +
                ", price=" + String.valueOf(price) +
                ", ingredients=" + Arrays.toString(ingredients) +
                '}';
    }
}
