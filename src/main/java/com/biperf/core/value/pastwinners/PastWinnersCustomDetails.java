
package com.biperf.core.value.pastwinners;

public class PastWinnersCustomDetails
{
  private Long claimId;
  private Long claimFormStepElementId;
  private String claimFormStepElementName;
  private String claimFormStepElementDesc;
  private String claimFormValue;
  private Long sequenceNumber;
  private boolean why;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getClaimFormStepElementId()
  {
    return claimFormStepElementId;
  }

  public void setClaimFormStepElementId( Long claimFormStepElementId )
  {
    this.claimFormStepElementId = claimFormStepElementId;
  }

  public String getClaimFormStepElementName()
  {
    return claimFormStepElementName;
  }

  public void setClaimFormStepElementName( String claimFormStepElementName )
  {
    this.claimFormStepElementName = claimFormStepElementName;
  }

  public String getClaimFormStepElementDesc()
  {
    return claimFormStepElementDesc;
  }

  public void setClaimFormStepElementDesc( String claimFormStepElementDesc )
  {
    this.claimFormStepElementDesc = claimFormStepElementDesc;
  }

  public String getClaimFormValue()
  {
    return claimFormValue;
  }

  public void setClaimFormValue( String claimFormValue )
  {
    this.claimFormValue = claimFormValue;
  }

  public Long getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( Long sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public boolean isWhy()
  {
    return why;
  }

  public void setWhy( boolean why )
  {
    this.why = why;
  }
}
