/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/claim/ClaimByPaxReportValue.java,v $
 */

package com.biperf.core.value.claim;

import java.util.Date;

/**
 * ClaimByPaxReportValue.
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
 * <td>drahn</td>
 * <td>Aug 24, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ClaimByPaxReportValue
{
  // TABULAR DATA
  private Long paxId;
  private String paxName;
  private String country;
  private String orgName;
  private String department;
  private String jobPosition;
  private String promotionName;
  private Long totalClaims;
  private Long openClaims;
  private Long closedClaims;
  private Long approvedItems;
  private Long deniedItems;
  private Long heldItems;
  private Long points;
  private Long sweepstakesWon;
  private Long badgesEarned;

  // DETAIL TABULAR DATA
  private String claimNumber;
  private String claimStatus;
  private Date dateSubmitted;
  private Long claimId;
  private Long submitterUserId;
  private Long promotionId;

  public void setPaxId( Long paxId )
  {
    this.paxId = paxId;
  }

  public Long getPaxId()
  {
    return paxId;
  }

  public String getPaxName()
  {
    return paxName;
  }

  public void setPaxName( String paxName )
  {
    this.paxName = paxName;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getJobPosition()
  {
    return jobPosition;
  }

  public void setJobPosition( String jobPosition )
  {
    this.jobPosition = jobPosition;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Long getTotalClaims()
  {
    return totalClaims;
  }

  public void setTotalClaims( Long totalClaims )
  {
    this.totalClaims = totalClaims;
  }

  public Long getOpenClaims()
  {
    return openClaims;
  }

  public void setOpenClaims( Long openClaims )
  {
    this.openClaims = openClaims;
  }

  public Long getClosedClaims()
  {
    return closedClaims;
  }

  public void setClosedClaims( Long closedClaims )
  {
    this.closedClaims = closedClaims;
  }

  public Long getApprovedItems()
  {
    return approvedItems;
  }

  public void setApprovedItems( Long approvedItems )
  {
    this.approvedItems = approvedItems;
  }

  public Long getDeniedItems()
  {
    return deniedItems;
  }

  public void setDeniedItems( Long deniedItems )
  {
    this.deniedItems = deniedItems;
  }

  public Long getHeldItems()
  {
    return heldItems;
  }

  public void setHeldItems( Long heldItems )
  {
    this.heldItems = heldItems;
  }

  public Long getPoints()
  {
    return points;
  }

  public void setPoints( Long points )
  {
    this.points = points;
  }

  public Long getSweepstakesWon()
  {
    return sweepstakesWon;
  }

  public void setSweepstakesWon( Long sweepstakesWon )
  {
    this.sweepstakesWon = sweepstakesWon;
  }

  public Long getBadgesEarned()
  {
    return badgesEarned;
  }

  public void setBadgesEarned( Long badgesEarned )
  {
    this.badgesEarned = badgesEarned;
  }

  public String getClaimNumber()
  {
    return claimNumber;
  }

  public void setClaimNumber( String claimNumber )
  {
    this.claimNumber = claimNumber;
  }

  public String getClaimStatus()
  {
    return claimStatus;
  }

  public void setClaimStatus( String claimStatus )
  {
    this.claimStatus = claimStatus;
  }

  public Date getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( Date dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getSubmitterUserId()
  {
    return submitterUserId;
  }

  public void setSubmitterUserId( Long submitterUserId )
  {
    this.submitterUserId = submitterUserId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

}
