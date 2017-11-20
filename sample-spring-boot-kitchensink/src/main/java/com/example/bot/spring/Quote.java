package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 *Store the josn content that have para of name, price and ingredients
 * @author Group 14
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    private String name;
    private int price;
    private String[] ingredients;
    /**
     * Constructor
     */
    public Quote() {
    }
    /**
     * Get name
     * @return name
     */
    public String getName() {
        return name;
    }
    /** 
     * Set name data member
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Get price
     * @return price
     */
    public int getPrice() {
        return price;
    }
    /**
     * Set price data member
     * @param price price
     */
    public void setPrice(int price) {
        this.price = price;
    }
    /**
     * Get ingredients
     * @return string of ingredients
     */
    public String getIngredients() {
        return Arrays.toString(ingredients);
    }
    /**
     * Set ingredients data member
     * @param ingredients ingredients
     */
    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
    /**
     * message of the json content
     * @return message of the json content
     */
    public String printString() {
        return "{" +
                "name='" + name + "\'" +
                ", price=" + String.valueOf(price) +
                ", ingredients=" + Arrays.toString(ingredients) +
                '}';
    }
}
