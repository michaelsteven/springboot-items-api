package com.github.michaelsteven.archetype.springboot.items.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * The Class ItemDtoTest.
 */
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@DisplayName("ItemDTO Unit Tests")
public class ItemDtoTest {

	private Validator validator;
	
	/**
	 * Given Item DTO
	 */
	@Nested
	@DisplayName("Given Item DTO")
	class GivenItemDto{
	
		private ItemDto itemDto;

		/**
		 * Setup.
		 */
		@BeforeEach
		private void Setup() {
			itemDto = new ItemDto();
			
			ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
			validator = validatorFactory.getValidator();
		}
		
		/**
		 * When name is null
		 */
		@Nested
		@DisplayName("When name is null")
		class WhenNameNull {
			
			/**
			 * Then should fail validation.
			 */
			@Test
			@DisplayName("Then should fail validation")
			public void thenShouldFailValidation() {
				assertConstraintViolation(itemDto, "name", "Cannot be null");
			}
		}
		
		/**
		 * The Class WhenNameBlank.
		 */
		@Nested
		@DisplayName("When name is blank")
		class WhenNameBlank{

			/**
			 * Setup.
			 */
			@BeforeEach
			private void Setup() {
				itemDto.setName("");
			}
			
			/**
			 * Then should fail validation.
			 */
			@Test
			@DisplayName("Then should fail validation")
			public void thenShouldFailValidation() {
				assertConstraintViolation(itemDto, "name", "Invalid text size");
			}
		}
		
		
		/**
		 * When name is too long
		 */
		@Nested
		@DisplayName("When name is too long")
		class WhenNameTooLong{
			
			/**
			 * Setup.
			 */
			@BeforeEach
			private void Setup() {
				itemDto.setName( StringUtils.repeat("A", 26) );
			}
			
			/**
			 * Then should fail validation.
			 */
			@Test
			@DisplayName("Then should fail validation")
			public void thenShouldFailValidation() {
				assertConstraintViolation(itemDto, "name", "Invalid text size");
			}
		}
		
		/**
		 * The Class WhenDescriptionBlank.
		 */
		@Nested
		@DisplayName("When description is blank")
		class WhenDescriptionBlank{

			/**
			 * Setup.
			 */
			@BeforeEach
			private void Setup() {
				itemDto.setName("foobar");
				itemDto.setDescription("");
			}
			
			/**
			 * Then should fail validation.
			 */
			@Test
			@DisplayName("Then should fail validation")
			public void thenShouldFailValidation() {
				assertConstraintViolation(itemDto, "description", "Invalid text size");
			}
		}
		
		/**
		 * When description is too long
		 */
		@Nested
		@DisplayName("When description is too long")
		class WhenDescriptionTooLong{
			
			/**
			 * Setup.
			 */
			@BeforeEach
			private void Setup() {
				itemDto.setName("foobar");
				itemDto.setDescription( StringUtils.repeat("A", 201) );
			}
			
			/**
			 * Then should fail validation.
			 */
			@Test
			@DisplayName("Then should fail validation")
			public void thenShouldFailValidation() {
				assertConstraintViolation(itemDto, "description", "Invalid text size");
			}
		}
	}	
	
	
	/**
	 * Assert constraint violation.
	 *
	 * @param <T> the generic type
	 * @param itemDto the item dto
	 * @param fieldName the field name
	 * @param expectedMessage the expected message
	 */
	private <T> void assertConstraintViolation(T itemDto, String fieldName, String expectedMessage) {
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(itemDto);
		assertFalse(constraintViolations.isEmpty());				
		assertTrue( constraintViolations.stream().anyMatch(v -> containsPropertyName(v, fieldName) && v.getMessage().contains(expectedMessage)));
	}
	
	/**
	 * Contains property name.
	 *
	 * @param constraintViolation the constraint violation
	 * @param propertyName the property name
	 * @return true, if successful
	 */
	private boolean containsPropertyName( ConstraintViolation<?> constraintViolation, String propertyName) {
		Path path = constraintViolation.getPropertyPath();
		if(null != path && null != path.iterator()) {
			Iterator<Node> nodeIterator = path.iterator();
			while(nodeIterator.hasNext()) {
				Node node = nodeIterator.next();
				String name = node.getName();
				if(propertyName == name ) {
					return true;
				}
			}
		}
		return false;
	}
}
