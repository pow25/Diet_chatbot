package com.example.bot.spring;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Respond{
	
	public Respond(){};
	private Result result;
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Result{
		private Restaurant[] restaurants;
		
		public Restaurant[] getRestaurant() {
			return restaurants;
		}
		
		public void setRestaurant(Restaurant[] restaurants) {
		       this.restaurants = restaurants;
		}
		
		public String printResult() {
			String RList = "Debug:\n";
			//for(Restaurant r :restaurants) {
				//RList += r.printRestaurant() + '\n';
			//}
			return RList;
		}
		
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class Restaurant {
		private String name, vicinity;
		private float rating;
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
		
		public float getRating() {
			return rating;
		}
		
	    public void setName(String name) {
	        this.name = name;
	    }

	    public void setVicinity(String vicinity) {
	        this.vicinity = vicinity;
	    }
	    
	    public void setRating(float rating) {
	        this.rating = rating;
	    }
	    
	    public void setPhotos(Photos[] photos) {
	        this.photos = photos;
	    }
		
	    public String printRestaurant() {
	        return  "Name: " + name + "\n" +
	                "Address: " + vicinity + "\n" +
	                "Rating: " + String.valueOf(rating) + "\n" +
	                "Detail: " + photos[0].print() + "\n";
	    }
	}
	
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
	    	return html_attributions[0];
	    }
	}
	
	public Result getResult() {
		return result;
	}
	
	public void setResult(Result result) {
	       this.result = result;
	}
	
	public String printRespond() {
		//return result.printResult();
		return "debug";
	}
}

