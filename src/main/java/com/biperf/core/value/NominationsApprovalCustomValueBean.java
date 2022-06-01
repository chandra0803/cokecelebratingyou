/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

/**
 * 
 * @author poddutur
 * @since May 17, 2016
 */
public class NominationsApprovalCustomValueBean
{
  private Long claimId;
  private Long nomineePaxId;
  private Long claimFormStepElementId;
  private String claimFormStepElementName;
  private String description;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getNomineePaxId()
  {
    return nomineePaxId;
  }

  public void setNomineePaxId( Long nomineePaxId )
  {
    this.nomineePaxId = nomineePaxId;
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

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

}
