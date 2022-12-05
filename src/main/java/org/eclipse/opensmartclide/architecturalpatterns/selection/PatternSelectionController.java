package org.eclipse.opensmartclide.architecturalpatterns.selection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private EnumMap<ArchitecturalPatterns, Integer> patternValues = new EnumMap<>(ArchitecturalPatterns.class);
	
	final URL url = this.getClass().getResource("/jsonfiles/surveyEvaluation.json");

	private JsonNode readSurveyEvaluation() {

		final Logger logger = LogManager.getLogger(PatternSelectionController.class);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode evaluationNode = mapper.createObjectNode();

		try {

			// Read evaluation values from JSON file
			final Path filePath = Path.of(Objects.requireNonNull(url).toURI());
			String jsonStr = Files.readString(filePath);
			evaluationNode = mapper.readValue(jsonStr, JsonNode.class);

		} catch (URISyntaxException | IOException e) {
			logger.error("Failed to read survey evaluation file!", e);
		}

		return evaluationNode;
	}

	JsonNode surveyEvaluationNode = readSurveyEvaluation();

	@PostMapping(value = "/evaluation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String evaluateSurveyInput(@RequestBody List<String> input) {

		// Initialize
		patternValues.put(ArchitecturalPatterns.LAYERED, 0);
		patternValues.put(ArchitecturalPatterns.EVENT_DRIVEN, 0);
		patternValues.put(ArchitecturalPatterns.MICROKERNEL, 0);
		patternValues.put(ArchitecturalPatterns.MICROSERVICES, 0);
		patternValues.put(ArchitecturalPatterns.SERVICE_ORIENTED, 0);
		patternValues.put(ArchitecturalPatterns.SPACE_BASED, 0);

		// Iterate over survey question IDs
		for (String id : input) {
			if (surveyEvaluationNode.get(id) == null) {
				throw new IllegalArgumentException(
						"Invalid survey input received: " + id + "is not a valid question ID.\n");
			}
			JsonNode valuesJsonNode = surveyEvaluationNode.get(id).get(0);

			for (ArchitecturalPatterns pattern : ArchitecturalPatterns.values()) {
				int currentValue = patternValues.get(pattern);
				int newValue = currentValue + valuesJsonNode.get(pattern.name()).asInt();
				// Update evaluation score for pattern
				patternValues.put(pattern, newValue);
			}
		}

		return patternValues.toString();

	}
}
