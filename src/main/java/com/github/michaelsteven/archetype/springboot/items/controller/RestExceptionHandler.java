package com.github.michaelsteven.archetype.springboot.items.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.github.michaelsteven.archetype.springboot.items.model.ApiError;


/**
 * The Class CustomRestExceptionHandler.
 * 
 * Use this class to define exit error exception handling. The Controller Advice
 * annotation will make it available to the controller so that exceptions caught
 * below will be converted to a standard format prior to the error response
 * being returned.
 * 
 */
@ControllerAdvice(annotations = RestController.class)
@Component
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{

    /**
     * Handle method argument not valid.
     *
     * @param exception the exception
     * @param headers   the headers
     * @param status    the status
     * @param request   the request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        List<String> errors = new ArrayList<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors())
        {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : exception.getBindingResult().getGlobalErrors())
        {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
        return handleExceptionInternal(exception, apiError, headers, apiError.getStatus(), request);
    }

    /**
     * Handle missing servlet request parameter.
     *
     * @param exception the exception
     * @param headers   the headers
     * @param status    the status
     * @param request   the request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request)
    {
        String error = exception.getParameterName() + " parameter is missing";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle validation exception.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler({ ValidationException.class })
    public ResponseEntity<Object> handleValidationException(ValidationException exception, WebRequest request)
    {
        List<String> errors = new ArrayList<>();
        errors.add(exception.getLocalizedMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle constraint violation.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception, WebRequest request)
    {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations())
        {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
                    + violation.getMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle method argument type mismatch.
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception,
            WebRequest request)
    {
        String error = exception.getName() + " should be of type " + exception.getRequiredType().getName();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle HTTP server error exception.
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler({ HttpServerErrorException.class })
    public ResponseEntity<Object> handleHttpServerErrorException(HttpServerErrorException exception)
    {
        String error = exception.getMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_GATEWAY, exception.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle http request method not supported.
     *
     * @param exception the exception
     * @param headers   the headers
     * @param status    the status
     * @param request   the request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(exception.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        exception.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, exception.getLocalizedMessage(),
                builder.toString());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle HTTP media type not supported.
     *
     * @param exception the exception
     * @param headers   the headers
     * @param status    the status
     * @param request   the request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(exception.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        exception.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, exception.getLocalizedMessage(),
                builder.substring(0, builder.length() - 2));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle http message not readable.
     *
     * @param exception the exception
     * @param headers   the headers
     * @param status    the status
     * @param request   the request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(),
                "Required request body is missing");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle all. A catch-all / fallback handler
     *
     * @param exception the exception
     * @param request   the request
     * @return the response entity
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception exception, WebRequest request)
    {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage(),
                "error occurred");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
