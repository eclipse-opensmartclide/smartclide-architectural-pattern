package eu.smartclide.architecturalpatterns.selection;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.smartclide.architecturalpatterns.supportedpatterns.ArchitecturalPatterns;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.EnumMap;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.lang.NullPointerException;

@RestController
public class PatternSelectionController {
	
	EnumMap<ArchitecturalPatterns, Integer> patternValues = new EnumMap<>(ArchitecturalPatterns.class);
	
	@PostMapping("/evaluate")
	public String evaluateSurveyInput(@RequestBody String input){

		//Initialize 
		patternValues.put(ArchitecturalPatterns.LAYERED, 0);
		patternValues.put(ArchitecturalPatterns.EVENT_DRIVEN, 0);
		patternValues.put(ArchitecturalPatterns.MICROKERNEL, 0);
		patternValues.put(ArchitecturalPatterns.MICROSERVICES, 0);
		patternValues.put(ArchitecturalPatterns.SERVICE_ORIENTED, 0);
		patternValues.put(ArchitecturalPatterns.SPACED_BASED, 0);
		
		ObjectMapper mapper = new ObjectMapper();
	
		//Read evaluation tables from JSON file
		final String EVALUATION_JSON_FILE_PATH = 
				"jsonfiles/surveyEvaluationTables.json";
		
		Path filePath = Path.of(EVALUATION_JSON_FILE_PATH);
		
		//JsonNode inputJson = null;
		JsonNode valuesJsonNode = null;
	
		try {
			
			if (input == null) {
				throw new NullPointerException("Survey input is empty.");
			} else {
				//JsonNode inputJson = mapper.readTree(input);
			
				//Get survey question IDs from into string list
				List<String> ids = mapper.readValue(input, new TypeReference<List<String>>(){});
				
				String jsonStr = Files.readString(filePath);
				JsonNode jsonNode = mapper.readValue(jsonStr, JsonNode.class);			
				
				//Iterate over survey question IDs
				ListIterator<String> itr = ids.listIterator();
			
				while (itr.hasNext()) {		
					String next = itr.next();
				    valuesJsonNode = jsonNode.get(next).get(0);	
				    	/*FIXME
						//Loop through the evaluation values for current id
						for (ArchitecturalPatterns pattern : ArchitecturalPatterns.values()) {
							int currentValue = patternValues.get(pattern);
							int newValue = currentValue + valuesJsonNode.get(pattern.toString()).asInt();
							patternValues.put(pattern, newValue);						
						}
						*/
				}
			}
		 } catch (IOException e) {
			 e.printStackTrace();
			 }

		return patternValues.toString();
		/*
		return valuesJsonNode.toString() + 
				"\n" + valuesJsonNode.get(ArchitecturalPatterns.LAYERED.toString()) + 
				"\n" + patternValues.get(ArchitecturalPatterns.LAYERED).toString();
		*/
	}
	
		
}
