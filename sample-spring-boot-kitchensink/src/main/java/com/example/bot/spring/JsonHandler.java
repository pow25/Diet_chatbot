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

public class JsonHandler {
//	String message;
	Quote[]  quote;
	public JsonHandler(String url) {
		RestTemplate restTemplate = new RestTemplate();
		this.quote = restTemplate.getForObject(url, Quote[].class);
//		message = quote.toString();
	}
	public String getJson() {
		String total = null;
		for(Quote q :quote) {
			total += q.printString() + '\n';
		}
		return total;
	}
	
	public Quote[] getQuote() {
		return quote;
	}
}
