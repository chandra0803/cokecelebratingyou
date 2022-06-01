/**
 * 
 */

package com.biperf.core.domain.promotion;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author poddutur
 *
 */
public class InstantPoll extends Survey
{

  /**
   * 
   */
  private static final long serialVersionUID = -1715870722208486802L;

  private Date submissionStartDate;
  private Date submissionEndDate;
  private String audienceType;
  private String status;
  private Boolean notifyPax;
  private Boolean isEmailAlreadySent;
  private Set<InstantPollAudience> audience = new HashSet<InstantPollAudience>();

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

  public String getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( String audienceType )
  {
    this.audienceType = audienceType;
  }

  public Set<InstantPollAudience> getAudience()
  {
    return audience;
  }

  public void setAudience( Set<InstantPollAudience> audience )
  {
    this.audience = audience;
  }

  public void addAudience( InstantPollAudience instantPollAudience )
  {
    instantPollAudience.setInstantPoll( this );
    this.audience.add( instantPollAudience );
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

  public String getQuestion()
  {
    String question = "";
    for ( Iterator iter = getActiveQuestions().iterator(); iter.hasNext(); )
    {
      SurveyQuestion surveyQuestion = (SurveyQuestion)iter.next();
      question = surveyQuestion.getQuestionText();
    }

    return question;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ( super.getId() == null ? 0 : super.getId().hashCode() );

    return result;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    final InstantPoll instantPoll = (InstantPoll)object;

    if ( getId() != null ? !getId().equals( instantPoll.getId() ) : instantPoll.getId() != null )
    {
      return false;
    }
    return true;

  }

}
