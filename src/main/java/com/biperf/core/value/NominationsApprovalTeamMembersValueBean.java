/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.math.BigDecimal;

/**
 * 
 * @author poddutur
 * @since May 17, 2016
 */
public class NominationsApprovalTeamMembersValueBean
{
  private Long claimId;
  private String userName;
  private String countryName;
  private String orgName;
  private String jobPositionName;
  private BigDecimal awardAmount;
  private Long userId;
  private String departmentName;
  private boolean optOutAwards;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getJobPositionName()
  {
    return jobPositionName;
  }

  public void setJobPositionName( String jobPositionName )
  {
    this.jobPositionName = jobPositionName;
  }

  public BigDecimal getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( BigDecimal awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public boolean isOptOutAwards()
  {
    return optOutAwards;
  }

  public void setOptOutAwards( boolean optOutAwards )
  {
    this.optOutAwards = optOutAwards;
  }

}
