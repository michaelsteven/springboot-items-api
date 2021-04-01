package com.github.michaelsteven.archetype.springboot.items.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.github.michaelsteven.archetype.springboot.items.model.ConfirmationDto;
import com.github.michaelsteven.archetype.springboot.items.model.ItemDto;
import com.github.michaelsteven.archetype.springboot.items.model.ItemEntity;
import com.github.michaelsteven.archetype.springboot.items.model.ItemStatus;
import com.github.michaelsteven.archetype.springboot.items.model.event.Compliance;
import com.github.michaelsteven.archetype.springboot.items.model.event.ComplianceAction;
import com.github.michaelsteven.archetype.springboot.items.repository.ItemRepository;

/**
 * The Class ItemsServiceImpl.
 */
@Service
@Validated
public class ItemsServiceImpl implements ItemsService {
		
	public static final Logger logger = LoggerFactory.getLogger(ItemsServiceImpl.class);
	private ItemRepository itemRepository;
	private MessageSource messageSource;
	
	/**
	 * Constructor.
	 * 
	 * @param itemRepository
	 * @param messageSource
	 */
	public  ItemsServiceImpl(ItemRepository itemRepository, MessageSource messageSource) {
		this.itemRepository = itemRepository;
		this.messageSource = messageSource;
	}
	
	
	/**
	 * Gets the items.
	 *
	 * @param pageable the pageable
	 * @return the items
	 */
	@Override
	@Compliance(action = ComplianceAction.read)
	public Page<ItemDto> getItems(Pageable pageable){
		return itemRepository.findAll(pageable)
				.map(this::convert);
	}
	
	
	/**
	 * Gets the item by id.
	 *
	 * @param id the id
	 * @return the item by id
	 */
	@Override
	@Compliance(action = ComplianceAction.read)
	public Optional<ItemDto> getItemById(long id){
		return itemRepository.findById(id)
				.map(entity -> Optional.of(convert(entity)))
				.orElse(Optional.empty());
	}
	
	
	/**
	 * Save item.
	 *
	 * @param itemDto the item dto
	 * @return the confirmation dto
	 */
	@Override
	@Compliance(action = ComplianceAction.create)
	public ConfirmationDto saveItem(@NotNull @Valid ItemDto itemDto) {
		ItemEntity itemEntity = convert(itemDto);
		ItemEntity savedEntity = itemRepository.save(itemEntity);
		return createConfirmationDto(ItemStatus.SUBMITTED, savedEntity);
	}
	
	
	/**
	 * Edits the item.
	 *
	 * @param itemDto the item dto
	 * @return the confirmation dto
	 */
	@Override
	@Transactional
	@Compliance(action = ComplianceAction.update)
	public ConfirmationDto editItem(@NotNull @Valid ItemDto itemDto) {
		return itemRepository.findById(itemDto.getId())
				.map( entity -> { 
						applyToEntity(itemDto, entity); //void method - entity modified byref
						return entity; 
					})
				.map( itemRepository::save )
				.map( savedItem -> createConfirmationDto(ItemStatus.SUBMITTED, savedItem) )
				.orElseThrow( () -> new ValidationException (
						messageSource.getMessage("itemsservice.validationexception.entitynotfoundforid", 
							new Object[] { String.valueOf(itemDto.getId()) },
							LocaleContextHolder.getLocale() )
					)
				);
	}
	
	
	/**
	 * Delete item by id.
	 *
	 * @param id the id
	 */
	@Override
	@Compliance(action = ComplianceAction.delete)
	public void deleteItemById(long id){
		itemRepository.findById(id)
			.ifPresent(itemRepository::delete);
	}
	
	
	/**
	 * Creates the confirmation dto.
	 *
	 * @param itemStatus the item status
	 * @param entity the entity
	 * @return the confirmation dto
	 */
	private ConfirmationDto createConfirmationDto(ItemStatus itemStatus, ItemEntity entity) {
		ConfirmationDto confirmationDto = new ConfirmationDto();
		confirmationDto.setStatus(itemStatus);
		if(null != entity) {
			confirmationDto.setId(entity.getId());
			if(null != entity.getCreatedTimestamp()) {
				ZonedDateTime dateSubmitted = ZonedDateTime.ofInstant(entity.getCreatedTimestamp(), ZoneOffset.UTC);
				confirmationDto.setDateSubmitted(dateSubmitted);
			}
		}
		else {
			logger.warn("itemEntity is null");
		}
		return confirmationDto;
	}
	
	/**
	 * Convert.
	 *
	 * @param sourceDto the source dto
	 * @return the item entity
	 */
	private ItemEntity convert(ItemDto sourceDto) {
		ItemEntity entity = new ItemEntity();
		applyToEntity(sourceDto, entity);
		return entity;
	}
	
	/**
	 * Apply changes to entity.
	 *
	 * @param sourceDto the source dto
	 * @param targetEntity the target entity
	 */
	private void applyToEntity(@NotNull ItemDto sourceDto, ItemEntity targetEntity) {
		if(null != targetEntity) {
			targetEntity.setId(sourceDto.getId());
			targetEntity.setName(sourceDto.getName());
			targetEntity.setDescription(sourceDto.getDescription());
		}
	}
	
	/**
	 * Convert.
	 *
	 * @param sourceEntity the source entity
	 * @return the item dto
	 */
	private ItemDto convert(ItemEntity sourceEntity) {
		if(null == sourceEntity) {
			return null;
		}
		ZonedDateTime dateSubmitted = null;
		
		if(null != sourceEntity.getCreatedTimestamp()) {
			dateSubmitted = ZonedDateTime.ofInstant(sourceEntity.getCreatedTimestamp(), ZoneOffset.UTC);
		}
		return new ItemDto(sourceEntity.getId(), sourceEntity.getName(), sourceEntity.getDescription(), dateSubmitted);
	}
}
