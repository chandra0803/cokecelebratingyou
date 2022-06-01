
package com.biperf.core.ui.engagement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * EngagementSummaryView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementSummaryView
{
  private String type;
  private String title;
  private String description;
  private int target;
  private int actual;
  private int avgTeamMem;
  private int avgCompany;
  private int teamMemMetTarget;
  private String reportUrl;
  private String reportTitle;
  private String targetLabel;
  private String actualLabel;
  private boolean isLargeAudienceEnabled;

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public int getTarget()
  {
    return target;
  }

  public void setTarget( int target )
  {
    this.target = target;
  }

  public int getActual()
  {
    return actual;
  }

  public void setActual( int actual )
  {
    this.actual = actual;
  }

  public int getAvgTeamMem()
  {
    return avgTeamMem;
  }

  public void setAvgTeamMem( int avgTeamMem )
  {
    this.avgTeamMem = avgTeamMem;
  }

  public int getAvgCompany()
  {
    return avgCompany;
  }

  public void setAvgCompany( int avgCompany )
  {
    this.avgCompany = avgCompany;
  }

  public int getTeamMemMetTarget()
  {
    return teamMemMetTarget;
  }

  public void setTeamMemMetTarget( int teamMemMetTarget )
  {
    this.teamMemMetTarget = teamMemMetTarget;
  }

  public String getReportUrl()
  {
    return reportUrl;
  }

  public void setReportUrl( String reportUrl )
  {
    this.reportUrl = reportUrl;
  }

  public String getReportTitle()
  {
    return reportTitle;
  }

  public void setReportTitle( String reportTitle )
  {
    this.reportTitle = reportTitle;
  }

  public String getTargetLabel()
  {
    return targetLabel;
  }

  public void setTargetLabel( String targetLabel )
  {
    this.targetLabel = targetLabel;
  }

  public String getActualLabel()
  {
    return actualLabel;
  }

  public void setActualLabel( String actualLabel )
  {
    this.actualLabel = actualLabel;
  }

  @JsonProperty( "isLargeAudienceEnabled" )
  public boolean isLargeAudienceEnabled()
  {
    return isLargeAudienceEnabled;
  }

  public void setLargeAudienceEnabled( boolean isLargeAudienceEnabled )
  {
    this.isLargeAudienceEnabled = isLargeAudienceEnabled;
  }

}
