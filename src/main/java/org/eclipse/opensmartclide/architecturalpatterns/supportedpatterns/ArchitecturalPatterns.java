package org.eclipse.opensmartclide.architecturalpatterns.supportedpatterns;

public enum ArchitecturalPatterns {
	EVENT_DRIVEN("Event-driven Architectural Pattern"), 
	LAYERED("Layered Architectural Pattern"), 
	MICROKERNEL("Microkernel Architectural Pattern"), 
	MICROSERVICES("Microservices Architectural Pattern"), 
	SERVICE_ORIENTED("Service-oriented Architectural Pattern"), 
	SPACE_BASED("Space-based Architectural Pattern");
	
	private String name;
	
	ArchitecturalPatterns(String name) {
		this.name = name;
	}
	
	public String getPatternName( ) {
		return name;
	}
}