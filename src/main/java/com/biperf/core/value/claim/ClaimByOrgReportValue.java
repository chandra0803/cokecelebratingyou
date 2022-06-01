/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/claim/ClaimByOrgReportValue.java,v $
 */

package com.biperf.core.value.claim;

import java.math.BigDecimal;

/**
 * ClaimByOrgReportValue.
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
 * <td>Aug 23, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ClaimByOrgReportValue
{
  // TABULAR DATA
  private Long orgId;
  private String orgName;
  private Long eligSubmitters;
  private Long actualSubmitters;
  private BigDecimal participationRate;
  private Long totalClaims;
  private Long openClaims;
  private Long closedClaims;
  private Long approvedItems;
  private Long deniedItems;
  private Long holdItems;
  private Long points;
  private Boolean isLeaf;
  private Long sweepstakesWon;
  private Long badgesEarned;

  // ITEM STATUS CHART
  private Long itemsApproved;
  private Long itemsDenied;
  private Long itemsHeld;

  // MONTHLY CHART
  private String monthName;
  private Long claimCount;

  // PARTICIPATION RATE CHART
  private Long submitterCount;
  private Long nonSubmitterCount;

  // SUBMISSION STATUS CHART
  private Long openStatusCount;
  private Long closedStatusCount;

  // TOTALS CHART
  private BigDecimal claimsPerSubmitter;
  private BigDecimal itemsPerSubmitter;

  public void setOrgId( Long orgId )
  {
    this.orgId = orgId;
  }

  public Long getOrgId()
  {
    return orgId;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public Long getEligSubmitters()
  {
    return eligSubmitters;
  }

  public void setEligSubmitters( Long eligSubmitters )
  {
    this.eligSubmitters = eligSubmitters;
  }

  public Long getActualSubmitters()
  {
    return actualSubmitters;
  }

  public void setActualSubmitters( Long actualSubmitters )
  {
    this.actualSubmitters = actualSubmitters;
  }

  public BigDecimal getParticipationRate()
  {
    return participationRate;
  }

  public void setParticipationRate( BigDecimal participationRate )
  {
    this.participationRate = participationRate;
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

  public Long getHoldItems()
  {
    return holdItems;
  }

  public void setHoldItems( Long holdItems )
  {
    this.holdItems = holdItems;
  }

  public Long getPoints()
  {
    return points;
  }

  public void setPoints( Long points )
  {
    this.points = points;
  }

  public void setIsLeaf( Boolean isLeaf )
  {
    this.isLeaf = isLeaf;
  }

  public Boolean getIsLeaf()
  {
    return isLeaf;
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

  public Long getItemsApproved()
  {
    return itemsApproved;
  }

  public void setItemsApproved( Long itemsApproved )
  {
    this.itemsApproved = itemsApproved;
  }

  public Long getItemsDenied()
  {
    return itemsDenied;
  }

  public void setItemsDenied( Long itemsDenied )
  {
    this.itemsDenied = itemsDenied;
  }

  public Long getItemsHeld()
  {
    return itemsHeld;
  }

  public void setItemsHeld( Long itemsHeld )
  {
    this.itemsHeld = itemsHeld;
  }

  public String getMonthName()
  {
    return monthName;
  }

  public void setMonthName( String monthName )
  {
    this.monthName = monthName;
  }

  public Long getClaimCount()
  {
    return claimCount;
  }

  public void setClaimCount( Long claimCount )
  {
    this.claimCount = claimCount;
  }

  public Long getSubmitterCount()
  {
    return submitterCount;
  }

  public void setSubmitterCount( Long submitterCount )
  {
    this.submitterCount = submitterCount;
  }

  public Long getNonSubmitterCount()
  {
    return nonSubmitterCount;
  }

  public void setNonSubmitterCount( Long nonSubmitterCount )
  {
    this.nonSubmitterCount = nonSubmitterCount;
  }

  public Long getOpenStatusCount()
  {
    return openStatusCount;
  }

  public void setOpenStatusCount( Long openStatusCount )
  {
    this.openStatusCount = openStatusCount;
  }

  public Long getClosedStatusCount()
  {
    return closedStatusCount;
  }

  public void setClosedStatusCount( Long closedStatusCount )
  {
    this.closedStatusCount = closedStatusCount;
  }

  public BigDecimal getClaimsPerSubmitter()
  {
    return claimsPerSubmitter;
  }

  public void setClaimsPerSubmitter( BigDecimal claimsPerSubmitter )
  {
    this.claimsPerSubmitter = claimsPerSubmitter;
  }

  public BigDecimal getItemsPerSubmitter()
  {
    return itemsPerSubmitter;
  }

  public void setItemsPerSubmitter( BigDecimal itemsPerSubmitter )
  {
    this.itemsPerSubmitter = itemsPerSubmitter;
  }

}
