package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.net.*;
@JsonIgnoreProperties(ignoreUnknown = true)
	public class Photos{
		private static final String API_KEY = "AIzaSyAuvyI2NJqZY8SQYLAVSyFhwSldRCgLgf8";
		private String photo_reference;
		private int width=-1;
		
		public int getWidth() {
			return width;
		}
		
		public void setWidth(int width) {
			this.width=width;
		}
		
		public String getPhoto_reference() {
			return photo_reference;
		}
		
	    public void setPhoto_reference(String photo_reference) {
	        this.photo_reference = photo_reference;
	    }
	    
	    public String print() {
	    	if (getPhoto_reference() == null||getWidth() == -1){
	    		return "No detial";
	    	}else {
	    		try {
	    			return resolve_url("https://maps.googleapis.com/maps/api/place/photo?maxwidth="+String.valueOf(width)+"&photoreference="+photo_reference+"&key="+API_KEY);
	    		} catch (Exception ex) {
	    			return "Error when loading";
	    		}
	    	}
	    }
	    
	    public String resolve_url(String old_url) throws Exception {
	    	String new_url = null;
	    	HttpURLConnection connection = (HttpURLConnection) new URL(old_url).openConnection();
	    	connection.setInstanceFollowRedirects(false);
	    	while (connection.getResponseCode() / 100 == 3) {
	    	    new_url = connection.getHeaderField("location");
	    	    connection = (HttpURLConnection) new URL(new_url).openConnection();
	    	}
	    	return new_url;
	    }
	}