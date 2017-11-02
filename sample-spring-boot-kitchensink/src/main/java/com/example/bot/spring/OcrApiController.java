package com.example.bot.spring;

import com.asprise.ocr.Ocr;
import java.io.File;
import java.net.*;

public class OcrApiController {
	//input type
	public final String TYPE_URL = "URL";
	public final String TYPE_IMG = "IMG";
	
	//just some constant for testing, can ignore this
	private final String FILEURL = "http://asprise.com/ocr/img/test-image.png";
	private final String FILENAME = "/app/sample-spring-boot-kitchensink/src/main/resources/static/test-image.png";
	
	//testing function
	public String recognize(String type,String type_content) {
		Ocr.setUp(); // one time setup
		Ocr ocr = new Ocr(); // create a new OCR engine
		ocr.startEngine(Ocr.LANGUAGE_ENG, Ocr.SPEED_SLOW); // English,best accuracy
		String testing_result ="";
		try {
			switch(type) {
			case TYPE_URL:
				testing_result = ocr.recognize(new URL[] {new URL(type_content)}, Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
				break;
			case TYPE_IMG:
				testing_result = ocr.recognize(new File[] {new File(type_content)}, Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
				break;
			default:
				testing_result="Wrong Input type.";
			}
			ocr.stopEngine();
		} catch (Exception e) {
			return e.toString();
		}
		return testing_result;
	}
}
