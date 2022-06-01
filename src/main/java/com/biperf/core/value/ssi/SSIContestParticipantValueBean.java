
package com.biperf.core.value.ssi;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class SSIContestParticipantValueBean
{
  private String firstName;
  private String lastName;
  private Long id;
  private Long userId;
  private String orgName;
  private String orgType;
  private String departmentName;
  private String jobName;
  private String countryName;
  private String countryCode;
  private String activityDescription;
  private String objectiveAmount;
  private String objectivePayoutDescription;
  private String objectivePayout;
  private String bonusForEvery;
  private String bonusPayout;
  private String bonusPayoutCap;
  private String baselineAmount;

  // for downloading contest data
  private String loginId;
  private String emailAddress;
  private String progressTotal;
  private String contestId;
  private Date activityDate;

  private String role;
  private String otherPayoutDescription;
  private String otherValue;
  private String activityAmount;
  private String payoutPoints;
  private String payoutDescription;
  private String value;

  public String getRole()
  {
    return role;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

  public String getOtherPayoutDescription()
  {
    return otherPayoutDescription;
  }

  public void setOtherPayoutDescription( String otherPayoutDescription )
  {
    this.otherPayoutDescription = otherPayoutDescription;
  }

  public String getOtherValue()
  {
    return otherValue;
  }

  public void setOtherValue( String otherValue )
  {
    this.otherValue = otherValue;
  }

  public String getActivityAmount()
  {
    return activityAmount;
  }

  public void setActivityAmount( String activityAmount )
  {
    this.activityAmount = activityAmount;
  }

  public String getPayoutPoints()
  {
    return payoutPoints;
  }

  public void setPayoutPoints( String payoutPoints )
  {
    this.payoutPoints = payoutPoints;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public Date getActivityDate()
  {
    return activityDate;
  }

  public void setActivityDate( Date activityDate )
  {
    this.activityDate = activityDate;
  }

  public String getContestId()
  {
    return contestId;
  }

  public void setContestId( String contestId )
  {
    this.contestId = contestId;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getOrgType()
  {
    return orgType;
  }

  public void setOrgType( String orgType )
  {
    this.orgType = orgType;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String jobName )
  {
    this.jobName = jobName;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public String getObjectivePayoutDescription()
  {
    return objectivePayoutDescription;
  }

  public void setObjectivePayoutDescription( String objectivePayoutDescription )
  {
    this.objectivePayoutDescription = objectivePayoutDescription;
  }

  public String getObjectiveAmount()
  {
    return objectiveAmount;
  }

  public void setObjectiveAmount( String objectiveAmount )
  {
    this.objectiveAmount = objectiveAmount;
  }

  public String getObjectivePayout()
  {
    return objectivePayout;
  }

  public void setObjectivePayout( String objectivePayout )
  {
    this.objectivePayout = objectivePayout;
  }

  public String getBonusForEvery()
  {
    return bonusForEvery;
  }

  public void setBonusForEvery( String bonusForEvery )
  {
    this.bonusForEvery = bonusForEvery;
  }

  public String getBonusPayout()
  {
    return bonusPayout;
  }

  public void setBonusPayout( String bonusPayout )
  {
    this.bonusPayout = bonusPayout;
  }

  public String getBonusPayoutCap()
  {
    return bonusPayoutCap;
  }

  public void setBonusPayoutCap( String bonusPayoutCap )
  {
    this.bonusPayoutCap = bonusPayoutCap;
  }

  public String getBaselineAmount()
  {
    return baselineAmount;
  }

  public void setBaselineAmount( String baselineAmount )
  {
    this.baselineAmount = baselineAmount;
  }

  public String getLoginId()
  {
    return loginId;
  }

  public void setLoginId( String loginId )
  {
    this.loginId = loginId;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getProgressTotal()
  {
    return progressTotal;
  }

  public void setProgressTotal( String progressTotal )
  {
    this.progressTotal = progressTotal;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

}
