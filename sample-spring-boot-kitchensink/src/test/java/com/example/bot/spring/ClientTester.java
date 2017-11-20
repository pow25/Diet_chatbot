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

import com.example.bot.spring.Client;




@RunWith(SpringRunner.class)

@SpringBootTest(classes = {ClientTester.class,Client.class })
public class ClientTester{
	
	@Autowired
	private Client client;
	@Test
	public void test()  throws Exception{
		boolean thrown = false;
		try {
			if (client.isInfoComplete("123456")!=1) {
				thrown=true;
			}
			if (client.calculateBMI()!=0) {
				thrown=true;
			}
			client.addClient("123456");
			if (client.getProfile()==null) {
				thrown=true;
			}
			if (client.isInfoComplete("123456")!=2) {
				thrown=true;
			}
			client.updateName("comp");
			if (client.isInfoComplete("123456")!=3) {
				thrown=true;
			}
			client.updateAge(3111);
			if (client.isInfoComplete("123456")!=4) {
				thrown=true;
			}
			client.updateGender("men");
			if (client.isInfoComplete("123456")!=5) {
				thrown=true;
			}
			client.updateHeight(2);
			if (client.isInfoComplete("123456")!=6) {
				thrown=true;
			}
			if (client.calculateBMI()!=0) {
				thrown=true;
			}
			client.updateWeight(4);
			if (client.isInfoComplete("123456")!=0) {
				thrown=true;
			}
			client.loadClient("123456");
			if (!client.getName().equals("comp")) {
				thrown=true;
			}
			if (client.getAge()!=3111) {
				thrown=true;
			}
			if (client.getHeight()==0) {
				thrown=true;
			}
			if (client.getWeight()==0) {
				thrown=true;
			}
			if (client.calculateBMI()==0) {
				thrown=true;
			}
			if (!client.getGender().equals("men")) {
				thrown=true;
			}
			client.addHistory("cheese burgur");
			client.addHistory("egg burger");
			if (client.getHistory()==null) {
				thrown=true;
			}
			client.updateCoupon(1234567);
			if (client.getCoupon()!=1234567) {
				thrown=true;
			}
			if (client.ifclaim()==true) {
				thrown=true;
			}
			if (client.claim(1234567)==null) {
				thrown=true;
			}
			if (client.ifclaim()==false) {
				thrown=true;
			}
			client.deleteRecord("123456");
			
		}catch (Exception e) {
			thrown=true;
		}
		assertThat(thrown).isEqualTo(false);
	}

}