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
		int count=0;
		String RestaurantList = new String();
		for(Restaurant r:results) {
			count+=1;
			RestaurantList+=String.valueOf(count)+".\n"+r.printRestaurant()+'\n';
			if (RestaurantList.length()>950) {
				return RestaurantList;
			}
		}

		return RestaurantList;
	}
}

