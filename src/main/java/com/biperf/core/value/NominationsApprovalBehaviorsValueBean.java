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
public class NominationsApprovalBehaviorsValueBean
{
  private Long claimId;
  private String behaviorName;
  private String badgeName;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getBehaviorName()
  {
    return behaviorName;
  }

  public void setBehaviorName( String behaviorName )
  {
    this.behaviorName = behaviorName;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

}
