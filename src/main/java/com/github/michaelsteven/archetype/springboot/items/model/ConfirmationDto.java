package com.github.michaelsteven.archetype.springboot.items.model;

import java.time.ZonedDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * Instantiates a new confirmation dto.
 */
@Data
@Schema(name="Confirmation")
public class ConfirmationDto {
	
	/** The id. */
	private Long id;
	
	/** The status. */
	private ItemStatus status;
	
	/** The date submitted. */
	private ZonedDateTime dateSubmitted;
}
