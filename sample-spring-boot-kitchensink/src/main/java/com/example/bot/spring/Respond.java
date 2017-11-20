package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Cast json content from passed from RestaurantAPI if it fulfill the formate in Restaurant
 * @author Group 14
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Respond{
	
	private Restaurant[] results;
	/**
	 * Get Result
	 * @return result
	 */
	public Restaurant[] getResults() {
		return results;
	}
	/**
	 * Set result data member
	 * @param results
	 */
	public void setResults(Restaurant[] results) {
	       this.results = results;
	}
	/**
	 * Print all restaurants info
	 * @return all restaurants info
	 */
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

