package com.example.bot.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import java.util.Calendar;
import java.util.*;

import com.example.bot.spring.menu;


@RunWith(SpringRunner.class)

@SpringBootTest(classes = {menuTester.class,menu.class })
public class menuTester{
	@Autowired
	private menu testMenu;
	@Test
	public void test() throws Exception{
		boolean thrown=false;
		try {
			testMenu.insertMenu("double cheeze hamburger",24,"bread,cheeze,ham");
			
			if (testMenu.getRecommendServing("men","19-50",false)==null) {
				thrown=true;
			}
			if (testMenu.getRecommendDish("1234567",-1)==null) {
				thrown=true;
			}
			if (testMenu.getRecommendDish("1234567",10)==null) {
				thrown=true;
			}
			if (testMenu.getRecommendDish("1234567",20)==null) {
				thrown=true;
			}
			if (testMenu.getRecommendDish("1234567",30)==null) {
				thrown=true;
			}
			if (testMenu.calculateNutrients("abc",0)==null) {
				thrown=true;
			}
			if (testMenu.calculateNutrients("abc",100)==null) {
				thrown=true;
			}
		}catch (Exception e) {
			thrown=true;
		}
		assertThat(thrown).isEqualTo(false);
	}
}

