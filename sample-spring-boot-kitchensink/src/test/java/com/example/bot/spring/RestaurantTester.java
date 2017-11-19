package com.example.bot.spring;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;



import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.google.common.io.ByteStreams;
import com.example.bot.spring.DatabaseEngine;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RestaurantTester.class})
public class RestaurantTester {
	
	@Test
	public void testapi()  throws Exception{
		boolean thrown = false;
//		try {
//			RestaurantAPI restaurantApi = new RestaurantAPI(22.327786,114.215811);
//			restaurantApi.serachRestaurant();
//			String result=restaurantApi.printRestaurant();
//			System.out.println("\n\n\nResult:\n\n\n"+result);
//			if (result.equals("null")){
//				thrown = true;
//			}
//		} catch (Exception e) {
//			thrown = true;
//			System.out.println("\n\n\nError:\n\n\n"+e.toString());
//		}
//		assertThat(thrown).isEqualTo(false);
		RestaurantAPI restaurantApi = new RestaurantAPI(22.327786,114.215811);
		try {
			restaurantApi.serachRestaurant();
		}catch(Exception ex){
			thrown = true;
		}
		System.out.println("Restaurant nearby:\n\n"+restaurantApi.printRestaurant());
		assertThat(thrown).isEqualTo(false);
	}

	
}

