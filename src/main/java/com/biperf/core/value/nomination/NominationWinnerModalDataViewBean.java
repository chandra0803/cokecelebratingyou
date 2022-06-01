
package com.biperf.core.value.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationWinnerModalDataViewBean
{
  NominationWinnerModalDisplayDetaillsViewBean nominationWinnerModalDisplayDetaillsViewBean = new NominationWinnerModalDisplayDetaillsViewBean();

  @JsonProperty( "modal" )
  public NominationWinnerModalDisplayDetaillsViewBean getNominationWinnerModalDisplayDetaillsViewBean()
  {
    return nominationWinnerModalDisplayDetaillsViewBean;
  }

  public void setNominationWinnerModalDisplayDetaillsViewBean( NominationWinnerModalDisplayDetaillsViewBean nominationWinnerModalDisplayDetaillsViewBean )
  {
    this.nominationWinnerModalDisplayDetaillsViewBean = nominationWinnerModalDisplayDetaillsViewBean;
  }
}
