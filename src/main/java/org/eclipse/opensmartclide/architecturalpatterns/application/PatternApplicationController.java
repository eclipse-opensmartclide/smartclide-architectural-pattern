package org.eclipse.opensmartclide.architecturalpatterns.application;

import org.eclipse.opensmartclide.architecturalpatterns.service.ArchitecturalPatternsJsonHandler;
import java.lang.IllegalArgumentException;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class PatternApplicationController {

	@Value("${IMPORT_PROJECT_URL}$")
	private String importProjectURL;
	
	private final ArchitecturalPatternsJsonHandler projectJsonHandler;

	public PatternApplicationController(final ArchitecturalPatternsJsonHandler jsonHandler) {
		this.projectJsonHandler = jsonHandler;
	}

	@PostMapping(value = "/application", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String applyPattern(@RequestParam("framework") String framework, @RequestParam("pattern") String pattern,
			@Nullable @RequestParam("name") String projName, @Nullable @RequestParam("visibility") String visibility,
			@RequestHeader String gitLabServerURL, @RequestHeader String gitlabToken) {

		try {

			String repoUrl = getProjectURL(framework, pattern);

			if (repoUrl == null) {
				throw new NullPointerException("Repository URL is not found.");
			} else 	
				return createProject(repoUrl, projName, visibility,gitLabServerURL,gitlabToken);

		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Problem with either target project repository or input parameters: " + e.getMessage(), e);
		}
	}

	public String getProjectURL(String framework, String pattern) {
		final JsonNode projUrlsJsonNode = projectJsonHandler.getProjectUrlsNode();
		
		JsonNode frameworkNode = projUrlsJsonNode.get(framework);
		JsonNode patternNode = projUrlsJsonNode.get(framework).get(pattern);
		
		if (frameworkNode == null) {
			throw new IllegalArgumentException(
					"Invalid framework received: " + framework + "is not a valid framework.");
		} else if (patternNode == null) {
			throw new IllegalArgumentException("Invalid pattern received: " + pattern + "is not a valid pattern.");
		}

		return patternNode.asText();
	}

	public String createProject(String repoUrl, String projName, String visibility,
			String gitLabServerURL, String gitlabToken) {

		// Creating URL with parameters
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("repoUrl", repoUrl);

		if (projName != null)
			parameters.add("name", projName);

		if (visibility != null)
			parameters.add("visibility", visibility);

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(importProjectURL).queryParams(parameters);
		String url = uriComponentsBuilder.build().encode().toUriString();

		// Setting headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("gitLabServerURL", gitLabServerURL);
		headers.set("gitlabToken", gitlabToken);

		// Creating POST request query
		HttpEntity<String> request = new HttpEntity<>(url, headers);
		
		try {
		// Make POST request to external project importer
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.postForObject(importProjectURL, request, String.class);

		return response;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid URL: " + url + "is not a valid url.");
		}
	}
}
