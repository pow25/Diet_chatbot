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

	private Respond respond;
	private static final String HEADER = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	private static final String API_KEY = "AIzaSyAuvyI2NJqZY8SQYLAVSyFhwSldRCgLgf8";
	
	@SuppressWarnings("unused")
	private String _latitude;
	@SuppressWarnings("unused")
	private String _longitude;
	
	
	public RestaurantAPI(String latitude,String longitude) {
		this._latitude=latitude;
		this._longitude=longitude;
	}
	
	public void serachRestaurant() throws IOException{
		String url = HEADER + "location=" + String.valueOf(_latitude) + "," + String.valueOf(_longitude) + 
				"&rankby=distance&types=food&key=" + API_KEY;
		
		RestTemplate restTemplate = new RestTemplate();
		this.respond = restTemplate.getForObject(url, Respond.class);

	}
	
	public String getLongitude() {
        return _longitude;
    }
	
	public String getLatitude() {
        return _latitude;
    }
	
	public String printRestaurant() {
		String total = HEADER + "location=" + _latitude + "," + _longitude + "&rankby=distance&types=food&key=" + API_KEY+"\n"+respond.printRespond();
		return total;
	}
	
	public Respond.Restaurant getRestaurant(int number) {
		return respond.getResult().getRestaurant()[number];
	}
}
