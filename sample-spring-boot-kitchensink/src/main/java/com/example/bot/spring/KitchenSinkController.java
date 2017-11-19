/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this  to you under the Apache License,
 * version 2.0 (the "License"); you may not use this  except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring;
//--------------------------------------------------//
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import java.net.*;
//import java.io.*;
//--------------------------------------------------//

import java.io.IOException;

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
import com.linecorp.bot.model.profile.UserProfileResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.io.ByteStreams;

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
import com.asprise.ocr.Ocr;
import java.sql.*;

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@LineMessageHandler
public class KitchenSinkController {
	


	@Autowired
	private LineMessagingClient lineMessagingClient;

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		log.info("This is your entry point:");
		log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		TextMessageContent message = event.getMessage();
		String replytoken = event.getReplyToken();
		String userId = event.getSource().getUserId();
		//if userld is not in the database
//		handleTextContent(replytoken, event, message.getText());
		try {
				int complete_indicator = client.isInfoComplete(userId);
			
				if(complete_indicator==0) {  // the user's info is full
					handleTextContent(replytoken, event, message.getText());
				}
				else {
					handleTextContent_newuser(replytoken,event,message.getText(),userId,complete_indicator);
				}
		 } catch (Exception e) {
			 	handleTextContent_newuser(replytoken,event,message.getText(),userId,1);
		 	}
	}

	@EventMapping
	public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
		handleSticker(event.getReplyToken(), event.getMessage());
	}

	@EventMapping
	public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
		LocationMessageContent locationMessage = event.getMessage();
		RestaurantAPI restaurantApi = new RestaurantAPI(locationMessage.getLatitude(),locationMessage.getLongitude());
		try {
			restaurantApi.serachRestaurant();
		}catch(Exception ex){
			reply(event.getReplyToken(),new TextMessage("Error: "+ex.toString()));
			return;
		}
		reply(event.getReplyToken(),new TextMessage("Restaurant nearby:\n"+restaurantApi.printRestaurant()));
	}

	@EventMapping
	public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
		final MessageContentResponse response;
		String replyToken = event.getReplyToken();
		String messageId = event.getMessage().getId();
		String userId = event.getSource().getUserId();
				
		try {

			int complete_indicator = client.isInfoComplete(userId);
		
			if(complete_indicator!=0) {  // the user's info is not full
				handleTextContent_newuser(replyToken,event,"",userId,complete_indicator);

				return;
			}
		} 
		catch (Exception e) {
			try {
				handleTextContent_newuser(replyToken,event,"",userId,1);
			}
			catch(Exception ex) {
				replyText(replyToken, e.getMessage());
			}
			return;
		}

		try {
			response = lineMessagingClient.getMessageContent(messageId).get();
		} catch (InterruptedException | ExecutionException e) {
			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
			throw new RuntimeException(e);
		}
		DownloadedContent jpg = saveContent("jpg", response);
		OcrApiController OcrApi = new OcrApiController();
		String imageText = OcrApi.recognize("URL",jpg.getUri());
		//this.replyText(replyToken, "Recognized message:\n"+imageText);
		if (imageText.equals(null)) {
			replyText(replyToken, "Cannot recognize image, Please try again.");
		}else {
			
			try {
				//this.replyText(replyToken, imageText);
				String reply ="";
				String[] parts = imageText.split(" ");
				for(String s :parts) {
					if (s.length()>2) {
						String result = mymenu.calculateNutrients(s,0.0);
						if (result != null) {
							reply += result;
						}
						if (reply.length()>900) {
							break;
						}
					}
				}
				if (reply!=null) {
					this.replyText(replyToken, reply);
				}else {
					this.replyText(replyToken, "Cannot find menu.");
				}
				
				}
			 catch (Exception e) {
				this.replyText(replyToken, "Error: "+e.getMessage());
			}
		}
	}
	
	@EventMapping
	public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) throws IOException {
		final MessageContentResponse response;
		String replyToken = event.getReplyToken();
		String messageId = event.getMessage().getId();
		try {
			response = lineMessagingClient.getMessageContent(messageId).get();
		} catch (InterruptedException | ExecutionException e) {
			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
			throw new RuntimeException(e);
		}
		DownloadedContent mp4 = saveContent("mp4", response);
		reply(event.getReplyToken(), new AudioMessage(mp4.getUri(), 100));
	}

	@EventMapping
	public void handleUnfollowEvent(UnfollowEvent event) {
		log.info("unfollowed this bot: {}", event);
	}

	@EventMapping
	public void handleFollowEvent(FollowEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got followed event");
	}

	@EventMapping
	public void handleJoinEvent(JoinEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Joined " + event.getSource());
	}

	@EventMapping
	public void handlePostbackEvent(PostbackEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got postback " + event.getPostbackContent().getData());
	}

	@EventMapping
	public void handleBeaconEvent(BeaconEvent event) {
		String replyToken = event.getReplyToken();
		this.replyText(replyToken, "Got beacon message " + event.getBeacon().getHwid());
	}

	@EventMapping
	public void handleOtherEvent(Event event) {
		log.info("Received message(Ignored): {}", event);
	}

	private void reply(@NonNull String replyToken, @NonNull Message message) {
		reply(replyToken, Collections.singletonList(message));
	}

	private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
			log.info("Sent messages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void push(@NonNull String to, @NonNull List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.pushMessage(new PushMessage(to, messages)).get();
			log.info("Sent pushmessages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void pushImage(@NonNull String to, @NonNull Message messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.pushMessage(new PushMessage(to, messages)).get();
			log.info("Sent pushmessages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	private void pushText(@NonNull String to, @NonNull String message) {
		if (to.isEmpty()) {
			throw new IllegalArgumentException("to must not be empty");
		}
		if (message.length() > 1000) {
			message = message.substring(0, 1000 - 2) + "..";
		}
		TextMessage temp = new TextMessage(message);
		this.push(to, Collections.singletonList(temp));
	}

	
	private void replyText(@NonNull String replyToken, @NonNull String message) {
		if (replyToken.isEmpty()) {
			throw new IllegalArgumentException("replyToken must not be empty");
		}
		if (message.length() > 1000) {
			message = message.substring(0, 1000 - 2) + "..";
		}
		this.reply(replyToken, new TextMessage(message));
	}


	private void handleSticker(String replyToken, StickerMessageContent content) {
		reply(replyToken, new StickerMessage(content.getPackageId(), content.getStickerId()));
	}

    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	public static boolean isNumberic(String str)
	{
		for (int i = 0; i < str.length(); i++){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
	// if the dish is in the database, it will return the string(description), otherwise, it will return null, so that we insert the dish into the database.
	private String menu_handler(String text, int price,String ingredients) {
		 String reply = null;
		 if (insert_mode == null) {
			 reply = mymenu.calculateNutrients(text,0);
		 }
		 else	 {
			 mymenu.insertMenu(text,price,ingredients);
		 }
			 return reply;
		 

	}
	
	private void handleTextContent_newuser(String replytoken, Event event, String text,String userId,int complete_indicator)
			throws Exception {
					String replytext = null;
					caseCounter=0;
					client.loadClient(userId);
					
					if(complete_indicator==1) {   //there is no information at all
						client.addClient(userId);
						replytext = "Welcome to the diet chatbot, system detects it is your first time to use the system, please create personal file.";
						replytext += '\n';  
						replytext += '\n';
						replytext += "Please input your name";
						this.replyText(replytoken, replytext);	
					}
					else if(complete_indicator==2) { //input the name
						
							client.updateName(text);
							
							replytext = "Great, you have just inputed the name, now please input you age";
							this.replyText(replytoken, replytext);	
					}
					else if(complete_indicator==3) {
						boolean temp = isNumberic(text);
						
						if(temp==true) {
							int i = Integer.parseInt(text);
							client.updateAge(i);
							// insert i to database
							replytext = "Great, you have just inputed the age, now please input you gender";
							replytext +='\n';
							replytext +='\n';
							replytext += "The gender should be 'men' or 'women'";
							this.replyText(replytoken, replytext);	
						}
						else {
							replytext = "sorry, you input may contain none interger letters, like a space, please re-input your age";

							this.replyText(replytoken, replytext);	
						}
					}
					
					else if(complete_indicator==4) {
					   if( text.equals("women") || text.equals("men") ) {
							client.updateGender(text);
						
							replytext = "Great, you have just inputed the gender, now please input you height(m)";
							this.replyText(replytoken, replytext);	
						}
						else {
							replytext = "sorry, you input is not 'men' nor 'women', please re-input you gender";
							this.replyText(replytoken, replytext);	
						}
					}
					else if(complete_indicator==5) {
						boolean temp = isDouble(text);
						
						if(temp==true) {
							double i = Double.parseDouble(text);
							client.updateHeight(i);
							// insert i to database
							replytext = "Great, you have just inputed the height, now please input you weight(Kg)";
							this.replyText(replytoken, replytext);	
						}
						else {
							replytext = "sorry, you input may contain none interger letters, like a space, please re-input your height";
							this.replyText(replytoken, replytext);	
						}
					}
					else {  //the last case, tell the user to update the weight
						boolean temp = isDouble(text);
						
						if(temp==true) {
							double i = Double.parseDouble(text);;
							client.updateWeight(i);
							// insert i to database
							replytext = "Great, you have just inputed the final piece of information, please type hi to start";
							this.replyText(replytoken, replytext);	
						}
						else {
							replytext = "sorry, you input may contain none interger letters, like a space, please re-input your weight";
							this.replyText(replytoken, replytext);	
						}
					}
					
		}
	
	
	private void handleTextContent(String replyToken, Event event, String text)
throws Exception{
        String userId = event.getSource().getUserId();
        client.loadClient(userId);
        log.info("Got text message from {}: {}", replyToken, text);
        
        //-----------------------------------------------------------//
       	try {//it gives error when url is https://
       		if (text != "https://") {
       			URL url = new URL(text);
       			JsonHandler jsonHandler = new JsonHandler(text);
       			Quote[] quote = jsonHandler.getQuote();
       			String stackmessage = null;
       			for(Quote q: quote) {
       				if (menu_handler(q.getName(), q.getPrice(), q.getIngredients()) != null) {
       					stackmessage = stackmessage + q.printString() + "\n" + "is found in database an have detailed info as below\n" + menu_handler(q.getName(), q.getPrice(), q.getIngredients()) + "\n\n";
       				}
       				else {
       					if(insert_mode == null) {
       						stackmessage = stackmessage + q.printString() + "\n" + "is not found in database\n\n";
       					}
       					else {
       						stackmessage = stackmessage + q.printString() + "\n" + "is inserted into database\n\n";
       					}
       				}
       			}
       			this.replyText(replyToken, stackmessage);
       			return;
       		}
       	}catch(MalformedURLException e) {
       		log.info("url handle json failed, perhaps not a real url");
      	}
        //-----------------------------------------------------------//

        switch (text) {
            case "profile": {
                	String reply = null;
                	double  bmi=client.calculateBMI();
                	reply = client.getProfile();
                	reply += '\n';
                	reply +="BMI:";
                	reply += String.valueOf(bmi);
                	caseCounter=1;
                	this.replyText(replyToken,reply);
                break;
            }
            
            case "search":{
            	caseCounter =3 ;
            	String reply = null;
            	reply = "Now please input the dish name you want to search. ";
            	reply += "\n\n";
            	reply += "Remember, if you want to search again, you have to retype keyword search";
            	this.replyText(replyToken, reply);
            	break;
            }
            
            case "insert":{
            	insert_mode = "insert";
            	caseCounter=2;
            	String reply = null;
            	reply = "Now please input the menu infomation as following format:";
            	reply += "\n";
            	reply += "\n";
            	reply += "first line:dish name";
            	reply += "second line:price(must be interger)";
            	reply += "third line:gredients";
            	this.replyText(replyToken, reply);
            	break;
            }
            
            case "uninsert":{
            	insert_mode = null;
            	caseCounter=8;
            	
            	String reply = null;
            	reply = "Now you have exit the insert mode";
            	
            	this.replyText(replyToken, reply);
            	
            	break;
            }
            
            case "history": {
                try  {
                	caseCounter=4;
                	String reply = null;
                	reply = client.getHistory();
                	this.replyText(replyToken,reply);
                } catch(Exception e)  {
                    this.replyText(replyToken, "No History Found!");
                }
                break;
            }
            //new requirement
            
            case "friend":{
            	String reply = null;
            	String temp = null;
            	caseCounter=10;
            	long n_coupon = client.getCoupon();
      	 		if (coupon_number==5000) {
      	 			reply = "Sorry, there are 5000 coupons already, the campaign is over.";
      	 			caseCounter = 8;
      	 			this.replyText(replyToken,reply);
      	 			break;
      	 		}
            	if (n_coupon == -1) 
            	{
            		long digit = System.currentTimeMillis();
            		digit = digit % 1000000;
            	
            		reply = "Your 6-digit code is: ";
            		temp = String.format("%06d", digit);
            		client.updateCoupon(digit);
            		reply += temp;
            	}
            	else {
            		reply = "You have already got one:";
            		reply += String.valueOf(n_coupon);
            	}
            	this.replyText(replyToken,reply);
            	break;
            }
            
            case "code":{
            	String reply = null;
            	caseCounter=11;
      	 		boolean if_claim = client.ifclaim();
      	 		if (if_claim) {
      	 			reply = "Sorry, you have already claimed the coupon.";
      	 			caseCounter = 8;
      	 			this.replyText(replyToken,reply);
      	 			break;
      	 		}
      	 		
      	 		if (coupon_number==5000) {
      	 			reply = "Sorry, there are 5000 coupons already, the campaign is over.";
      	 			caseCounter = 8;
      	 			this.replyText(replyToken,reply);
      	 			break;
      	 		}
      	 		
            	reply = "Please type in the 6-digit code ";
        
            	this.replyText(replyToken,reply);
            	break;
            }
            case "recommend dish":{
            	String reply=null;
            	caseCounter=12;
            	reply=mymenu.getRecommendDish(userId,client.calculateBMI());
            	this.replyText(replyToken,reply);
            	break;
            }
           
            
            case "add history":{
            	caseCounter=5;
            	this.replyText(replyToken,"What do you eat today?");
            	break;
            }
            case "calculate nutrients":{
            	caseCounter=9;
            	this.replyText(replyToken, "please type the dish name, or you can type both the name and weight(g) of dish seperated by a ','\n"
            			+ "for example: chilli chicken,135");
            	break;
            }
            
            case "recommend serving": {
            	caseCounter=6;
            	String clientagerange=null;
            	int clientage=client.getAge();
            	if (clientage<=50) {
            		clientagerange="19-50";
            	}
            	else if (clientage>50 && clientage<=70) {
            		clientagerange="51-70";
            	}
            	else
            		clientagerange="70+";
                try  {
                	String reply = null;
                	reply = mymenu.getRecommendServing(client.getGender(),clientagerange,false);
                	this.replyText(replyToken,reply);
                } catch(Exception e)  {
                    this.replyText(replyToken, "Bot can't use profile,something wrong!");
                }
                break;
            }
            

            case "hi":{
            	String replya = null;
            	String replyb = null;
            	String replyc = null;
            	String replyd = null;
            	String replye = null;

            	caseCounter=7;
            	List<Message> megs = new ArrayList<Message>();

            
                 replya = "Welcome back to the diet chatbot!";
                 replya += '\n';
                 replya += '\n';
                 replya += "There are several functions you can use, to use the function, just type the keyword";
                 replya += '\n';
                 replya += '\n';
                 replya += "You can send the location, and we will sugest the best healthy restaurants near you!!";
                 replya += '\n';
                 replya += '\n';
                 replya += "Keyword: profile ";
                 replya += '\n';
                 replya += '\n';
                 replya += "It will provide you the personal health information";

                 replyb = "Keyword:friend";
                 replyb += '\n';
                 replyb += '\n';
                 replyb += "It will generate the coupon for you";
                 replyb += "Keyword: code ";
                 replyb += '\n';
                 replyb += '\n';
                 replyb += "After type \"code\", you can input the 6-digit number to receive coupon";
                		 
                		 
                 replyc = "Keyword: history ";
                 replyc += '\n';
                 replyc += '\n';
                 replyc += "It will provide you the personal wegiht history and food history\n\n";
                 replyc += "Keyword: add history \n\n";
                 replyc += "It will let you input what you eat today and record the eating history\n\n";
                 
                 replyd = "Keyword: recommend serving";
                 replyd += "\n\n";
                 replyd += "It will provide you the recommend daily serving\n\n";
                 replyd +="keyword: calculate nutrients\n\n ";
                 replyd +="It will let you input the name of dish then provide you the nutrients details.\n\n";
                 
                 replye = "If you want to search some dish in database, type \"search\" first, at then type the dish name";
                 replye += '\n';
                 replye += '\n';
                 replye += "In addition, you can simply type the meal image and url as you wish. The chatbot will return you the content";
                 replye += '\n';
                 replye += '\n';
                 replye += "However, if you want to insert the dish into the menu, please first input keyword:insert to change to insert mode, then type the dish name or url."; 
                 replye += '\n';
                 replye += '\n';
                 replye += "If you want to stop insert, type keyword:uninsert";
            	
             	Message a=  new TextMessage(replya);
             	Message b = new TextMessage(replyb);
             	Message c=  new TextMessage(replyc);
             	Message d = new TextMessage(replyd);
             	Message e=  new TextMessage(replye);

             	megs.add(a);
             	megs.add(b);
             	megs.add(c);
             	megs.add(d);
             	megs.add(e);

                 
                 this.reply(replyToken, megs);	

               break;
              }

              default:{
              	 	String reply = null;
              	 	
              	 	if(caseCounter == 11) {    //handle the input 6-digit case
              	 		caseCounter=8;

              	 		long i = Long.parseLong(text);
              	 		
              	 		List<String> result = client.claim(i);
              	 		              	 		
              	 		if (result.isEmpty()) {
              	 				reply = "Invalid code, please type \"code\" to start the process again.";
              	 				this.replyText(replyToken, reply);
              	 				break;
              	 		}
              	 		else if (result.contains(userId)) {
              	 			reply="This is your own code, please type \"code\" to start the process again.";
              	 			this.replyText(replyToken, reply);
          	 				break;
              	 		}
              	 		else {
              	 			result.add(userId);
              	 			String imageUrl = createUri("/static/buttons/a.jpg");
                        	ImageMessage img_reply =new ImageMessage(imageUrl,imageUrl);
              	 			
                        	for(String m:result) { 
                        		if (coupon_number==5000) {
                        			break;
                        		}
                        		this.pushImage(m, img_reply);
                        		coupon_number +=1;
                        	}
                        	break;
              	 		}
              	 	
              	 	}
              	 	
              	 	if (caseCounter==5) {
              	 		try {
              	 			caseCounter=8;
              	 			client.addHistory(text);
              	 			this.replyText(replyToken,"new history added successfully!");
              	 		}catch (Exception e){
              	 			this.replyText(replyToken, "Something goes wrong recording history...");
              	 		}
              	 		break;
              	 	}
              	 	if (caseCounter==9) {
              	 		caseCounter=8;
              	 		int weight=0;
              	 		String[] words=text.split(",");
              	 		if (words.length==2)
              	 			weight=Integer.parseInt(words[1]);
              	 		String replyline=mymenu.calculateNutrients(words[0],weight);
              	 		this.replyText(replyToken, replyline);
              	 	}
              	 	
              	 	if(caseCounter == 2) {
              	 		String[] words = text.split("\n");
              	 		
              	 		if (words.length != 3) {
              	 			reply = "You must follow the format, please type \" insert \" ";
              	 			caseCounter = 8;
              	 			this.replyText(replyToken, reply);
              	 			break;
              	 		}
						boolean temp = isNumberic(words[1]);
						              	 		
              	 		if (temp==true) {
              	 			mymenu.insertMenu(words[0], Integer.parseInt(words[1]),words[2]);
              	 			reply = "The dish name has been inputted into the menu sucessfully.";
              	 		}
              	 		else {
              	 			reply = "The price you entered is not double format, plase do it again";
              	 		}
              	 		
          	 			this.replyText(replyToken, reply);
          	 			break;
              	 	}
              	 	
              	 	if(caseCounter == 3) {
              	 		caseCounter = 8;
              	 		reply = mymenu.calculateNutrients(text,0);
                  	 	
              	 		if( reply==null ) {            
                  	 		reply = "Sorry, we could not find the dish name you input";
                  	 	}
              	 		
          	 			this.replyText(replyToken, reply);
          	 			break;
              	 	}
              	 	
              	 
              	 	
              	 	reply = "Sorry, we could recongize your input, if you want help, please type hi";
              	 	this.replyText(replyToken,reply);
              	 	log.info("Returns echo message {}: {}", replyToken, reply);
              	 	break;
              }
        }
	}

	static String createUri(String path) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUriString();
	}
	
	private void system(String... args) {
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		try {
			Process start = processBuilder.start();
			int i = start.waitFor();
			log.info("result: {} =>  {}", Arrays.toString(args), i);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (InterruptedException e) {
			log.info("Interrupted", e);
			Thread.currentThread().interrupt();
		}
	}

	private static DownloadedContent saveContent(String ext, MessageContentResponse responseBody) {
		log.info("Got content-type: {}", responseBody);

		DownloadedContent tempFile = createTempFile(ext);
		try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
			ByteStreams.copy(responseBody.getStream(), outputStream);
			log.info("Saved {}: {}", ext, tempFile);
			return tempFile;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static DownloadedContent createTempFile(String ext) {
		String fileName = LocalDateTime.now().toString() + '-' + UUID.randomUUID().toString() + '.' + ext;
		Path tempFile = KitchenSinkApplication.downloadedContentDir.resolve(fileName);
		tempFile.toFile().deleteOnExit();
		return new DownloadedContent(tempFile, createUri("/downloaded/" + tempFile.getFileName()));
	}


	


	public KitchenSinkController() {
		itscLOGIN = System.getenv("ITSC_LOGIN");
		client = new Client();
		mymenu=new menu();
		caseCounter=0;
		coupon_number =0 ;
		insert_mode = null;
	}

	private String itscLOGIN;
	private Client client;
	private String insert_mode;
	private menu mymenu;
	private int caseCounter;
	private int coupon_number;
	//The annontation @Value is from the package lombok.Value
	//Basically what it does is to generate constructor and getter for the class below
	//See https://projectlombok.org/features/Value
	@Value
	public static class DownloadedContent {
		Path path;
		String uri;
	}


	//an inner class that gets the user profile and status message
	class ProfileGetter implements BiConsumer<UserProfileResponse, Throwable> {
		private KitchenSinkController ksc;
		private String replyToken;
		
		public ProfileGetter(KitchenSinkController ksc, String replyToken) {
			this.ksc = ksc;
			this.replyToken = replyToken;
		}
		@Override
    	public void accept(UserProfileResponse profile, Throwable throwable) {
    		if (throwable != null) {
            	ksc.replyText(replyToken, throwable.getMessage());
            	return;
        	}
        	ksc.reply(
                	replyToken,
                	Arrays.asList(new TextMessage(
                		"Display name: " + profile.getDisplayName()),
                              	new TextMessage("Status message: "
                            		  + profile.getStatusMessage()))
        	);
    	}
    }
	
	

}