
package com.biperf.core.domain.goalquest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PromotionView
{
  private String id;
  private String name;
  private String startDate;
  private String status;
  private List<GoalsView> goals;
  private String promoStartDate;
  private String promoEndDate;
  private String finalProcessDate;
  private String awardIssueRun;
  private String tileDisplayEndDate;
  private boolean ua = false;
  private boolean uaconnected = false;
  private String uaAuthorizeUrl;
  private boolean honeycombProgram;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public List<GoalsView> getGoals()
  {
    return goals;
  }

  public void setGoals( List<GoalsView> goals )
  {
    this.goals = goals;
  }

  public String getPromoStartDate()
  {
    return promoStartDate;
  }

  public void setPromoStartDate( String promoStartDate )
  {
    this.promoStartDate = promoStartDate;
  }

  public String getPromoEndDate()
  {
    return promoEndDate;
  }

  public void setPromoEndDate( String promoEndDate )
  {
    this.promoEndDate = promoEndDate;
  }

  public String getFinalProcessDate()
  {
    return finalProcessDate;
  }

  public void setFinalProcessDate( String finalProcessDate )
  {
    this.finalProcessDate = finalProcessDate;
  }

  public String getAwardIssueRun()
  {
    return awardIssueRun;
  }

  public void setAwardIssueRun( String awardIssueRun )
  {
    this.awardIssueRun = awardIssueRun;
  }

  public String getTileDisplayEndDate()
  {
    return tileDisplayEndDate;
  }

  public void setTileDisplayEndDate( String tileDisplayEndDate )
  {
    this.tileDisplayEndDate = tileDisplayEndDate;
  }

  @JsonProperty( "ua" )
  public boolean isUa()
  {
    return ua;
  }

  public void setUa( boolean ua )
  {
    this.ua = ua;
  }

  @JsonProperty( "uaconnected" )
  public boolean isUaconnected()
  {
    return uaconnected;
  }

  public void setUaconnected( boolean uaconnected )
  {
    this.uaconnected = uaconnected;
  }

  public String getUaAuthorizeUrl()
  {
    return uaAuthorizeUrl;
  }

  public void setUaAuthorizeUrl( String uaAuthorizeUrl )
  {
    this.uaAuthorizeUrl = uaAuthorizeUrl;
  }

  public boolean isHoneycombProgram()
  {
    return honeycombProgram;
  }

  public void setHoneycombProgram( boolean honeycombProgram )
  {
    this.honeycombProgram = honeycombProgram;
  }

}
