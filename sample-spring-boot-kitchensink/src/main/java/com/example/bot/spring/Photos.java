package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
	public class Photos{
		private String[] html_attributions;
		
		public String[] getHtml_attributions() {
			return html_attributions;
		}
		
	    public void setHtml_attributions(String[] html_attributions) {
	        this.html_attributions = html_attributions;
	    }
	    
	    public String print() {
	    	if (html_attributions == null){
	    		return "No detial";
	    	}else {	
	    		return html_attributions[0].split("\"")[1];
	    	}
	    }
	}