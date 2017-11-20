package com.example.bot.spring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import java.net.*;
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

import com.linecorp.bot.client.*;

import java.io.IOException;
import java.time.Instant;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.ArrayList;
import java.time.LocalDate;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.event.source.UnknownSource;
import com.linecorp.bot.model.message.AudioMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.linecorp.bot.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.io.ByteStreams;

import com.asprise.ocr.Ocr;
import java.sql.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.OngoingStubbing;

import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ControllerTester.class})
public class ControllerTester {


//	@Test
//	public void testhelpfunction1()  throws Exception{
//		boolean thrown = false;
//		
//		KitchenSinkController ksc = new KitchenSinkController();
//			String ha = "123.35";
//			boolean result = ksc.isDouble(ha);
//
//			if(result==false) {
//				thrown = true;
//			}
//			System.out.println("there is exception");
//	
//			assertThat(thrown).isEqualTo(false);
//	}
//	
//	@Test
//	public void testhelpfunction2()  throws Exception{
//		boolean thrown = false;
//		
//		KitchenSinkController ksc = new KitchenSinkController();
//			String ha = "123";
//			boolean result = ksc.isNumberic(ha);
//
//			if(result==false) {
//				thrown = true;
//			}
//	
//			assertThat(thrown).isEqualTo(false);
//	}
//	
//
//	
//	@Test
//	public void testhelpfunction3()  throws Exception{
//		boolean thrown = false;
//		
//		KitchenSinkController ksc = new KitchenSinkController();
//			String ha = "123";
//			String result = ksc.menu_handler(ha,1,ha);
//			ksc.setvalues("insert",0,0);
//			String result2 = ksc.menu_handler(ha,1,ha);
//			assertThat(thrown).isEqualTo(false);
//	}
//	
//	@Test
//	public void test_handlenewuser()  throws Exception{
//		boolean thrown = false;
//		
//		KitchenSinkController ksc = new KitchenSinkController();
//			String replytoken = "123";
//			String text = "women";
//			String text2 = "000";
//			String userid = "12";
//			for (int i = 1; i<7;i++) {
//				String result = ksc.handleTextContent_newuser(replytoken,text,userid,i);
//				String result2 = ksc.handleTextContent_newuser(replytoken,text2,userid,i);
//				if(result==null || result2 == null) {
//					thrown = true;
//				}
//			}
//			assertThat(thrown).isEqualTo(false);
//	}
//	
//	@Test
//	public void test_handletextcontent()  throws Exception{
//		boolean thrown = false;
//		
//		KitchenSinkController ksc = new KitchenSinkController();
//			String replytoken = "123";
//			String[] text = {"profile","json","insert","uninsert","search","add history","history","recommend dish","calculate nutrients","recommend serving",};
//			String userid = "12";
//			for (String m:text) {
//				String result = ksc.handleTextContent(replytoken,userid,m);
//				if(result==null) {
//					thrown = true;
//				}
//			}
//			//test for coupon ==5000 with code and friend
//			ksc.setvalues(null,0,5000);
//			String result2 = ksc.handleTextContent(replytoken,userid,"code");
//			String result3 = ksc.handleTextContent(replytoken,userid,"friend");
//			assertThat(thrown).isEqualTo(false);
//	}
//	



//	public void test_handletextcontent2()  throws Exception{
//		boolean thrown = false;
//		
//		KitchenSinkController ksc = new KitchenSinkController();
//			String replytoken = "123";		
//			String userid = "12";
//			int [] caseCounter = {2,3,5,9,11,20};
//			for (int i:caseCounter) 
//			{	
//				ksc.setvalues(null,i,0);
//				String result4= ksc.handleTextContent(replytoken,userid,"1");
//			}
//
//			assertThat(thrown).isEqualTo(false);
//	}
	
//	@Test
//	public void test_handlecontent_json()  throws Exception{
//		boolean thrown = false;
//		
//		KitchenSinkController ksc = new KitchenSinkController();
//			String replytoken = "123";
//			String text = "http://wwwabd-efcom.000webhostapp.com/data.json";
//			String userid = "12";
//			ksc.setvalues(null,20,0);
//			String result = ksc.handleTextContent(replytoken,userid,text);
//			ksc.setvalues("insert",20,0);
//			result = ksc.handleTextContent(replytoken,userid,text);
//			if(result==null ) {
//					thrown = true;
//				}
//			
//			assertThat(thrown).isEqualTo(false);
//	}	

//	@Test
//	public void test_coupon()  throws Exception{
//		boolean thrown = false;
//		
//		KitchenSinkController ksc = new KitchenSinkController();
//			String replytoken = "123";
//			String userid = "1";
//			String digit = ksc.handleTextContent(replytoken,userid,"friend");
//			digit=digit.substring(digit.length()-6);
//			String result = ksc.handleTextContent(replytoken,userid,"friend");
//			 result = ksc.handleTextContent(replytoken,userid,"code");
//			 result = ksc.handleTextContent(replytoken,userid,digit);
//			 result = ksc.handleTextContent(replytoken,"2","code");
//			 result = ksc.handleTextContent(replytoken,"2","123456");
//			 result = ksc.handleTextContent(replytoken,"2","code");
//			 result = ksc.handleTextContent(replytoken,"2",digit);
//			 result = ksc.handleTextContent(replytoken,"2","friend");
//			if(result==null ) {
//					thrown = true;
//				}
//			
//			assertThat(thrown).isEqualTo(false);
//	}		
	
	
	@Test
	public void test_system()  throws Exception{
		boolean thrown = false;

		KitchenSinkController ksc = new KitchenSinkController();
		KitchenSinkController.DownloadedContent temp;
	
		temp = ksc.createTempFile(" ");
		assertThat(thrown).isEqualTo(false);
	}
	
//	@Test
//	public void testreplytext()  throws Exception{
//		boolean thrown = false;
//		 whenCall(retrofitMock.pushMessage(any()),BOT_API_SUCCESS_RESPONSE);
//		KitchenSinkController ksc = new KitchenSinkController();
//			String replyToken = "asd";
//			String message = new String();
//			message="random";
//			System.out.println(message.length());
//			try{
//				ksc.replyText(replyToken,"random");
//			}
//			catch(Exception e) {
//				thrown = false;
//			}
//			assertThat(thrown).isEqualTo(false);
//	}
//	
	
	
	

	@Test
	public void testTextimageSearcher()  throws Exception{
		boolean thrown = false;
		
		KitchenSinkController ksc = new KitchenSinkController();
		String result=ksc.imagetextSearcher("http://asprise.com/ocr/img/test-image.png");
		System.out.println(result);
		if (result==null) {
			thrown = true;
		}
		assertThat(thrown).isEqualTo(false);
	}
}
