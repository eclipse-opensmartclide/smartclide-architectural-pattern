package org.eclipse.opensmartclide.architecturalpatterns.selection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class SurveyController {
	
	private final String SURVEY_JSON_FILE_PATH =
			"jsonfiles/survey.json";
	
	ObjectMapper mapper = new ObjectMapper();
	
	@GetMapping("/survey")
	public String getSurvey() {
		 Path filePath = Path.of(SURVEY_JSON_FILE_PATH);
		 //System.out.println(filePath);
		 //JsonNode jsonNode = null;
		 String surveyJsonStr = null;
		 
		 try {
			 surveyJsonStr = Files.readString(filePath);
			 //jsonNode = mapper.readTree(surveyJsonStr);			 
			 
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		 return surveyJsonStr;
	}
	
}