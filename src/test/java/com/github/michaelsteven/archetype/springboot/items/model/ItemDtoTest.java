/* 
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.    
*/
package com.github.michaelsteven.archetype.springboot.items.model;

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
public class ItemDtoTest extends BaseDtoTest {
	
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
}
