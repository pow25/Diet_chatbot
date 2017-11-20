package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.net.*;
/**
 * Handle photos requested from google api
 * @author Group 14
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
	public class Photos{
		private static final String API_KEY = "AIzaSyAuvyI2NJqZY8SQYLAVSyFhwSldRCgLgf8";
		private String photo_reference;
		private int width=-1;
		/**
		 * Get photo width
		 * @return photo width
		 */
		public int getWidth() {
			return width;
		}
		/**
		 * Set photo width data member
		 * @param width photo width
		 */
		public void setWidth(int width) {
			this.width=width;
		}
		/**
		 * Get photo_reference
		 * @return photo_reference
		 */
		public String getPhoto_reference() {
			return photo_reference;
		}
		/**
		 * Set photo_reference data member
		 * @param photo_reference photo reference
		 */
	    public void setPhoto_reference(String photo_reference) {
	        this.photo_reference = photo_reference;
	    }
	    /**
	     * Print url link
	     * @return resolved url
	     */
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
	    /**
	     * Change the long url to short form url
	     * @param old_url original url
	     * @return short form URL
	     * @throws Exception if exception inside
	     */
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