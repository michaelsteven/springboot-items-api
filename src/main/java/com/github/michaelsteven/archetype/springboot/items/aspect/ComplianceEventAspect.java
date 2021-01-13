package com.github.michaelsteven.archetype.springboot.items.aspect;

import java.util.concurrent.CompletableFuture;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import com.github.michaelsteven.archetype.springboot.items.model.event.Compliance;
import com.github.michaelsteven.archetype.springboot.items.model.event.ComplianceEvent;

/**
 * The Class ComplianceEventAspect.
 */
@Aspect
public class ComplianceEventAspect {
	
	/**
	 * Log compliance event.
	 *
	 * @param joinPoint the join point
	 * @param compliance the compliance
	 */
	@After("@annotation(compliance)")
	public void logComplianceEvent(JoinPoint joinPoint, Compliance compliance) {
		CompletableFuture.runAsync(() -> {
			ComplianceEvent complianceEvent = buildComplianceEvent(joinPoint, compliance);
			// TODO: go do something to record the compliance event
		});
	}
	
	/**
	 * Builds the compliance event.
	 *
	 * @param joinPoint the join point
	 * @param compliance the compliance
	 * @return the compliance event
	 */
	private ComplianceEvent buildComplianceEvent(JoinPoint joinPoint, Compliance compliance) {
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		
		ComplianceEvent complianceEvent = new ComplianceEvent();
		complianceEvent.setAction(compliance.action());
		complianceEvent.setResource(className);
		complianceEvent.setEventSource(className + "." + methodName);
		return complianceEvent;
	}
}
