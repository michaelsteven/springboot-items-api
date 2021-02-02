package com.github.michaelsteven.archetype.springboot.items.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.michaelsteven.archetype.springboot.items.TestPageImpl;
import com.github.michaelsteven.archetype.springboot.items.model.ConfirmationDto;
import com.github.michaelsteven.archetype.springboot.items.model.ItemDto;
import com.github.michaelsteven.archetype.springboot.items.model.ItemEntity;
import com.github.michaelsteven.archetype.springboot.items.repository.ItemRepository;

/**
 * The ItemsService Unit test class
 */
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@DisplayName("ItemsService Unit Tests")
public class ItemsServiceTest
{
    @Mock
    private ItemRepository itemRepository;
    
    @Mock
    private MessageSource messageSource;

    private ItemsService itemsService;

    /**
     * Setup.
     */
    @BeforeEach
    void setup()
    {
        itemsService = new ItemsServiceImpl(itemRepository, messageSource);
    }

    /**
     * Given an item to save
     */
    @Nested
    @DisplayName("Given an item to save")
    class GivenAnItemToSave
    {
        /**
         * When the item is valid
         */
        @Nested
        @DisplayName("When the item is valid")
        class WhenItemIsValid
        {
            ItemDto itemDto;

            /**
             * Setup.
             */
            @BeforeEach
            void setup()
            {
                itemDto = new ItemDto();

            }

            /**
             * Then should return confirmation DTO with new ID.
             */
            @Test
            @DisplayName("Then should return confirmation DTO with new ID")
            void thenShouldReturnConfirmationDtoWithNewId()
            {
                ItemEntity itemEntity = new ItemEntity();
                itemEntity.setId(12345L);
                when(itemRepository.save(Mockito.any(ItemEntity.class)))
                        .thenReturn(itemEntity);

                ConfirmationDto confirmationDto = itemsService.saveItem(itemDto);
                assertNotNull(confirmationDto);
                assertEquals(12345, confirmationDto.getId());
            }
        }
    }

    /**
     * Given ID of item to delete.
     */
    @Nested
    @DisplayName("Given ID of item to delete")
    class GivenIdOfItemToDelete
    {
        /**
         * When item exists for the ID
         */
        @Nested
        @DisplayName("When item exists for the ID")
        class WhenItemExistsForID
        {
            /**
             * Then should delete the item
             */
            @Test
            @DisplayName("Then should delete the item")
            void shouldDeleteTheItem()
            {
            	// make the delete call
                itemsService.deleteItemById(12345L);
                
                //verify the repository's delete method was invoked.
            }
        }
    }

    /**
     * Given ID of item to get
     */
    @Nested
    @DisplayName("Given ID of item to get")
    class GivenIdOfItemToGet
    {
        /**
         * When the ID exists
         */
        @Nested
        @DisplayName("When the ID exists")
        class WhenIdExists
        {
        	/**
        	 * Setup
        	 */
        	@BeforeEach
        	void setup() {
        		Optional<ItemEntity> optionalItemEntity = Optional.of(new ItemEntity());
                when(itemRepository.findById(Mockito.anyLong())).thenReturn(optionalItemEntity);
        	}
        	
            /**
             * Then should return item.
             */
            @Test
            @DisplayName("Then should return item")
            void thenShouldReturnItem()
            {
                Optional<ItemDto> optionalItemDto = itemsService.getItemById(12345L);
                assertTrue(optionalItemDto.isPresent());
            }
        }

        /**
         * When the ID does not exist
         */
        @Nested
        @DisplayName("When the ID does not exist")
        class WhenApplicationIdDoesNotExist
        {
            /**
             * Then should return empty optional.
             */
            @Test
            @DisplayName("Then should return empty optional")
            void thenShouldRetunEmptyOptional()
            {
                Optional<ItemEntity> optionalItemEntity = Optional.empty();
                when(itemRepository.findById(Mockito.anyLong())).thenReturn(optionalItemEntity);
                Optional<ItemDto> optionalItemDto = itemsService.getItemById(12345L);
                assertTrue(!optionalItemDto.isPresent());
            }
        }
    }

    /**
     * The Class GivenCallToGetLoanApplications.
     */
    @Nested
    @DisplayName("Given call to get loan applications")
    class GivenCallToGetLoanApplications
    {
        /**
         * The Class WhenCalledWithPagination.
         */
        @Nested
        @DisplayName("When getting a paged list")
        class WhenCalledWithPagination
        {
            /**
             * Then should return paged list.
             */
            @Test
            @DisplayName("Then should return paged list")
            void thenShouldReturnPagedList()
            {
                Page<ItemEntity> itemEntityPage = new TestPageImpl<>();
                when(itemRepository.findAll(Mockito.any(PageRequest.class)))
                        .thenReturn(itemEntityPage);
                
                Pageable pageable = PageRequest.of(1, 10, Sort.by("id").ascending());
                Page<ItemDto> actualPagedLoanApplication = itemsService.getItems(pageable);
                assertNotNull(actualPagedLoanApplication);
            }
        }
    }

    /**
     * Given item to edit
     */
    @Nested
    @DisplayName("Given item to edit")
    class GivenItemToEdit
    {

        /**
         * When item is valid
         */
        @Nested
        @DisplayName("When item is valid")
        class WhenItemIsValid
        {
            ItemEntity itemEntity;
            ItemEntity savedItemEntity;
            ItemDto itemDto;

            /**
             * Setup.
             */
            @BeforeEach
            void setup()
            {
                itemEntity = new ItemEntity();
                itemEntity.setId(12345L);
                itemEntity.setName("foo bar");
                
                savedItemEntity = new ItemEntity();
                savedItemEntity.setId(12345L);
                savedItemEntity.setName("fizz buzz"); 
                
                itemDto = new ItemDto();
                itemDto.setId(12345L);
                itemDto.setName("fizz buzz");              
                
            }

            /**
             * Then should return confirmation DTO
             */
            @Test
            @DisplayName("Then should return confirmation DTO")
            void thenShouldReturnConfirmationDto()
            {
                // when calling findById on the repository
                Optional<ItemEntity> optionalItemEntity = Optional.of(itemEntity);
                when(itemRepository.findById(Mockito.anyLong())).thenReturn(optionalItemEntity);

                // when calling save on the repository
                when(itemRepository.save(Mockito.any(ItemEntity.class)))
                        .thenReturn(savedItemEntity);

                // make the call to the service
                ConfirmationDto confirmationDto = itemsService.editItem(itemDto);

                // validate the response
                assertNotNull(confirmationDto);
                assertEquals(12345, confirmationDto.getId());
            }
        }
    }
}
