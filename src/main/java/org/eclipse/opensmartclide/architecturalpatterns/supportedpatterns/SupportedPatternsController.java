package org.eclipse.opensmartclide.architecturalpatterns.supportedpatterns;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class SupportedPatternsController {

	@GetMapping("/supported-patterns")
	public List<ArchitecturalPatterns> getSupportedPatterns() {
			
		return Arrays.asList(ArchitecturalPatterns.values());
	}
}