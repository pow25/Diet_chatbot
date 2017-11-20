package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
	public class Restaurant {
		private String name, vicinity;
		private Float rating;
		private Photos[] photos;
		
		public Restaurant() {}
		
		public String getName() {
	        return name;
	    }
	    
	    public String getVicinity() {
	        return vicinity;
	    }
		
		public Photos[] getPhotos() {
	        return photos;
	    }
		
		public Float getRating() {
			return rating;
		}
		
	    public void setName(String name) {
	        this.name = name;
	    }

	    public void setVicinity(String vicinity) {
	        this.vicinity = vicinity;
	    }
	    
	    public void setRating(Float rating) {
	        this.rating = rating;
	    }
	    
	    public void setPhotos(Photos[] photos) {
	        this.photos = photos;
	    }
		
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
	    	
	    	Restaurantinfo += "\nMap: ";
	    	if (getPhotos()!=null) {
	    		Restaurantinfo += getPhotos()[0].print();
	    	}else {
	    		Restaurantinfo += "No detail";
	    	}

	    	
	    	
	        return  Restaurantinfo;
	    }
	}