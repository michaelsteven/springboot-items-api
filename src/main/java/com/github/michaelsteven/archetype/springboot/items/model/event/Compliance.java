package com.github.michaelsteven.archetype.springboot.items.model.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The compliance annotation
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Compliance {
	
	/**
	 * Action.
	 *
	 * @return the compliance action
	 */
	ComplianceAction action();
}
