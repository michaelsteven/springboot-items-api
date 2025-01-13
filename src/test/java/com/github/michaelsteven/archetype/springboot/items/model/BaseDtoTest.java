package com.github.michaelsteven.archetype.springboot.items.model;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Path.Node;

public abstract class BaseDtoTest {

	protected Validator validator;
	
	
	/**
	 * Assert constraint violation.
	 *
	 * @param <T> the generic type
	 * @param itemDto the item dto
	 * @param fieldName the field name
	 * @param expectedMessage the expected message
	 */
	protected <T> void assertConstraintViolation(T itemDto, String fieldName, String expectedMessage) {
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(itemDto);
		assertFalse(constraintViolations.isEmpty());				
		assertTrue( constraintViolations.stream().anyMatch(v -> containsPropertyName(v, fieldName) 
				                                                && v.getMessage().contains(expectedMessage)));
	}
	
	@BeforeEach
	protected void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	/**
	 * Contains property name.
	 *
	 * @param constraintViolation the constraint violation
	 * @param propertyName the property name
	 * @return true, if successful
	 */
	protected boolean containsPropertyName( ConstraintViolation<?> constraintViolation, String propertyName) {
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
