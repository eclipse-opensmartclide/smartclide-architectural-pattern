package org.eclipse.opensmartclide.architecturalpatterns.selection;

import org.eclipse.opensmartclide.architecturalpatterns.service.SurveyJsonHandler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SurveyController {
    private final SurveyJsonHandler surveyJsonHandler;

    public SurveyController(final SurveyJsonHandler surveyJsonHandler) {
        this.surveyJsonHandler = surveyJsonHandler;
    }

    @GetMapping(value = "/survey", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSurvey() {
        return surveyJsonHandler.getSurvey();
    }
}
