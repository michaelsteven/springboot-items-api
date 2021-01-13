package com.github.michaelsteven.archetype.springboot.items.model.event;

import lombok.Data;

/**
 * Compliance Event
 */
@Data
public class ComplianceEvent {
	
	/** The action. */
	private ComplianceAction action;
	
	/** The resource. */
	private String resource;
	
	/** The event source. */
	private String eventSource;
}
