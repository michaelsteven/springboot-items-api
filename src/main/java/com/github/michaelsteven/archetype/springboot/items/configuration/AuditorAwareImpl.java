package com.github.michaelsteven.archetype.springboot.items.configuration;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * The Class AuditorAwareImpl.
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

	/**
	 * Gets the current auditor.
	 *
	 * @return the current auditor
	 */
	@Override
	public Optional<String> getCurrentAuditor(){
		String username = "unknown";
		// SecurityContextHolder.getContext().getAuthentication().getName();
		return Optional.of(username);
	}
}
