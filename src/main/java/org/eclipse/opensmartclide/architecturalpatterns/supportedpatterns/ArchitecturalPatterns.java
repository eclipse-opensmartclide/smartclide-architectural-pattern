package org.eclipse.opensmartclide.architecturalpatterns.supportedpatterns;

public enum ArchitecturalPatterns {
    EVENT_DRIVEN("Event-driven Architectural Pattern"),
    LAYERED("Layered Architectural Pattern"),
    MICROKERNEL("Microkernel Architectural Pattern"),
    MICROSERVICES("Microservices Architectural Pattern"),
    SERVICE_ORIENTED("Service-oriented Architectural Pattern"),
    SPACE_BASED("Space-based Architectural Pattern");

    private final String name;

    ArchitecturalPatterns(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public String getPatternName() {
        return name;
    }
}
