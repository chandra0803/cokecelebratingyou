/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/GoalLevelValueBean.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.util.Date;

/**
 * This bean 'ClaimRecipientValueBean' is created for WIP #43735
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dudam</td>
 * <td>Mar 19, 2018</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimRecipientValueBean implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Long claimItemId;
  private Long participantId;
  private Long nodeId;
  private Boolean optOut;
  private Long cashAwardQty;
  private String cashCurrency;
  private String cashDivision;
  private Boolean cashPaxClaimed;
  private String promotionName;
  private Date submissionDate;
  private Long claimId;
  private Date approvedOn;

  public Long getClaimItemId()
  {
    return claimItemId;
  }

  public void setClaimItemId( Long claimItemId )
  {
    this.claimItemId = claimItemId;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Boolean getOptOut()
  {
    return optOut;
  }

  public void setOptOut( Boolean optOut )
  {
    this.optOut = optOut;
  }

  public Long getCashAwardQty()
  {
    return cashAwardQty;
  }

  public void setCashAwardQty( Long cashAwardQty )
  {
    this.cashAwardQty = cashAwardQty;
  }

  public String getCashCurrency()
  {
    return cashCurrency;
  }

  public void setCashCurrency( String cashCurrency )
  {
    this.cashCurrency = cashCurrency;
  }

  public String getCashDivision()
  {
    return cashDivision;
  }

  public void setCashDivision( String cashDivision )
  {
    this.cashDivision = cashDivision;
  }

  public Boolean getCashPaxClaimed()
  {
    return cashPaxClaimed;
  }

  public void setCashPaxClaimed( Boolean cashPaxClaimed )
  {
    this.cashPaxClaimed = cashPaxClaimed;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Date getApprovedOn()
  {
    return approvedOn;
  }

  public void setApprovedOn( Date approvedOn )
  {
    this.approvedOn = approvedOn;
  }

}
