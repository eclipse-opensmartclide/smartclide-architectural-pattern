package org.eclipse.opensmartclide.architecturalpatterns.selection;

import org.eclipse.opensmartclide.architecturalpatterns.supportedpatterns.ArchitecturalPatterns;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

@RestController
public class PatternSelectionController {

	EnumMap<ArchitecturalPatterns, Integer> patternValues = new EnumMap<>(ArchitecturalPatterns.class);

	final URL url = this.getClass().getResource("/jsonfiles/surveyEvaluation.json");

	@PostMapping(value = "/evaluation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String evaluateSurveyInput(@RequestBody List<String> input) {

		// Initialize
		patternValues.put(ArchitecturalPatterns.LAYERED, 0);
		patternValues.put(ArchitecturalPatterns.EVENT_DRIVEN, 0);
		patternValues.put(ArchitecturalPatterns.MICROKERNEL, 0);
		patternValues.put(ArchitecturalPatterns.MICROSERVICES, 0);
		patternValues.put(ArchitecturalPatterns.SERVICE_ORIENTED, 0);
		patternValues.put(ArchitecturalPatterns.SPACE_BASED, 0);

		JsonNode valuesJsonNode = null;
		ObjectMapper mapper = new ObjectMapper();

		try {

			// Read evaluation values from JSON file
			final Path filePath = Path.of(Objects.requireNonNull(url).toURI());
			String jsonStr = Files.readString(filePath);
			JsonNode jsonNode = mapper.readValue(jsonStr, JsonNode.class);

			// Iterate over survey question IDs
			for (String id : input) {
				if (jsonNode.get(id) == null) {
					throw new IllegalArgumentException(
							"Invalid survey input received: " + id + "is not a valid item ID.\n");
				}
				valuesJsonNode = jsonNode.get(id).get(0);

				for (ArchitecturalPatterns pattern : ArchitecturalPatterns.values()) {
					int currentValue = patternValues.get(pattern);
					int newValue = currentValue + valuesJsonNode.get(pattern.name()).asInt();
					// Update evaluation score for pattern
					patternValues.put(pattern, newValue);
				}
			}

		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

		return patternValues.toString();

	}
}
