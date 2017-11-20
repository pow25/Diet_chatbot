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

/**
 * Cast json content from URL if it fulfill the formate in Qoute
 * @author Group 14
 *
 */
public class JsonHandler {
//	String message;
	private Quote[]  quote;
	/**
	 * Cast website content into Quote
	 * @param url
	 */
	public JsonHandler(String url) {
		RestTemplate restTemplate = new RestTemplate();
		this.quote = restTemplate.getForObject(url, Quote[].class);
//		message = quote.toString();
	}
	/**
	 * Get the json content of the website
	 * @return json content
	 */
	public String getJson() {
		String total = null;
		for(Quote q :quote) {
			total += q.printString() + '\n';
		}
		return total;
	}
	/**
	 * Get Quote
	 * @return array of Quote
	 */
	public Quote[] getQuote() {
		return quote;
	}
}
