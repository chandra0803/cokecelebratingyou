
package com.biperf.core.exception;

public class FieldValidationException extends Exception
{

  private static final long serialVersionUID = 1L;

  private ExceptionView exceptionView;

  public FieldValidationException()
  {
  }

  public FieldValidationException( ExceptionView exceptionView )
  {
    super( exceptionView.getResponseMessage() );
    this.exceptionView = exceptionView;
  }

  public FieldValidationException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace )
  {
    super( message, cause, enableSuppression, writableStackTrace );
  }

  public FieldValidationException( String message, Throwable cause )
  {
    super( message, cause );
  }

  public FieldValidationException( String message )
  {
    super( message );
  }

  public FieldValidationException( Throwable cause )
  {
    super( cause );
  }

  public ExceptionView getCustomException()
  {
    return exceptionView;
  }

}
