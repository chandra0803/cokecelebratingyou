
package com.biperf.core.value.nomination;

public class NominationCertificateCustomValueBean
{
  private Long claimStepElmtId;
  private String name;
  private String claimFormValue;
  private Long seqNumber;
  private boolean whyField;

  public Long getClaimStepElmtId()
  {
    return claimStepElmtId;
  }

  public void setClaimStepElmtId( Long claimStepElmtId )
  {
    this.claimStepElmtId = claimStepElmtId;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getClaimFormValue()
  {
    return claimFormValue;
  }

  public void setClaimFormValue( String claimFormValue )
  {
    this.claimFormValue = claimFormValue;
  }

  public Long getSeqNumber()
  {
    return seqNumber;
  }

  public void setSeqNumber( Long seqNumber )
  {
    this.seqNumber = seqNumber;
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
