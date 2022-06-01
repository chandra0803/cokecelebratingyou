
package com.biperf.core.value.nomination;

/**
 * Contained within {@link NominationMoreInfoPageValueBean}
 */
public class NominationMoreInfoPageCustomFieldValueBean
{
  private Long sequenceNum;
  private String name;
  private String description;
  private Long claimStepElmtId;
  private boolean whyField;

  public Long getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( Long sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public Long getClaimStepElmtId()
  {
    return claimStepElmtId;
  }

  public void setClaimStepElmtId( Long claimStepElmtId )
  {
    this.claimStepElmtId = claimStepElmtId;
  }

  public boolean isWhyField()
  {
    return whyField;
  }

  public void setWhyField( boolean whyField )
  {
    this.whyField = whyField;
  }

}
