package com.github.michaelsteven.archetype.springboot.items.aspect;

import javax.persistence.PersistenceException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * The Repository Aspect class.
 */
@Aspect
public class RepositoryAspect {
	
	private MessageSource messageSource;
	
	/**
	 * Constructor
	 * 
	 * @param messageSource the message source
	 */
	public RepositoryAspect(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * Repository point cut.
	 */
	@Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))")
	public void repositoryPointCut() {
	}
	
	/**
	 * Convert to persistence exception.
	 *
	 * @param joinPoint the join point
	 * @param e the e
	 */
	@AfterThrowing(pointcut = "repositoryPointCut()", throwing = "e")
	public void convertToPersistenceException(JoinPoint joinPoint, Throwable e) {
		String message = messageSource.getMessage("repositoryaspect.persistenceexception.message", null, LocaleContextHolder.getLocale());
		throw new PersistenceException(message, e);
	}
}