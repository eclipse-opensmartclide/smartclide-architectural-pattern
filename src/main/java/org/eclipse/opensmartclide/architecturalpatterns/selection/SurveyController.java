package org.eclipse.opensmartclide.architecturalpatterns.selection;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@RestController
public class SurveyController {

	final URL url = this.getClass().getResource("/jsonfiles/survey.json");

	ObjectMapper mapper = new ObjectMapper();

	@GetMapping(value = "/survey", produces = MediaType.APPLICATION_JSON_VALUE)

	public String getSurvey() {

		String surveyJsonStr = null;

		try {

			final Path filePath = Path.of(Objects.requireNonNull(url).toURI());
			surveyJsonStr = Files.readString(filePath);

		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

		return surveyJsonStr;
	}
}
