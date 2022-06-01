
package com.biperf.core.domain.goalquest;

import java.util.HashMap;
import java.util.Map;

import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.utils.ClientStateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class GoalsView
{
  /* Start non-serializaed parms */
  private Long promotionId;
  private String paxGoalId;
  private String id;
  private String servletContext;
  /* End non-serializaed parms */

  private boolean isPartner;
  private boolean canChange;
  private String progressValue = null;
  private String progressDate = null;
  private boolean isAchieved;
  private GoalLevelView goalLevel;
  private ParticipantView participant;
  private GoalQuestPromotion promotion;
  private PaxGoal paxGoal;
  private boolean showProgress;
  private boolean percentageExceeds = false;
  
  private String selectGoalLink = null;
  private String rulesLink = null;
  private String progressLink = null;
  private String resultsLink = null;

  public String getSelectGoalLink()
  {
    if ( selectGoalLink == null )
    {
      return ClientStateUtils.generateEncodedLink( "", servletContext + "/goalquest/selectGoal.do", buildParameterMap() );
    }
    else
    {
      return selectGoalLink;
    }
  }

  public String getRulesLink()
  {
    if ( rulesLink == null )
    {
      return ClientStateUtils.generateEncodedLink( "", servletContext + "/goalquest/goalquestShowRules.do?method=showRulesDetail", buildParameterMap() );
    }
    else
    {
      return rulesLink;
    }
  }

  public String getProgressLink()
  {
    if ( progressLink == null )
    {
      return ClientStateUtils.generateEncodedLink( "", servletContext + "/goalquest/detail.do?method=showProgress", buildParameterMap() );
    }
    else
    {
      return progressLink;
    }
  }

  public String getResultsLink()
  {
    if ( resultsLink == null )
    {
      return ClientStateUtils.generateEncodedLink( "", servletContext + "/goalquest/detail.do?method=showDetail", buildParameterMap() );
    }
    else
    {
      return resultsLink;
    }
  }

  @JsonIgnore
  public String getServletContext()
  {
    return servletContext;
  }

  @JsonIgnore
  public void setServletContext( String servletContext )
  {
    this.servletContext = servletContext;
  }

  @JsonIgnore
  public Long getPromotionId()
  {
    return promotionId;
  }

  @JsonIgnore
  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  @JsonIgnore
  public String getPaxGoalId()
  {
    return paxGoalId;
  }

  @JsonIgnore
  public void setPaxGoalId( String paxGoalId )
  {
    this.paxGoalId = paxGoalId;
  }

  @JsonIgnore
  public String getId()
  {
    return id;
  }

  @JsonIgnore
  public void setId( String id )
  {
    this.id = id;
  }

  public boolean getIsPartner()
  {
    return isPartner;
  }

  public void setIsPartner( boolean isPartner )
  {
    this.isPartner = isPartner;
  }

  public boolean isCanChange()
  {
    return canChange;
  }

  public void setCanChange( boolean canChange )
  {
    this.canChange = canChange;
  }

  public String getProgressValue()
  {
    return progressValue;
  }

  public void setProgressValue( String progressValue )
  {
    this.progressValue = progressValue;
  }

  public String getProgressDate()
  {
    return progressDate;
  }

  public void setProgressDate( String progressDate )
  {
    this.progressDate = progressDate;
  }

  public boolean getIsAchieved()
  {
    return isAchieved;
  }

  public void setIsAchieved( boolean isAchieved )
  {
    this.isAchieved = isAchieved;
  }

  public GoalLevelView getGoalLevel()
  {
    return goalLevel;
  }

  public void setGoalLevel( GoalLevelView goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public ParticipantView getParticipant()
  {
    return participant;
  }

  public void setParticipant( ParticipantView participant )
  {
    this.participant = participant;
  }

  public Map<String, Object> buildParameterMap()
  {
    Map<String, Object> parms = new HashMap<String, Object>();
    parms.put( "promotionId", promotionId );
    parms.put( "paxGoalId", paxGoalId );
    parms.put( "isPartner", String.valueOf( isPartner ) );
    return parms;
  }

  public GoalQuestPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( GoalQuestPromotion promotion )
  {
    this.promotion = promotion;
  }

  public PaxGoal getPaxGoal()
  {
    return paxGoal;
  }

  public void setPaxGoal( PaxGoal paxGoal )
  {
    this.paxGoal = paxGoal;
  }

  public boolean isShowProgress()
  {
    return showProgress;
  }

  public void setShowProgress( boolean showProgress )
  {
    this.showProgress = showProgress;
  }

  public boolean isPercentageExceeds()
  {
    return percentageExceeds;
  }
  
  public void setPercentageExceeds( boolean percentageExceeds )
  {
    this.percentageExceeds = percentageExceeds;
  }

  public void setSelectGoalLink( String selectGoalLink )
  {
    this.selectGoalLink = selectGoalLink;
  }

  public void setRulesLink( String rulesLink )
  {
    this.rulesLink = rulesLink;
  }

  public void setProgressLink( String progressLink )
  {
    this.progressLink = progressLink;
  }

  public void setResultsLink( String resultsLink )
  {
    this.resultsLink = resultsLink;
  }

}
