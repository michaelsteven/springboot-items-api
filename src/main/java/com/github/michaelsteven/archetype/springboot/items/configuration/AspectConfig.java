package com.github.michaelsteven.archetype.springboot.items.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.michaelsteven.archetype.springboot.items.aspect.ComplianceEventAspect;
import com.github.michaelsteven.archetype.springboot.items.aspect.LoggerAspect;
import com.github.michaelsteven.archetype.springboot.items.aspect.RepositoryAspect;

/**
 * The Class AspectConfig.
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {

	private ObjectMapper objectMapper;
	private MessageSource messageSource;
	
	/**
	 * Constructor.
	 *
	 * @param objectMapper the object mapper
	 * @param messageSource the message source
	 */
	public AspectConfig(ObjectMapper objectMapper, MessageSource messageSource) {
		this.objectMapper = objectMapper;
		this.messageSource = messageSource;
	}
	
	/**
	 * Logger aspect.
	 *
	 * @param environment the environment
	 * @return the logger aspect
	 */
	@Bean
	public LoggerAspect loggerAspect(Environment environment) {
		return new LoggerAspect(objectMapper);
	}
	
	/**
	 * Repository aspect.
	 *
	 * @param enviornment the environment
	 * @return the repository aspect
	 */
	@Bean
	public RepositoryAspect repositoryAspect(Environment enviornment) {
		return new RepositoryAspect(messageSource);
	}
	
	/**
	 * Compliance event.
	 *
	 * @param environment the environment
	 * @return the compliance event aspect
	 */
	@Bean
	public ComplianceEventAspect complianceEvent(Environment environment) {
		return new ComplianceEventAspect();
	}
}