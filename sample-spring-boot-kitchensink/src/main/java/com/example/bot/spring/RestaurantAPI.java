package com.example.bot.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class RestaurantAPI {

	private Restaurant[]  restaurants;
	private static final String HEADER = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	private static final String API_KEY = "AIzaSyAuvyI2NJqZY8SQYLAVSyFhwSldRCgLgf8";
	
	@SuppressWarnings("unused")
	private double _latitude;
	@SuppressWarnings("unused")
	private double _longitude;
	
	
	public RestaurantAPI(double latitude,double longitude) {
		this._latitude=latitude;
		this._longitude=longitude;
		serachRestaurant();
	}
	
	public void serachRestaurant() {
		String url = HEADER + "location=" + String.valueOf(_latitude) + "," + String.valueOf(_longitude) + 
				"&rankby=distance&types=food&key=" + API_KEY;
		
		RestTemplate restTemplate = new RestTemplate();
		this.restaurants = restTemplate.getForObject(url, Restaurant[].class);
	}
	
	public double getLongitude() {
        return _longitude;
    }
	
	public double getLatitude() {
        return _latitude;
    }
	
	public String printRestaurant() {
		String total = null;
		for(Restaurant r :restaurants) {
			total += r.printRestaurant() + '\n';
		}
		return total;
	}
	
	public Restaurant getRestaurant(int number) {
		return restaurants[number];
	}
}
