package com.example.bot.spring;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    private String name;
    private String price;
    private String ingredients;

    public Quote() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", ingredients='" + ingredients +
                '}';
    }
}
