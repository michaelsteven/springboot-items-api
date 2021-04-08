package com.github.michaelsteven.archetype.springboot.items.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.michaelsteven.archetype.springboot.items.TestPageImpl;
import com.github.michaelsteven.archetype.springboot.items.model.ApiError;
import com.github.michaelsteven.archetype.springboot.items.model.ConfirmationDto;
import com.github.michaelsteven.archetype.springboot.items.model.ItemDto;
import com.github.michaelsteven.archetype.springboot.items.model.ItemStatus;
import com.github.michaelsteven.archetype.springboot.items.service.ItemsService;

/**
 * Items controller unit tests
 */
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@DisplayName("Items controller unit tests")
public class ItemsControllerTest
{
    private MockMvc mockMvc;
    private ItemsService itemsService;
    private MessageSource messageSource;
    private static final String API_VERSION = "v1";
    private ObjectMapper objectMapper;

    /**
     * Setup - executes before each test
     */
    @BeforeEach
    void setup()
    {
        itemsService = Mockito.mock(ItemsService.class);
        messageSource = Mockito.mock(MessageSource.class);
        ItemsController itemsController = new ItemsController(itemsService, messageSource);
        
        mockMvc = MockMvcBuilders.standaloneSetup(itemsController)
        		   .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        		   .setControllerAdvice(new RestExceptionHandler())
        		   .build();
        
        objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Canary test. When the mock mvc is null, should throw an exception. Helps to
     * validate the testing framework is functioning properly.
     */
    @Test
    @DisplayName("Canary Test")
    void canaryTest()
    {
        assertThat(mockMvc).isNotNull();
    }

    /**
     * Given call to retrieve collection of items
     */
    @Nested
    @DisplayName("Given call to retrieve collection of items")
    class GivenCallToRetrieveColllectionOfItems
    {
        private MockHttpServletRequestBuilder requestBuilder;

        /**
         * Setup - executes before each test
         */
        @BeforeEach
        void setup()
        {
            // define the request
            requestBuilder = MockMvcRequestBuilders
            		.get("/api/" + API_VERSION + "/items").characterEncoding("utf-8")
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        }

        /**
         * When call is valid
         */
        @Nested
        @DisplayName("When call is valid")
        class WhenCallIsValid
        {
        	/**
        	 * Setup
        	 */
        	@BeforeEach
            void setup() 
        	{
               	// define response to return from the service layer
                ItemDto itemDto = new ItemDto();
                itemDto.setDateSubmitted( ZonedDateTime.now() );
            	Pageable pageable = PageRequest.of(1, 10, Sort.by("id").ascending());
                List<ItemDto> itemDtoList = new ArrayList<>();
                itemDtoList.add(itemDto);
                Page<ItemDto> page = new PageImpl<>(itemDtoList, pageable, 1);

                // mock the call to the service layer
                Mockito.when(itemsService.getItems(Mockito.any(Pageable.class))).thenReturn(page);
            }
        	
            /**
             * Then should return pageable with items
             * 
             * @throws Exception the exception
             */
            @Test
            @DisplayName("Then should return pageable with items")
            void thenShouldReturnPageableWithItems() throws Exception
            {

                // perform the call to the api
                MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print()).andReturn();

                // validate the response
                MockHttpServletResponse response = mvcResult.getResponse();
                String jsonString = response.getContentAsString();
                Page<ItemDto> actualResult = objectMapper.readValue(jsonString,
                        new TypeReference<TestPageImpl<ItemDto>>()
                        {
                        });
                assertEquals(actualResult.getContent().size(), 1);
            }
        }
    }

    /**
     *  Given call to get an item by id
     */
    @Nested
    @DisplayName("Given call to get an item by id")
    class GivenCallToGetItemById 
    {
    	private MockHttpServletRequestBuilder requestBuilder;
    	
    	@BeforeEach
    	void setup() {
    		 requestBuilder = MockMvcRequestBuilders.get("/api/" + API_VERSION + "/items/123")
                        .characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON);
    	}
    	
    	/**
    	 * When item exists for id
    	 */
    	@Nested
    	@DisplayName("When item exists for id")
    	class WhenItemExistsForId
    	{
    		/**
    		 * Setup
    		 */
    		@BeforeEach
    		void setup() {
                ItemDto itemDto = new ItemDto();
                itemDto.setId(123L);
                Optional<ItemDto> optionalItemDto = Optional.of(itemDto);
                
                // have the service call return the optional itemdto
                Mockito.when(itemsService.getItemById(Mockito.anyLong())).thenReturn(optionalItemDto);
    		}
    		
