package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *Store the josn content that have name, vicinity, rating and photos
 * @author Group 14
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
	public class Restaurant {
		private String name, vicinity;
		private Float rating;
		private Photos[] photos;
		/**
		 * Constructor
		 */
		public Restaurant() {}
		/**
		 * Get name
		 * @return name
		 */
		public String getName() {
	        return name;
	    }
	    /**
	     * Get vicinity
	     * @return vicinity
	     */
	    public String getVicinity() {
	        return vicinity;
	    }
		/**
		 * Get photos
		 * @return array of photos
		 */
		public Photos[] getPhotos() {
	        return photos;
	    }
		/**
		 * Get rating
		 * @return rating
		 */
		public Float getRating() {
			return rating;
		}
		/**
		 * Set name data member
		 * @param name name of json
		 */
	    public void setName(String name) {
	        this.name = name;
	    }
	    /**
	     * Set vicinity data member
	     * @param vicinity vicinity of json
	     */
	    public void setVicinity(String vicinity) {
	        this.vicinity = vicinity;
	    }
	    /**
	     * Set rating data member
	     * @param rating rating of json
	     */
	    public void setRating(Float rating) {
	        this.rating = rating;
	    }
	    /**
	     * Set photos data member
	     * @param photos photos of json
	     */
	    public void setPhotos(Photos[] photos) {
	        this.photos = photos;
	    }
		/**
		 * Print info of the restaurant
		 * @return string of the reataurant info
		 */
	    public String printRestaurant() {
	    	String Restaurantinfo="Name: ";
	    	if (getName()!=null) {
	    		Restaurantinfo += getName();
	    	}else {
	    		Restaurantinfo += "No detail";
	    	}
	    	
	    	Restaurantinfo += "\nAddress: ";
	    	if (getVicinity()!=null) {
	    		Restaurantinfo += getVicinity();
	    	}else {
	    		Restaurantinfo += "No detail";
	    	}
	    	
	    	Restaurantinfo += "\nRating: ";
	    	if (getRating()!=null) {
	    		Restaurantinfo += String.valueOf(getRating());
	    	}else {
	    		Restaurantinfo += "No detail";
	    	}
	    	
	    	Restaurantinfo += "\nPhoto: ";
	    	if (getPhotos()!=null) {
	    		Restaurantinfo += getPhotos()[0].print();
	    	}else {
	    		Restaurantinfo += "No detail";
	    	}

	    	
	    	
	        return  Restaurantinfo;
	    }
	}