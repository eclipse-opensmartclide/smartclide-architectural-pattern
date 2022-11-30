package org.eclipse.opensmartclide.architecturalpatterns.selection;

import org.eclipse.opensmartclide.architecturalpatterns.supportedpatterns.ArchitecturalPatterns;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.NullPointerException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

@RestController
public class PatternSelectionController {
	
	EnumMap<ArchitecturalPatterns, Integer> patternValues = new EnumMap<>(ArchitecturalPatterns.class);
	
	final URL url = this.getClass().getResource("/jsonfiles/surveyEvaluation.json");
	
	@PostMapping("/evaluation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String evaluateSurveyInput(@RequestBody String input){
		
		if (input == null) {
			throw new NullPointerException("Survey input is empty.");
			
		} else {

			//Initialize 
			patternValues.put(ArchitecturalPatterns.LAYERED, 0);
			patternValues.put(ArchitecturalPatterns.EVENT_DRIVEN, 0);
			patternValues.put(ArchitecturalPatterns.MICROKERNEL, 0);
			patternValues.put(ArchitecturalPatterns.MICROSERVICES, 0);
			patternValues.put(ArchitecturalPatterns.SERVICE_ORIENTED, 0);
			patternValues.put(ArchitecturalPatterns.SPACE_BASED, 0);
		
			List<ArchitecturalPatterns> patternList = Arrays.asList(ArchitecturalPatterns.values());
		
			JsonNode valuesJsonNode = null;	
			ObjectMapper mapper = new ObjectMapper();
	
			try {	
			
				//Read evaluation values from JSON file
				final Path filePath = Path.of(Objects.requireNonNull(url).toURI());
				String jsonStr = Files.readString(filePath);
				JsonNode jsonNode = mapper.readValue(jsonStr, JsonNode.class);	
			
				//Get survey question IDs from into string list
				List<String> ids = mapper.readValue(input, new TypeReference<List<String>>(){});
				
				//Iterate over survey question IDs
				ListIterator<String> itr = ids.listIterator();	
			
				while (itr.hasNext()) {		
					String next = itr.next();
					valuesJsonNode = jsonNode.get(next).get(0);	
			    
					for (ArchitecturalPatterns pattern : patternList) {							
						int currentValue = patternValues.get(pattern);
						int newValue = currentValue + valuesJsonNode.get(pattern.name()).asInt();
						//Update evaluation value for pattern 
						patternValues.put(pattern, newValue);
					}		
				}

			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}

			return patternValues.toString();
		}	
	}
}
