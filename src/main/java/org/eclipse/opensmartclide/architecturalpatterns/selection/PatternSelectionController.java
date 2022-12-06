package org.eclipse.opensmartclide.architecturalpatterns.selection;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.opensmartclide.architecturalpatterns.service.JsonHandler;
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
    private final JsonHandler jsonHandler;

    public PatternSelectionController(final JsonHandler jsonHandler) {
        this.jsonHandler = jsonHandler;
    }

    @PostMapping(
            value = "/evaluation",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<ArchitecturalPatterns, Integer> evaluateSurveyInput(@RequestBody List<String> input) {
        return calculatePatternValues(input);
    }

    private HashMap<ArchitecturalPatterns, Integer> calculatePatternValues(final List<String> input) {
        // Initialize
        final JsonNode surveyEvaluationNode = jsonHandler.getSurveyEvaluationNode();
        final HashMap<ArchitecturalPatterns, Integer> patternValues = initializePatternValues();

        // Iterate over survey question IDs
        for (String id : input) {
            if (surveyEvaluationNode.get(id) == null) {
                throw new IllegalArgumentException("Invalid survey input received: " + id + "is not a valid question ID.");
            }
            JsonNode valuesJsonNode = surveyEvaluationNode.get(id);

            for (ArchitecturalPatterns pattern : ArchitecturalPatterns.values()) {
                int newValue = patternValues.get(pattern) + valuesJsonNode.get(pattern.name()).asInt();
                // Update evaluation score for pattern
                patternValues.put(pattern, newValue);
            }
        }
        return patternValues;
    }

    private HashMap<ArchitecturalPatterns, Integer> initializePatternValues() {
        return new HashMap<>(
                Map.of(
                        ArchitecturalPatterns.LAYERED, 0,
                        ArchitecturalPatterns.EVENT_DRIVEN, 0,
                        ArchitecturalPatterns.MICROKERNEL, 0,
                        ArchitecturalPatterns.MICROSERVICES, 0,
                        ArchitecturalPatterns.SERVICE_ORIENTED, 0,
                        ArchitecturalPatterns.SPACE_BASED, 0
                )
        );
    }
}
