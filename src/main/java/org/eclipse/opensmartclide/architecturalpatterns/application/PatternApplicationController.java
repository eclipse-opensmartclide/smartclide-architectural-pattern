package org.eclipse.opensmartclide.architecturalpatterns.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatternApplicationController {

	@GetMapping("/application")
	public String setupPattern() {
		//TODO
		return "TODO: Architectural Pattern is being set up.";
	}
}
