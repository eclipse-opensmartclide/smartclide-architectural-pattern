package org.eclipse.opensmartclide.architecturalpatterns.application;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.opensmartclide.architecturalpatterns.service.ArchitecturalPatternsJsonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class PatternApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(PatternApplicationController.class);

    @Value("${IMPORT_PROJECT_URL}")
    private String importProjectURL;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ArchitecturalPatternsJsonHandler projectJsonHandler;

    public PatternApplicationController(final ArchitecturalPatternsJsonHandler jsonHandler) {
        this.projectJsonHandler = jsonHandler;
    }

    @PostMapping(value = "/application")
    public String applyPattern(@RequestParam("framework") String framework,
                               @RequestParam("pattern") String pattern,
                               @Nullable @RequestParam("name") String projName,
                               @Nullable @RequestParam("visibility") String visibility,
                               @RequestHeader String gitLabServerURL,
                               @RequestHeader String gitlabToken) {
        try {
            String repoUrl = getProjectURL(framework, pattern);
            if (repoUrl == null) {
                throw new NullPointerException("Repository URL is not found.");
            }

            String response = createProject(repoUrl, projName, visibility, gitLabServerURL, gitlabToken);
            logger.info("Pattern application succeeded!");
            return response;
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

    public String createProject(String repoUrl,
                                String projName,
                                String visibility,
                                String gitLabServerURL,
                                String gitlabToken) {
        // Creating URL with parameters
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("repoUrl", repoUrl);

        if (projName != null) {
            parameters.add("name", projName);
        }

        if (visibility != null) {
            parameters.add("visibility", visibility);
        }

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(importProjectURL).queryParams(parameters);
        String url = uriComponentsBuilder.build().encode().toUriString();

        if (!url.startsWith(importProjectURL)) {
            throw new IllegalStateException("Not a valid url: " + url);
        }

        // Setting headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("gitLabServerURL", gitLabServerURL);
        headers.set("gitlabToken", gitlabToken);

        // Creating POST request query
        HttpEntity<String> request = new HttpEntity<>(url, headers);

        // Make POST request to external project importer
        return restTemplate.postForObject(url, request, String.class);
    }
}
