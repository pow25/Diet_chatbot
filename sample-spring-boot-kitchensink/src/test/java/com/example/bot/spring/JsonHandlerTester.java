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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.io.ByteStreams;

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
import java.lang.String;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = { "JsonHandlerTester.class", "JsonHandler.class"})
public class JsonHandlerTester {
	
	@Test
	public void jsonurl() throws Exception {
		boolean thrown = false;
		JsonHandler jsonHandler = new JsonHandler("https://wwwabd-efcom.000webhostapp.com/data.json");

//		try {
			Quote[] quote = jsonHandler.getQuote();
			if (quote[1].getName() != "Spicy Bean curd with Minced Pork served with Rice"){
				thrown = true;
			}
//		}catch(Exception e) {
//			thrown = true;
//		}
		assertThat(thrown).isEqualTo(false);
	}
//	@Test
//	public void jsoninvalidurl() throws Exception {
//		boolean thrown = false;
//		String text = "https://wwwabd-efcom.000webhostapp.com/data.json";
//		try {
//			URL url = new URL(url);
//			jsonhandler = new JsonHandler(text);
//			Quote[] quote = jsonHandler.getQuote();
//			if {quote[1].getName() == "Spicy Bean curd with Minced Pork served with Rice"){
//				thrown = true;
//			}
//		}catch(Exception e) {
//			thrown = true;
//		}
//		assertThar(thrown).isEqualTo(False);
//	}
}
