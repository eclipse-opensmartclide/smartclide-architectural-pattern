package org.eclipse.opensmartclide.architecturalpatterns.application;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.opensmartclide.architecturalpatterns.service.ArchitecturalPatternsJsonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class PatternApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(PatternApplicationController.class);

    private final ArchitecturalPatternsJsonHandler projectJsonHandler;

    public PatternApplicationController(final ArchitecturalPatternsJsonHandler jsonHandler) {
        this.projectJsonHandler = jsonHandler;
    }

    @PostMapping(value = "/application", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> applyPattern(@RequestParam("framework") String framework,
                                            @RequestParam("pattern") String pattern) {
        try {
            String repoUrl = getProjectURL(framework, pattern);
            if (repoUrl == null) {
                throw new NullPointerException("Repository URL is not found.");
            }
            return Map.of("templateRepositoryUrl", repoUrl);
        } catch (Exception e) {
            logger.error("Exception during pattern application.", e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Problem with either project template resource or input parameters: " + e.getMessage()
            );
        }
    }

    public String getProjectURL(String framework, String pattern) {
        final JsonNode projUrlsJsonNode = projectJsonHandler.getProjectUrlsNode();

        JsonNode frameworkNode = projUrlsJsonNode.get(framework);
        if (frameworkNode == null) {
            throw new IllegalArgumentException("Invalid framework received: " + framework + " is not a valid framework.");
        }

        JsonNode patternNode = frameworkNode.get(pattern);
        if (frameworkNode.get(pattern) == null) {
            throw new IllegalArgumentException("Invalid pattern received: " + pattern + " is not a valid pattern.");
        }

        return patternNode.asText();
    }
}