    		/**
    		 * Then should return item
    		 * @throws Exception 
    		 */
    		@Test
    		void thenShouldReturnItem() throws Exception {
                
    			// make the call
                MvcResult mvcResult = mockMvc.perform(requestBuilder)
                        .andDo(MockMvcResultHandlers.print()).andReturn();
                
                // verify the response
                MockHttpServletResponse response = mvcResult.getResponse();
                String responseJson = response.getContentAsString();
                ItemDto returnedItemDto = objectMapper.readValue(responseJson,
                        new TypeReference<ItemDto>()
                        {
                        });
                assertEquals(123, returnedItemDto.getId());
    		}
    		
    		/**
    		 * Then should return 200 OK
    		 * @throws Exception 
    		 */
    		@Test
    		void thenShouldReturn200Ok() throws Exception {
    
                // make the request and check response code
                mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print()).andReturn();
    		}
    	}
    	
        /**
         * When Item is not found
         */
        @Nested
        @DisplayName("When Item is not found")
        class WhenItemIsNotFound
        {
            /**
             * Setup.
             */
            @BeforeEach
            void setup()
            {
                // have the service call return null
                Mockito.when(itemsService.getItemById(Mockito.anyLong())).thenReturn(Optional.empty());
            }

            /**
             * Then should return 404.
             *
             * @throws Exception the exception
             */
            @Test
            @DisplayName("Then should return 404 Not Found")
            void thenShouldReturn404() throws Exception
            {
                // make the request and check response code
                mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andDo(MockMvcResultHandlers.print()).andReturn();
            }
        }
    }
    
    /**
     * Given call to delete by id
     */
    @Nested
    @DisplayName("Given call to delete by id")
    class GivenCallToDeleteById
    {
        MockHttpServletRequestBuilder requestBuilder;

        @BeforeEach
        void setup() {
        	requestBuilder = MockMvcRequestBuilders.delete("/api/" + API_VERSION + "/items/123")
        			.characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON);
        }
        
        /**
         * When item exists for ID
         */
        @Nested
        @DisplayName("When item exists for ID")
        class WhenItemExistsForId
        {
            /**
             * Setup.
             */
            @BeforeEach
            void setup()
            {
                Mockito.doNothing().when(itemsService).deleteItemById(Mockito.anyLong());
            }

            @Test
            @DisplayName("Then should delete item")
            void thenShouldDeleteItem() throws Exception
            {
                // make the request and check response code
                mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNoContent())
                        .andDo(MockMvcResultHandlers.print()).andReturn();
            }
        }
    }
    
    /**
     * Given POST to create an item
     */
    @Nested
    @DisplayName("Given POST to create an item")
    class GivenPostToCreateAnItem
    {
        /**
         * When the item is null
         */
        @Nested
        @DisplayName("When the item is null")
        class WhenItemIsNull
        {
            final static String EXPECTED_ERROR = "Required request body is missing";
            MockHttpServletRequestBuilder requestBuilder;
    	
	        /**
	         * Setup.
	         *
	         * @throws JsonProcessingException the json processing exception
	         */
	        @BeforeEach
	        void setup() throws JsonProcessingException
	        {
	            // build the request
	            requestBuilder = MockMvcRequestBuilders.post("/api/" + API_VERSION + "/items")
	                    .characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON)
	                    .accept(MediaType.APPLICATION_JSON);
	        }
            
            /**
             * Then should throw 400.
             *
             * @throws Exception the exception
             */
            @Test
            @DisplayName("Then should throw 400 Bad Request")
            void thenShouldThrow400() throws Exception
            {
                // make the request and check response code
                mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andDo(MockMvcResultHandlers.print()).andReturn();
            }

            /**
             * Then response should be api error.
             *
             * @throws Exception the exception
             */
            @Test
            @DisplayName("Then response object should be ApiError")
            void thenResponseShouldBeApiError() throws Exception
            {
                // make the request and verify the ApiError object
                MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
                verifyApiError(mvcResult, HttpMessageNotReadableException.class, EXPECTED_ERROR);
            }
        }
        
        @Nested
        @DisplayName("When the item name is null")
        class WhenItemNameIsNull {
            final static String EXPECTED_ERROR = "name: Cannot be null.";
            MockHttpServletRequestBuilder requestBuilder;
            private ItemDto itemDto = null;
            
	        /**
	         * Setup.
	         *
	         * @throws JsonProcessingException the json processing exception
	         */
	        @BeforeEach
	        void setup() throws JsonProcessingException
	        {
	        	itemDto = new ItemDto();

                // build the request
                final String jsonContent = objectMapper.writeValueAsString(itemDto);
                requestBuilder = MockMvcRequestBuilders.post("/api/" + API_VERSION + "/items")
                        .characterEncoding("utf-8").content(jsonContent).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
	        }
	        
            /**
             * Then should throw 400.
             *
             * @throws Exception the exception
             */
            @Test
            @DisplayName("Then should throw 400 Bad Request")
            void thenShouldThrow400() throws Exception
            {
                // make the request and check response code
                mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andDo(MockMvcResultHandlers.print()).andReturn();
            }

            /**
             * Then response should be api error.
             *
             * @throws Exception the exception
             */
            @Test
            @DisplayName("Then response object should be ApiError")
            void thenResponseShouldBeApiError() throws Exception
            {
                // make the request and verify the ApiError object
                MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
                verifyApiError(mvcResult, HttpMessageNotReadableException.class, EXPECTED_ERROR);
            }
        }

        /**
         * When the item is valid
         */
        @Nested
        @DisplayName("When the item is Valid")
        class WhenItemIsValid
        {

            private ItemDto itemDto = null;
            private ConfirmationDto confirmationDto;
            MockHttpServletRequestBuilder requestBuilder;

            /**
             * Setup.
             *
             * @throws JsonProcessingException the json processing exception
             */
            @BeforeEach
            void setup() throws JsonProcessingException
            {
            	itemDto = new ItemDto();
            	itemDto.setId(123L);
            	itemDto.setName("foo bar");
            	
                // build the confirmation object
                confirmationDto = new ConfirmationDto();
                confirmationDto.setStatus(ItemStatus.SUBMITTED);
                confirmationDto.setId(123L);

                // handle service call
                Mockito.when(itemsService.saveItem(Mockito.any(ItemDto.class))).thenReturn(confirmationDto);

                // build the request
                final String jsonContent = objectMapper.writeValueAsString(itemDto);
                requestBuilder = MockMvcRequestBuilders.post("/api/" + API_VERSION + "/items")
                        .characterEncoding("utf-8").content(jsonContent).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
            }

            /**
             * Then should return 202 Accepted.
             *
             * @throws Exception the exception
             */
            @Test
            @DisplayName("Then should return 202 Accepted")
            void thenShouldReturn202() throws Exception
            {
                // make the request and check response code
                MvcResult mvcResult = mockMvc.perform(requestBuilder)
                        .andExpect(MockMvcResultMatchers.status().isAccepted()).andDo(MockMvcResultHandlers.print())
                        .andReturn();

                // evaluate the return object
                MockHttpServletResponse response = mvcResult.getResponse();
                assertNotNull(response);
            }

            /**
             * Then should contain response object.
             *
             * @throws Exception the exception
             */
            @Test
            @DisplayName("Then should contain a response object")
            void thenShouldContainResponseObject() throws Exception
            {
                // make the request and check response code
                MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

                // get the return object
                MockHttpServletResponse response = mvcResult.getResponse();
                String content = response.getContentAsString();

                // compare to expected content
                String expectedContent = objectMapper.writeValueAsString(confirmationDto);
                assertEquals(expectedContent, content);
            }
        }
    }
    
    /**
     * Verify api error.
     *
     * @param mvcResult            the mvc result
     * @param expectedException    the expected exception
     * @param expectedErrorMessage the expected error message
     * @throws Exception the exception
     */
    private void verifyApiError(MvcResult mvcResult, Class<?> expectedException, String expectedErrorMessage)
            throws Exception
    {
        // check the exception type
        Exception e = mvcResult.getResolvedException();
        assertThat(e.getClass().equals(expectedException));

        // get the response content as string
        MockHttpServletResponse response = mvcResult.getResponse();
        String content = response.getContentAsString();

        // convert it to a json object
        ApiError apiError = objectMapper.readValue(content, ApiError.class);

        // verify response error code
        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());

        // verify error is present in list
        List<String> errors = apiError.getErrors();
        assertTrue(errors.contains(expectedErrorMessage));
    }
}