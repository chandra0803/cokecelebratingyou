/**
 * 
 */

package com.biperf.core.value.serviceanniversary;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.biperf.core.domain.user.User;

public class SAValueBean
{

  private static final int ACCESS_CUTOFF_BEFORE_AWARD_DATE = 1;

  private String celebrationId;
  private String programName;
  private String programCmxCode;
  private Long recipientId;
  private Date awardDate;
  private User recipient;
  private Long inviteCount = 0L;
  private Long viewCount = 0L;
  private String contributionStatus;
  private Date inviteSendDate;

  public SAValueBean()
  {

  }

  public SAValueBean( String celebrationId, String programName, String programCmxCode, Long recipientId, Date awardDate )
  {
    super();
    this.celebrationId = celebrationId;
    this.programName = programName;
    this.programCmxCode = programCmxCode;
    this.recipientId = recipientId;
    this.awardDate = awardDate;
  }

  public String getCelebrationId()
  {
    return celebrationId;
  }

  public void setCelebrationId( String celebrationId )
  {
    this.celebrationId = celebrationId;
  }

  public String getProgramName()
  {
    return programName;
  }

  public void setProgramName( String programName )
  {
    this.programName = programName;
  }

  public String getProgramCmxCode()
  {
    return programCmxCode;
  }

  public void setProgramCmxCode( String programCmxCode )
  {
    this.programCmxCode = programCmxCode;
  }

  public Date getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

  public Long getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public User getRecipient()
  {
    return recipient;
  }

  public void setRecipient( User recipient )
  {
    this.recipient = recipient;
  }

  public Long getInviteCount()
  {
    return inviteCount;
  }

  public void setInviteCount( Long inviteCount )
  {
    this.inviteCount = inviteCount;
  }

  public Long getViewCount()
  {
    return viewCount;
  }

  public void setViewCount( Long viewCount )
  {
    this.viewCount = viewCount;
  }

  public String getContributionStatus()
  {
    return contributionStatus;
  }

  public void setContributionStatus( String contributionStatus )
  {
    this.contributionStatus = contributionStatus;
  }

  public Date getInviteSendDate()
  {
    return inviteSendDate;
  }

  public void setInviteSendDate( Date inviteSendDate )
  {
    this.inviteSendDate = inviteSendDate;
  }

  public Date getCloseDate()
  {
    Date closeDate = new Date();
    closeDate.setTime( awardDate.getTime() - ACCESS_CUTOFF_BEFORE_AWARD_DATE * DateUtils.MILLIS_PER_DAY );
    return com.biperf.core.utils.DateUtils.toEndDate( closeDate );
  }

}
