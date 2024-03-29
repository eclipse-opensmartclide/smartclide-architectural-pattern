package org.eclipse.opensmartclide.architecturalpatterns.selection;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.opensmartclide.architecturalpatterns.service.ArchitecturalPatternsJsonHandler;
import org.eclipse.opensmartclide.architecturalpatterns.supportedpatterns.ArchitecturalPatterns;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PatternSelectionController {
	private final ArchitecturalPatternsJsonHandler surveyJsonHandler;

	public PatternSelectionController(final ArchitecturalPatternsJsonHandler surveyJsonHandler) {
		this.surveyJsonHandler = surveyJsonHandler;
	}

	@PostMapping(value = "/evaluation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<ArchitecturalPatterns, Integer> evaluateSurveyInput(@RequestBody List<String> input) {
		return calculatePatternValues(input);
	}

	private HashMap<ArchitecturalPatterns, Integer> calculatePatternValues(final List<String> input) {
		// Initialize
		final JsonNode surveyEvaluationNode = surveyJsonHandler.getSurveyEvaluationNode();
		final HashMap<ArchitecturalPatterns, Integer> patternValues = initializePatternValues();
		int totalValue = 0;

		// Iterate over survey question IDs
		for (String id : input) {
			if (surveyEvaluationNode.get(id) == null) {
				throw new IllegalArgumentException(
						"Invalid survey input received: " + id + "is not a valid question ID.");
			}
			JsonNode valuesJsonNode = surveyEvaluationNode.get(id);

			for (ArchitecturalPatterns pattern : ArchitecturalPatterns.values()) {
				int value = valuesJsonNode.get(pattern.name()).asInt();
				int newValue = patternValues.get(pattern) + value;
				// Update evaluation score for pattern
				patternValues.put(pattern, newValue);
				totalValue = totalValue + value;
			}
		}

		for (Map.Entry<ArchitecturalPatterns, Integer> entry : patternValues.entrySet()) {
			double percentage = (entry.getValue() * 100) / (double) totalValue;
			patternValues.put(entry.getKey(), (int) percentage);
		}
		return patternValues;
	}

	private HashMap<ArchitecturalPatterns, Integer> initializePatternValues() {
		return new HashMap<>(Map.of(ArchitecturalPatterns.LAYERED, 0, ArchitecturalPatterns.EVENT_DRIVEN, 0,
				ArchitecturalPatterns.MICROKERNEL, 0, ArchitecturalPatterns.MICROSERVICES, 0,
				ArchitecturalPatterns.SERVICE_ORIENTED, 0, ArchitecturalPatterns.SPACE_BASED, 0));
	}
}
