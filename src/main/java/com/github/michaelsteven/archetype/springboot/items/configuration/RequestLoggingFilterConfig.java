package com.github.michaelsteven.archetype.springboot.items.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * The Class RequestLoggingFilterConfig.
 */
@Configuration
public class RequestLoggingFilterConfig {
	
	/**
	 * Log filter.
	 *
	 * @return the commons request logging filter
	 */
	@Bean
	public CommonsRequestLoggingFilter logFilter() {
		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
		loggingFilter.setIncludeQueryString(true);
		loggingFilter.setIncludePayload(true);
		loggingFilter.setMaxPayloadLength(10000);
		loggingFilter.setIncludeHeaders(true);
		return loggingFilter;
	}
}
