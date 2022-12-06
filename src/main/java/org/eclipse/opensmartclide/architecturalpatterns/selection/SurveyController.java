package org.eclipse.opensmartclide.architecturalpatterns.selection;

import org.eclipse.opensmartclide.architecturalpatterns.service.JsonHandler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SurveyController {
    private final JsonHandler jsonHandler;

    public SurveyController(final JsonHandler jsonHandler) {
        this.jsonHandler = jsonHandler;
    }

    @GetMapping(value = "/survey", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSurvey() {
        return jsonHandler.getSurvey();
    }
}
