package com.example.bot.spring;

import com.asprise.ocr.Ocr;
import java.io.File;

public class OcrApiController {
	private final String FILENAME = "/static/test-image.png";
	
	public String test() {
		Ocr.setUp(); // one time setup
		Ocr ocr = new Ocr(); // create a new OCR engine
		ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
		String testing_result = ocr.recognize(new File[] {new File(FILENAME)}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
		//System.out.println("Result: " + testing_result);
		// ocr more images here ...
		ocr.stopEngine();
		return testing_result;
	}
}
