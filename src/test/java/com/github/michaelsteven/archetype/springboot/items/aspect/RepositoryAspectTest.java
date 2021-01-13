package com.github.michaelsteven.archetype.springboot.items.aspect;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.michaelsteven.archetype.springboot.items.aspect.RepositoryAspect;
import com.github.michaelsteven.archetype.springboot.items.model.ItemEntity;

/**
 * RepositoryAspect Unit Tests
 */
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@DisplayName("RepositoryAspect Unit Tests")
public class RepositoryAspectTest {
	
	/** The jpa repository mock. */
	private JpaRepository<ItemEntity, Long> jpaRepositoryMock;
	
	/** The jpa repository proxy. */
	private JpaRepository<ItemEntity, Long> jpaRepositoryProxy;
	
	/** The message source mock. */
	private MessageSource messageSourceMock;
	
	/**
	 * Setup.
	 */
	@SuppressWarnings("unchecked")
	@BeforeEach
	void setup() {
		jpaRepositoryMock = Mockito.mock(JpaRepository.class);
		messageSourceMock = Mockito.mock(MessageSource.class);
		
		RepositoryAspect repositoryAspect = new RepositoryAspect(messageSourceMock);
		
		AspectJProxyFactory factory = new AspectJProxyFactory(jpaRepositoryMock);
		factory.addAspect(repositoryAspect);
		jpaRepositoryProxy = factory.getProxy();
	}

	/**
	 * Given call to JPA Repository
	 */
	@Nested
	@DisplayName("Given call to JPA Repository")
	class GivenCallToJpaRepository {
		
		/**
		 * When exception is thrown
		 */
		@Nested
		@DisplayName("When exception is thrown")
		class WhenExceptionIsThrown{
			
			/** The expected message. */
			private String expectedMessage = "Fake exception message";
			
			/**
			 * Setup.
			 */
			@BeforeEach
			void setup() {
				Mockito.when(jpaRepositoryMock.findById(Mockito.anyLong())).thenThrow( new RuntimeException());
				Mockito.when(messageSourceMock.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class)))
				   .thenReturn(expectedMessage);
			}
			
			/**
			 * Then should throw persistence exception.
			 */
			@Test
			@DisplayName("Then should throw persistence exception")
			public void thenShouldThrowPersistenceException() {
				Exception exception = assertThrows(PersistenceException.class, () -> {
					jpaRepositoryProxy.findById(123L);
				});
				assertTrue(exception.getMessage().contains(expectedMessage));
			}
		}
	}
}
