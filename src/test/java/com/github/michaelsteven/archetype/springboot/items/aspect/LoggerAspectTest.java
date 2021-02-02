package com.github.michaelsteven.archetype.springboot.items.aspect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.PersistenceException;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.michaelsteven.archetype.springboot.items.service.ItemsService;

/**
 * Logger aspect unit tests
 */
@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(classes = {LoggerAspectTest.Config.class} )
@DisplayName("LoggerAspect Unit Tests")
public class LoggerAspectTest {

	@Autowired
	Logger loggerMock;
	
	private ItemsService itemsServiceProxy;
	private ItemsService itemsServiceMock;
	
	/**
	 * the configuration class used to load beans for the test
	 */
	static class Config{
		@Bean
		public Logger logger() {
			return Mockito.mock(Logger.class);
		}
	}
	
	/*
	 * A test implementation of the LoggerAspct class that overrides a static call, returning the local logger mock
	 */
	private class LoggerAspectUnderTest extends LoggerAspect {
		public LoggerAspectUnderTest(ObjectMapper objectMapper) {
			super(objectMapper);
		}

		@Override
		Logger getLogger(JoinPoint joinPoint){
			return loggerMock;
		}
	}
	
	/**
	 * Setup
	 */
	@BeforeEach
	void setup() {
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		itemsServiceMock = Mockito.mock(ItemsService.class);
		
		LoggerAspectUnderTest loggerAspectUnderTest = new LoggerAspectUnderTest(objectMapper);

		AspectJProxyFactory factory = new AspectJProxyFactory(itemsServiceMock);
		factory.addAspect(loggerAspectUnderTest);
		itemsServiceProxy = factory.getProxy();
	}
	
	
	/**
	 * Canary test
	 */
    @Test
    @DisplayName("Canary Test")
    void canaryTest()
    {
        assertThat(loggerMock).isNotNull();
    }
    
    
    /**
     * Given call to service method
     */
    @Nested
    @DisplayName("Given call to service method")
    class GivenCallToServiceMethod{
    	
    	@BeforeEach
    	void setup() {
			Mockito.reset(loggerMock);
    	}
    	
    	/**
    	 * When call is public
    	 */
    	@Nested
    	@DisplayName("When call is public")
    	class WhenCallIsPublic {
    		
    		/**
    		 * Then should trace log the entry and exit
    		 */
    		@Test
    		@DisplayName("Then should trace log the entry and exit")
    		void thenShouldTraceLogEntryandExit() {
    			itemsServiceProxy.getItemById(12345L);
    			Mockito.verify(loggerMock, Mockito.times(2)).trace(Mockito.anyString(), Mockito.anyString(), Mockito.any(Object.class));
    		}		
    	}
    	
    	
    	/**
    	 * When exception is thrown
    	 */
    	@Nested
    	@DisplayName("When exception is thrown")
    	class WhenExceptionIsThrown {
    		
    		@BeforeEach
    		void setup() {
    			PersistenceException exception = new PersistenceException("test message", new RuntimeException("foobar"));
    			Mockito.when(itemsServiceMock.getItemById(Mockito.anyLong())).thenThrow(exception);
    		}
    		
    		/**
    		 * Then should log exception
    		 */
    		@Test
    		@DisplayName("Then should log exception")
    		void thenShouldLogException() {
    			assertThrows(PersistenceException.class, () -> {
    				itemsServiceProxy.getItemById(12345L);
    			});
    			Mockito.verify(loggerMock, Mockito.times(1)).trace(Mockito.anyString(), Mockito.anyString(), Mockito.any(Object.class));
    			Mockito.verify(loggerMock, Mockito.times(1)).error(Mockito.anyString(), Mockito.anyString(), Mockito.any(RuntimeException.class), Mockito.anyString(), Mockito.any(PersistenceException.class) );
    			Mockito.verify(loggerMock, Mockito.times(1)).error(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    			Mockito.verifyNoMoreInteractions(loggerMock);
    		}
    	}
    }
}
