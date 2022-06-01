
package com.biperf.core.value;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class BaseJsonView implements Serializable
{
  private static final long serialVersionUID = 1L;

  private boolean visible = true;
  
  public BaseJsonView()
  {
    // default constructor
  }

  public BaseJsonView( boolean visible )
  {
    this.visible = visible;
  }

  @JsonProperty( "visible" )
  public boolean isVisible()
  {
    return visible;
  }

  public void setVisible( boolean visible )
  {
    this.visible = visible;
  }

  public static BaseJsonView getHideView()
  {
    return new BaseJsonView( false );
  }

}
