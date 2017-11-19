package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Respond{
	
	private Restaurant[] results;
		
	public Restaurant[] getResults() {
		return results;
	}
	
	public void setResults(Restaurant[] results) {
	       this.results = results;
	}
	
	public String printRespond() {
		String RestaurantList = new String();
		for(Restaurant r:results) {
			RestaurantList+=r.printRestaurant()+'\n';
			if (RestaurantList.length()>900) {
				return RestaurantList;
			}
		}

		return RestaurantList;
	}
}

