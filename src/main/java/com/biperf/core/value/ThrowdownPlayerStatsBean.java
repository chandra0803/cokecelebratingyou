
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class ThrowdownPlayerStatsBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  private ThrowdownPromotion promotion;
  private List<ThrowdownMatchBean> matches = new ArrayList<ThrowdownMatchBean>();
  private ThrowdownTeamBean self;

  @JsonProperty( "rulesUrl" )
  public String getRulesUrl()
  {
    return "/urlToRules.do";
  }

  @JsonProperty( "numberOfRounds" )
  public int getNumberOfRounds()
  {
    return promotion.getNumberOfRounds();
  }

  @JsonProperty( "displayProgress" )
  public boolean isDisplayProgress()
  {
    return promotion.isDisplayTeamProgress();
  }

  @JsonProperty( "promotionId" )
  public Long getPromotionId()
  {
    return promotion.getId();
  }

  @JsonIgnore
  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  @JsonIgnore
  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  @JsonProperty( "matches" )
  public List<ThrowdownMatchBean> getMatches()
  {
    return matches;
  }

  public void setMatches( List<ThrowdownMatchBean> matches )
  {
    this.matches = matches;
  }

  @JsonProperty( "self" )
  public ThrowdownTeamBean getSelf()
  {
    return self;
  }

  @JsonIgnore
  public void setSelf( ThrowdownTeamBean self )
  {
    this.self = self;
  }

  public void addMatch( ThrowdownMatchBean match )
  {
    this.matches.add( match );
  }

}
