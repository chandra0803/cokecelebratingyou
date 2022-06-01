
package com.biperf.core.value.claim;

import java.io.Serializable;

public class ClaimRecipientValueObject implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String nodeName;
  private String displayName;
  private String positionTypepicklistName;
  private String departmentTypePickListName;
  private Long claimItemId;
  private Long calculatorScore;
  private Long recipientItemId;
  private Long awardQuantity;
  private String awardTypeName;
  private String recipientDisplayName;

  public String getRecipientDisplayName()
  {
    return recipientDisplayName;
  }

  public void setRecipientDisplayName( String recipientDisplayName )
  {
    this.recipientDisplayName = recipientDisplayName;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName( String displayName )
  {
    this.displayName = displayName;
  }

  public String getDepartmentTypePickListName()
  {
    return departmentTypePickListName;
  }

  public void setDepartmentTypePickListName( String departmentTypePickListName )
  {
    this.departmentTypePickListName = departmentTypePickListName;
  }

  public Long getClaimItemId()
  {
    return claimItemId;
  }

  public void setClaimItemId( Long claimItemId )
  {
    this.claimItemId = claimItemId;
  }

  public Long getCalculatorScore()
  {
    return calculatorScore;
  }

  public void setCalculatorScore( Long calculatorScore )
  {
    this.calculatorScore = calculatorScore;
  }

  public Long getRecipientItemId()
  {
    return recipientItemId;
  }

  public void setRecipientItemId( Long recipientItemId )
  {
    this.recipientItemId = recipientItemId;
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public String getAwardTypeName()
  {
    return awardTypeName;
  }

  public void setAwardTypeName( String awardTypeName )
  {
    this.awardTypeName = awardTypeName;
  }

  public String getPositionTypepicklistName()
  {
    return positionTypepicklistName;
  }

  public void setPositionTypepicklistName( String positionTypepicklistName )
  {
    this.positionTypepicklistName = positionTypepicklistName;
  }

}
