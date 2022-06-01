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
public class NominationsApprovalTimePeriodsValueBean
{
  private Long claimId;
  private Long timePeriodId;
  private String timePeriodName;
  private Long maxWinsllowed;
  private Long noOfWinnners;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getTimePeriodId()
  {
    return timePeriodId;
  }

  public void setTimePeriodId( Long timePeriodId )
  {
    this.timePeriodId = timePeriodId;
  }

  public String getTimePeriodName()
  {
    return timePeriodName;
  }

  public void setTimePeriodName( String timePeriodName )
  {
    this.timePeriodName = timePeriodName;
  }

  public Long getMaxWinsllowed()
  {
    return maxWinsllowed;
  }

  public void setMaxWinsllowed( Long maxWinsllowed )
  {
    this.maxWinsllowed = maxWinsllowed;
  }

  public Long getNoOfWinnners()
  {
    return noOfWinnners;
  }

  public void setNoOfWinnners( Long noOfWinnners )
  {
    this.noOfWinnners = noOfWinnners;
  }

}
