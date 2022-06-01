
package com.biperf.core.exception;

import java.io.Serializable;

public class FieldValidation implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String field;

  private String message;

  public FieldValidation()
  {
  }

  public FieldValidation( String field, String message )
  {
    this.field = field;
    this.message = message;
  }

  public String getField()
  {
    return field;
  }

  public void setField( String field )
  {
    this.field = field;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  @Override
  public String toString()
  {
    return field + message;
  }
}
