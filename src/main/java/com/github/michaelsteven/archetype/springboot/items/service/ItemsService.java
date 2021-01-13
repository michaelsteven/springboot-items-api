package com.github.michaelsteven.archetype.springboot.items.service;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.michaelsteven.archetype.springboot.items.model.ConfirmationDto;
import com.github.michaelsteven.archetype.springboot.items.model.ItemDto;

/**
 * The Interface ItemsService.
 */
public interface ItemsService {
	
	/**
	 * Gets the items.
	 *
	 * @param pageable the pageable
	 * @return the items
	 */
	public abstract Page<ItemDto> getItems(Pageable pageable);
	
	/**
	 * Gets the item by id.
	 *
	 * @param id the id
	 * @return the item by id
	 */
	public abstract Optional<ItemDto> getItemById(long id);
	
	/**
	 * Save item.
	 *
	 * @param itemDto the item dto
	 * @return the confirmation dto
	 */
	public abstract ConfirmationDto saveItem(@NotNull @Valid ItemDto itemDto);
	
	/**
	 * Edits the item.
	 *
	 * @param itemDto the item dto
	 * @return the confirmation dto
	 */
	public abstract ConfirmationDto editItem(@NotNull @Valid ItemDto itemDto);
	
	/**
	 * Delete item by id.
	 *
	 * @param id the id
	 */
	public abstract void deleteItemById(long id);

}
