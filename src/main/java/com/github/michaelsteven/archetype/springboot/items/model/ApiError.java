package com.github.michaelsteven.archetype.springboot.items.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data
@NoArgsConstructor
public class ApiError {

	/** The status. */
	private HttpStatus status;
	
	/** The message. */
	private String message;
	
	/** The errors. */
	private List<String> errors;
	
    /**
     * Instantiates a new api error.
     *
     * @param status the status
     * @param message the message
     * @param errors the errors
     */
    public ApiError(HttpStatus status, String message, List<String> errors) {
		this.status =  status;
		this.message = message;
		this.errors = errors;
	}
    
    /**
     * Instantiates a new api error.
     *
     * @param status the status
     * @param message the message
     * @param error the error
     */
    public ApiError(HttpStatus status, String message, String error) {
 		this.status =  status;
 		this.message = message;
 		this.errors = Arrays.asList(error);
 	}
}
