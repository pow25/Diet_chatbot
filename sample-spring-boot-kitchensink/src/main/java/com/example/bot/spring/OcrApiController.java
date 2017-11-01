package com.example.bot.spring;

import com.asprise.ocr.Ocr;
import java.io.File;
import java.net.*;

public class OcrApiController {
	private final String FILEURL = "http://asprise.com/ocr/img/test-image.png";
	private final String FILENAME = "test.jpg";
	
	public String test(String input_type,String uri) {
		Ocr.setUp(); // one time setup
		Ocr ocr = new Ocr(); // create a new OCR engine
		ocr.startEngine("eng", Ocr.SPEED_SLOW); // English
		String testing_result = new String();
		try {
			if (input_type.equals("URL")) {
				testing_result = ocr.recognize(new URL[] {new URL(FILEURL)}, Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
			}else if(input_type.equals("Img")){
				testing_result = ocr.recognize(new File[] {new File(uri)}, Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
			}else {
				testing_result = ocr.recognize(new File[] {new File(FILENAME)},  Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
			}
			ocr.stopEngine();
		} catch (Exception e) {
			return e.toString();
		}
		return testing_result;
	}
}
