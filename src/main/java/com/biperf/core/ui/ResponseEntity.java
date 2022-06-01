
package com.biperf.core.ui;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( Include.NON_NULL )
public class ResponseEntity<M, D>
{
  @JsonProperty( "message" )
  private M message;
  @JsonProperty( "data" )
  private D data;

  public ResponseEntity( M message, D data )
  {
    this.data = data;
    this.message = message;
  }

  public M getMessage()
  {
    return message;
  }

  public void setMessage( M message )
  {
    this.message = message;
  }

  public D getData()
  {
    return data;
  }

  public void setData( D data )
  {
    this.data = data;
  }

}
