package org.eclipse.opensmartclide.architecturalpatterns.application;

import org.eclipse.opensmartclide.architecturalpatterns.service.ArchitecturalPatternsJsonHandler;
import java.lang.IllegalArgumentException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class PatternApplicationController {

	final String importProjectURL = "https://api.dev.smartclide.eu/external-project-importer/importProject";
	private final ArchitecturalPatternsJsonHandler projectJsonHandler;

	public PatternApplicationController(final ArchitecturalPatternsJsonHandler jsonHandler) {
		this.projectJsonHandler = jsonHandler;
	}

	@PostMapping(value = "/application", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	
	public String applyPattern(@RequestParam("framework") String framework, 
			@RequestParam("pattern") String pattern,
			@Nullable @RequestParam("name") String projName, 
			@Nullable @RequestParam("visibility") String visibility,
			@RequestHeader String gitLabServerURL, 
			@RequestHeader String gitlabToken) {

		try {
			String repoUrl = getProjectURL(framework, pattern);
			MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();
			parameters.add("framework", framework);
			parameters.add("pattern", pattern);
			
			if (projName != null) 
				parameters.add("name", projName);
			
			if (visibility != null) 
				parameters.add("visibility", visibility);

			if (repoUrl == null) {
				throw new NullPointerException("Repository URL is not found.");
			} else {
				HttpHeaders headers = new HttpHeaders();
				headers.set("gitLabServerURL", gitLabServerURL);
				headers.set("gitlabToken", gitlabToken);
				
				HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);

				RestTemplate restTemplate = new RestTemplate();
				String result = restTemplate.postForObject(importProjectURL, request, String.class);

				return result;
			}
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected a git-based URL", e);
		}
	}

	public String getProjectURL(String framework, String pattern) {
		final JsonNode projUrlsJsonNode = projectJsonHandler.getProjectUrlsNode();

		if (projUrlsJsonNode.get(framework) == null) {
			throw new IllegalArgumentException(
					"Invalid framework received: " + framework + "is not a valid framework.");
		} else if (projUrlsJsonNode.get(framework).get(pattern) == null) {
			throw new IllegalArgumentException(
					"Invalid pattern received: " + pattern + "is not a valid pattern.");
		}

		return projUrlsJsonNode.get(framework).get(pattern).asText();
	}

}
