package org.eclipse.opensmartclide.architecturalpatterns.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Component
public class ArchitecturalPatternsJsonHandler {
    private static final String SURVEY_JSON_FILE_PATH = "/jsonfiles/survey.json";
    private static final String SURVEY_EVALUATION_JSON_PATH = "/jsonfiles/surveyEvaluation.json";
    private static final String PROJECT_URLS_JSON_PATH = "/jsonfiles/projectURLs.json";
    private static final Logger logger = LoggerFactory.getLogger(ArchitecturalPatternsJsonHandler.class);
    private final ObjectMapper mapper;
    private final String survey;
    private final JsonNode surveyEvaluationNode;
    private final JsonNode projectUrlsNode;

    public ArchitecturalPatternsJsonHandler(final ObjectMapper mapper) {
        this.mapper = mapper;
        this.survey = readJsonFileIntoString(SURVEY_JSON_FILE_PATH);
        this.surveyEvaluationNode = readJsonFileIntoJsonNode(SURVEY_EVALUATION_JSON_PATH);
        this.projectUrlsNode = readJsonFileIntoJsonNode(PROJECT_URLS_JSON_PATH);
    }

    public String getSurvey() {
        return survey;
    }

    public JsonNode getSurveyEvaluationNode() {
        return surveyEvaluationNode;
    }

    public JsonNode getProjectUrlsNode() {
    	return projectUrlsNode;
    }
    
    private String readJsonFileIntoString(final String pathToFile) {
        logger.info("Reading file: {}", pathToFile);
        try {
            final URL jsonUrl = ArchitecturalPatternsJsonHandler.class.getResource(pathToFile);
            final Path path = Path.of(Objects.requireNonNull(jsonUrl).toURI());
            return Files.readString(path);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(String.format("Failed to read file: %s!", pathToFile), e);
        }
    }

    private JsonNode readJsonFileIntoJsonNode(@SuppressWarnings("SameParameterValue") final String pathToFile) {
        try {
            final String jsonStr = readJsonFileIntoString(pathToFile);
            return mapper.readValue(jsonStr, JsonNode.class);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to read file: %s!", pathToFile), e);
        }
    }
}
