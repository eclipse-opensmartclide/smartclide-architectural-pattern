package eu.smartclide.architecturalpatterns.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatternApplicationController {

	@GetMapping("/apply")
	public String setupPattern() {
		//TODO
		return "TODO: Architectural Pattern is being set up.";
	}
}
