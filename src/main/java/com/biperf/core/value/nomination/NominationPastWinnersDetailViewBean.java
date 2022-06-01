
package com.biperf.core.value.nomination;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationPastWinnersDetailViewBean
{
  private List<NominationWinnersDetailViewBean> nominationWinnersDetail;

  @JsonProperty( "nominationWinnersDetail" )
  public List<NominationWinnersDetailViewBean> getNominationWinnersDetail()
  {
    return nominationWinnersDetail;
  }

  public void setNominationWinnersDetail( List<NominationWinnersDetailViewBean> nominationWinnersDetail )
  {
    this.nominationWinnersDetail = nominationWinnersDetail;
  }
}
