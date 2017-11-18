package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {
	private String name, address, distance, duration,url;
	private float rating;
	
	public Restaurant(String name,String address,String distance,String duration,String url,float rating) {
		this.name = name;
        this.address = address;
        this.distance = distance;
        this.duration = duration;
        this.url = url;
        this.rating = rating;
	}
	
	public String getName() {
        return name;
    }
    
    public String getaddress() {
        return address;
    }

	public String getdistance() {
        return distance;
    }
	
	public String getduration() {
        return duration;
    }
	
	public String geturl() {
        return url;
    }
	
	public float getrating() {
		return rating;
	}
	
    public String printRestaurant() {
        return  "name: " + name + "\n" +
                "address: " + address + "\n" +
                "distance: " + distance + "\n" +
                "duration: " + duration + "\n" +
                "rating: " + String.valueOf(rating) + "\n" +
                "On map: " + url + "\n";
    }
}

