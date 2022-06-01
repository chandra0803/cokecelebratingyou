
package com.biperf.core.ui.goalquest;

public class ManagerGoalquestViewBean
{

  private Long promotionId;
  private String promotionName;
  private String role;
  private String startDate;
  private String endDate;
  private String objectiveText;
  private String rulesText;
  private String detailsLink;
  
  private boolean honeycombProgram; 

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }
  
  public String getRole()
  {
    return role;
  }
  
  public void setRole( String role )
  {
    this.role = role;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getObjectiveText()
  {
    return objectiveText;
  }

  public void setObjectiveText( String objectiveText )
  {
    this.objectiveText = objectiveText;
  }

  public String getRulesText()
  {
    return rulesText;
  }

  public void setRulesText( String rulesText )
  {
    this.rulesText = rulesText;
  }
  
  public String getDetailsLink()
  {
    return detailsLink;
  }

  public void setDetailsLink( String detailsLink )
  {
    this.detailsLink = detailsLink;
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
