package com.github.michaelsteven.archetype.springboot.items.model;

import java.time.ZonedDateTime;

import lombok.Data;


/**
 * Instantiates a new confirmation dto.
 */
@Data
public class ConfirmationDto {
	
	/** The id. */
	private Long id;
	
	/** The status. */
	private ItemStatus status;
	
	/** The date submitted. */
	private ZonedDateTime dateSubmitted;
}
