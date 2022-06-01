
package com.biperf.core.ui.user;

import java.io.Serializable;

import com.biperf.core.exception.ExceptionView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_EMPTY )
public class InvalidTokenView extends ExceptionView implements Serializable
{
  private boolean tokenValid = false;// this is always false if this object gets returned

  public boolean isTokenValid()
  {
    return tokenValid;
  }
 
}
