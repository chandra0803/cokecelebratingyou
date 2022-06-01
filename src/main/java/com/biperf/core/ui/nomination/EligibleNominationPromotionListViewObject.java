
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( Include.NON_NULL )
public class EligibleNominationPromotionListViewObject
{

  private List<EligibleNominationPromotionViewObject> nominations = new ArrayList<EligibleNominationPromotionViewObject>();
  private boolean showPastWinnersLink;
  private int totalPromotionCount;

  @JsonProperty( "nominations" )
  public List<EligibleNominationPromotionViewObject> getNominations()
  {
    return nominations;
  }

  public void setNominations( List<EligibleNominationPromotionViewObject> nomPromos )
  {
    this.nominations = nomPromos;
  }

  public void addPromotionView( EligibleNominationPromotionViewObject view )
  {
    this.nominations.add( view );
  }

  public boolean isShowPastWinnersLink()
  {
    return showPastWinnersLink;
  }

  public void setShowPastWinnersLink( boolean showPastWinnersLink )
  {
    this.showPastWinnersLink = showPastWinnersLink;
  }

  public int getTotalPromotionCount()
  {
    return totalPromotionCount;
  }

  public void setTotalPromotionCount( int totalPromotionCount )
  {
    this.totalPromotionCount = totalPromotionCount;
  }
}
