package org.eclipse.opensmartclide.architecturalpatterns.selection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@RestController
public class SurveyController {

	final URL url = this.getClass().getResource("/jsonfiles/survey.json");
	final Logger logger = LoggerFactory.getLogger(PatternSelectionController.class);

	private String readSurvey() {

		String surveyJsonStr = new String();

		try {

			final Path filePath = Path.of(Objects.requireNonNull(url).toURI());
			return Files.readString(filePath);

		} catch (URISyntaxException | IOException e) {

			logger.error("Failed to read survey file!", e);
		}

		return surveyJsonStr;
	}

	String survey = readSurvey();

	@GetMapping(value = "/survey", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getSurvey() {

		if (survey == null)
			throw new NullPointerException("Survey is not being referenced.");

		return survey;
	}
}
