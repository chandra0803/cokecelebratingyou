
package com.biperf.core.value;

import java.util.Date;
import java.util.Set;

import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.DateUtils;

public class ThrowdownPromotionSelectorView
{
  private ThrowdownPromotion promotion;
  private String matchesUrl;
  private String rulesUrl;

  public ThrowdownPromotionSelectorView( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  @JsonProperty( "promoId" )
  public String getPromoId()
  {
    return String.valueOf( promotion.getId() );
  }

  @JsonProperty( "promoName" )
  public String getPromoName()
  {
    return promotion.getPromoNameFromCM();
  }

  @JsonProperty( "matchesUrl" )
  public String getMatchesUrl()
  {
    return matchesUrl;
  }

  public void setMatchesUrl( String matchesUrl )
  {
    this.matchesUrl = matchesUrl;
  }

  @JsonProperty( "rulesUrl" )
  public String getRulesUrl()
  {
    return rulesUrl;
  }

  public void setRulesUrl( String rulesUrl )
  {
    this.rulesUrl = rulesUrl;
  }

  /*
   * get the current round and provide the end date. If the promotion has yet to start, then always
   * show the first round's end date. If the promotion has ended, always show the last
   */
  @JsonProperty( "endDate" )
  public long getEndDate()
  {
    Set<Round> rounds = promotion.getDivisions().iterator().next().getRounds();
    Date now = new Date();
    // Rounds have not started yet, provide the first one
    Round currentRound = null;
    if ( now.before( promotion.getHeadToHeadStartDate() ) )
    {
      currentRound = rounds.iterator().next();
    }
    else
    {
      // iterate and get the current round
      for ( Round round : rounds )
      {
        currentRound = round;
        if ( DateUtils.isDateBetween( now, round.getStartDate(), round.getEndDate() ) )
        {
          break;
        }
      }
    }
    return currentRound.getEndDate().getTime();
  }
}
