
package com.biperf.core.ui.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NomineeViewBean
{

  @JsonProperty( "nominee" )
  private String nominee;

  public String getNominee()
  {
    return nominee;
  }

  public void setNominee( String nominee )
  {
    this.nominee = nominee;
  }

}
