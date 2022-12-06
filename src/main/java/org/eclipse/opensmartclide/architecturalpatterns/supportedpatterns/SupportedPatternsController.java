package org.eclipse.opensmartclide.architecturalpatterns.supportedpatterns;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupportedPatternsController {
    @GetMapping(value = "/supported-patterns", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArchitecturalPatterns[] getSupportedPatterns() {
        return ArchitecturalPatterns.values();
    }
}
