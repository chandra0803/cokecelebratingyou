/**
 * 
 */

package com.biperf.core.value.instantpoll;

import java.util.Date;

import com.biperf.core.utils.DateUtils;

/**
 * @author poddutur
 *
 */
public class InstantPollsListbean
{
  private Long instantPollId;
  private String question;
  private Date submissionStartDate;
  private Date submissionEndDate;
  private String audienceType;
  private String status;
  private Boolean notifyPax;
  private Boolean isEmailAlreadySent;

  public String getQuestion()
  {
    return question;
  }

  public void setQuestion( String question )
  {
    this.question = question;
  }

  public Long getInstantPollId()
  {
    return instantPollId;
  }

  public void setInstantPollId( Long instantPollId )
  {
    this.instantPollId = instantPollId;
  }

  public Date getSubmissionStartDate()
  {
    return submissionStartDate;
  }

  public void setSubmissionStartDate( Date submissionStartDate )
  {
    this.submissionStartDate = submissionStartDate;
  }

  public Date getSubmissionEndDate()
  {
    return submissionEndDate;
  }

  public void setSubmissionEndDate( Date submissionEndDate )
  {
    this.submissionEndDate = submissionEndDate;
  }

  /**
   * @return submissionStartDate in String
   */
  public String getDisplayStartDate()
  {
    return DateUtils.toDisplayString( this.getSubmissionStartDate() );
  }

  /**
   * @return submissionEndDate in String
   */
  public String getDisplayEndDate()
  {
    return DateUtils.toDisplayString( this.getSubmissionEndDate() );
  }

  public String getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( String audienceType )
  {
    this.audienceType = audienceType;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public Boolean getNotifyPax()
  {
    return notifyPax;
  }

  public void setNotifyPax( Boolean notifyPax )
  {
    this.notifyPax = notifyPax;
  }

  public Boolean getIsEmailAlreadySent()
  {
    return isEmailAlreadySent;
  }

  public void setIsEmailAlreadySent( Boolean isEmailAlreadySent )
  {
    this.isEmailAlreadySent = isEmailAlreadySent;
  }
}
