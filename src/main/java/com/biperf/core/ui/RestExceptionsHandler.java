
package com.biperf.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.biperf.core.exception.BadRequestException;
import com.biperf.core.exception.ExceptionView;
import com.biperf.core.exception.FieldValidation;
import com.biperf.core.exception.FieldValidationException;
import com.biperf.core.exception.ResourceNotFoundException;
import com.biperf.core.exception.ServiceNotAvailableException;
import com.biperf.core.exception.UnauthorizedException;

@ControllerAdvice
public class RestExceptionsHandler extends ResponseEntityExceptionHandler
{

  private static final Log LOGGER = LogFactory.getLog( RestExceptionsHandler.class );

  @ResponseStatus( HttpStatus.SERVICE_UNAVAILABLE )
  @ExceptionHandler( ServiceNotAvailableException.class )
  public ResponseEntity<Object> serviceUnavailableHandler( RuntimeException ex, WebRequest request )
  {
    ExceptionView exceptionView = new ExceptionView( HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getLocalizedMessage(), ExceptionUtils.getStackTrace( ex ) );
    LOGGER.error( ex );
    return handleExceptionInternal( ex, exceptionView, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request );
  }

  @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
  @ExceptionHandler( Exception.class )
  public ResponseEntity<Object> internalServerError( Exception ex, WebRequest request )
  {
    ExceptionView exceptionView = new ExceptionView( HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getLocalizedMessage(), ExceptionUtils.getStackTrace( ex ) );
    LOGGER.error( ex.getMessage(), ex );
    return handleExceptionInternal( ex, exceptionView, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request );
  }

  @ResponseStatus( HttpStatus.UNAUTHORIZED )
  @ExceptionHandler( UnauthorizedException.class )
  public ResponseEntity<Object> unauthorizedHandler( RuntimeException ex, WebRequest request )
  {
    ExceptionView exceptionView = new ExceptionView( HttpStatus.UNAUTHORIZED.value(),
                                                     getErrorMessage( ex.getLocalizedMessage() ),
                                                     ExceptionUtils.getStackTrace( ex ),
                                                     getErrorCode( ex.getLocalizedMessage() ) );
    LOGGER.error( ex );
    return handleExceptionInternal( ex, exceptionView, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request );
  }

  @ResponseStatus( HttpStatus.NOT_FOUND )
  @ExceptionHandler( ResourceNotFoundException.class )
  public ResponseEntity<Object> resourceNotFoundHandler( RuntimeException ex, WebRequest request )
  {
    ExceptionView exceptionView = new ExceptionView( HttpStatus.NOT_FOUND.value(), ex.getLocalizedMessage(), ExceptionUtils.getStackTrace( ex ) );
    LOGGER.error( ex );
    return handleExceptionInternal( ex, exceptionView, new HttpHeaders(), HttpStatus.NOT_FOUND, request );
  }

  @ResponseStatus( HttpStatus.BAD_REQUEST )
  @ExceptionHandler( BadRequestException.class )
  public ResponseEntity<Object> badRequestHandler( RuntimeException ex, WebRequest request )
  {
    ExceptionView exceptionView = new ExceptionView( HttpStatus.BAD_REQUEST.value(),
                                                     getErrorMessage( ex.getLocalizedMessage() ),
                                                     ExceptionUtils.getStackTrace( ex ),
                                                     getErrorCode( ex.getLocalizedMessage() ) );
    LOGGER.error( ex );
    return handleExceptionInternal( ex, exceptionView, new HttpHeaders(), HttpStatus.BAD_REQUEST, request );
  }

  @ResponseStatus( HttpStatus.PRECONDITION_FAILED )
  @ExceptionHandler( FieldValidationException.class )
  public ResponseEntity<Object> fieldValidationException( FieldValidationException ex, WebRequest request )
  {
    ExceptionView exceptionView = new ExceptionView( HttpStatus.PRECONDITION_FAILED.value(), ex.getLocalizedMessage(), ExceptionUtils.getStackTrace( ex ) );
    exceptionView.setFieldErrors( ex.getCustomException() != null ? ex.getCustomException().getFieldErrors() : null );
    LOGGER.error( ex );
    return handleExceptionInternal( ex, exceptionView, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED, request );
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid( MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request )
  {
    ExceptionView exceptionView = new ExceptionView( HttpStatus.PRECONDITION_FAILED.value(), ex.getLocalizedMessage(), ExceptionUtils.getStackTrace( ex ) );
    List<FieldValidation> fieldErrors = new ArrayList<FieldValidation>();
    BindingResult result = ex.getBindingResult();
    for ( FieldError fieldError : result.getFieldErrors() )
    {
      fieldErrors.add( new FieldValidation( fieldError.getField(), fieldError.getDefaultMessage() ) );
    }
    exceptionView.setFieldErrors( fieldErrors );
    LOGGER.error( ex );
    return handleExceptionInternal( ex, exceptionView, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED, request );

  }

  private String getErrorMessage( String localizedMessage )
  {
    if ( localizedMessage.substring( 0, 1 ).matches( "[0-9]" ) )
    {
      return localizedMessage.substring( 8, localizedMessage.length() );
    }
    return localizedMessage;
  }

  private Integer getErrorCode( String localizedMessage )
  {
    if ( localizedMessage.substring( 0, 1 ).matches( "[0-9]" ) )
    {
      String value = localizedMessage.substring( 0, 5 );
      return Integer.valueOf( value );
    }
    return null;
  }

}