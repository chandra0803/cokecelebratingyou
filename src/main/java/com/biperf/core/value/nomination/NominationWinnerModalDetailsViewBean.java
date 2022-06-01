
package com.biperf.core.value.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationWinnerModalDetailsViewBean
{
  private String type;
  private String name;
  NominationWinnerModalDataViewBean nominationWinnerModalDataViewBean = new NominationWinnerModalDataViewBean();

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  @JsonProperty( "data" )
  public NominationWinnerModalDataViewBean getNominationWinnerModalDataViewBean()
  {
    return nominationWinnerModalDataViewBean;
  }

  public void setNominationWinnerModalDataViewBean( NominationWinnerModalDataViewBean nominationWinnerModalDataViewBean )
  {
    this.nominationWinnerModalDataViewBean = nominationWinnerModalDataViewBean;
  }
}
