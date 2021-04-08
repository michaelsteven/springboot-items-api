package com.github.michaelsteven.archetype.springboot.items.model;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Instantiates a new item dto.
 *
 * @param id the id
 * @param name the name
 * @param description the description
 * @param dateSubmitted the date submitted
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // helpful to support minor model changes
@Schema(name="Item")
public class ItemDto {

	/** The id. */
	@Schema(name = "id", description="The ID of the item", example ="1234567890")
	private Long id;
	
	/** The name. */
	@Schema(name = "name", description="The name of the item", example = "wigit5spr")
	@NotNull(message = "{itemdto.null}")
	@Size(min  = 1, max = 25, message = "{itemdto.textlimit}")
	private String name;
	
	/** The description. */
	@Schema(name = "description", description="The description of the item", example = "5 sprocket wigit")
	@Size(min  = 1, max = 200, message = "{itemdto.textlimit}")
	private String description;
	
	/** The date submitted. */
	@Schema(hidden = true)
	private ZonedDateTime dateSubmitted;
	
}
