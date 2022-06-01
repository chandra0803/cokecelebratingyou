
package com.biperf.core.domain.gamification;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class BadgeInfo
{
  private String noBadgesCompleteText;
  private String headerTitle;
  private boolean showProgress;
  private Long allBehaviorPoints;

  @JsonProperty( "badges" )
  private List<BadgeDetails> badgeDetails = new ArrayList<BadgeDetails>();

  public String getNoBadgesCompleteText()
  {
    return noBadgesCompleteText;
  }

  public void setNoBadgesCompleteText( String noBadgesCompleteText )
  {
    this.noBadgesCompleteText = noBadgesCompleteText;
  }

  public List<BadgeDetails> getBadgeDetails()
  {
    return badgeDetails;
  }

  public void setBadgeDetails( List<BadgeDetails> badgeDetails )
  {
    this.badgeDetails = badgeDetails;
  }

  public String getHeaderTitle()
  {
    return headerTitle;
  }

  public void setHeaderTitle( String headerTitle )
  {
    this.headerTitle = headerTitle;
  }

  public Long getAllBehaviorPoints()
  {
    return allBehaviorPoints;
  }

  public void setAllBehaviorPoints( Long allBehaviorPoints )
  {
    this.allBehaviorPoints = allBehaviorPoints;
  }

  public boolean isShowProgress()
  {
    return showProgress;
  }

  public void setShowProgress( boolean showProgress )
  {
    this.showProgress = showProgress;
  }

}
