package com.example.bot.spring;

import com.asprise.ocr.Ocr;
import java.io.File;
import java.net.*;

public class OcrApiController {
	private final String FILENAME = "/static/test-image.png";
	
	public String test() {
		Ocr.setUp(); // one time setup
		Ocr ocr = new Ocr(); // create a new OCR engine
		ocr.startEngine("eng", Ocr.SPEED_SLOW); // English
		String testing_result = new String();
//		URL[] testURL = {new URL("http://asprise.com/ocr/img/test-image.png")};
		try {
			testing_result = ocr.recognize(new File[] {new File(FILENAME)}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
			ocr.stopEngine();
		} catch (Exception e) {
			return e.toString();
		}
		return testing_result;
	}
}
