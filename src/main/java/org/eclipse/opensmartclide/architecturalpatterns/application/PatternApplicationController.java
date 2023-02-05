package org.eclipse.opensmartclide.architecturalpatterns.application;

import org.eclipse.opensmartclide.architecturalpatterns.service.ArchitecturalPatternsJsonHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class PatternApplicationController {

	final String importProjectURL = "https://api.dev.smartclide.eu/external-project-importer/importProject";
	private final ArchitecturalPatternsJsonHandler projectJsonHandler;

	public PatternApplicationController(final ArchitecturalPatternsJsonHandler jsonHandler) {
		this.projectJsonHandler = jsonHandler;
	}

	@PostMapping(value = "/application", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String applyPattern(@RequestParam("technology") String techName, @RequestParam("pattern") String pattern,
			@Nullable @RequestParam("name") String projName, 
			@Nullable @RequestParam("visibility") String visibility,
			@RequestHeader String gitLabServerURL, @RequestHeader String gitlabToken) {
		
		String repoUrl = getProjectURL(techName, pattern);

		HttpHeaders headers = new HttpHeaders();
		headers.set("gitLabServerURL", gitLabServerURL);
		headers.set("gitlabToken", gitlabToken);

		String query = "repoUrl=" + repoUrl + "&name=" + projName + "&visibility=" + visibility;
		HttpEntity<String> request = new HttpEntity<>(query, headers);

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject(importProjectURL, request, String.class);

		return result;
	}
	
	public String getProjectURL(String techName, String pattern) {
        final JsonNode jsonNode = projectJsonHandler.getProjectUrlsNode();

            if (jsonNode.get(techName) == null) {
                throw new IllegalArgumentException("Invalid technology received: " + techName + "is not a valid technology.");
            }
            return jsonNode.get(techName).asText();
	}

}
