
package com.biperf.core.domain.client;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.biperf.core.domain.BaseDomain;

public class ClientCareerMoments extends BaseDomain
{
  private static final long serialVersionUID = -8509982046118488021L;
  
  private Long userId;
  private Date enrollmentDate;
  private Date jobChangeDate;
  private boolean termsAccepted;
  private Date dateTermsAccepted;
  private boolean inviteEmailSent;
  private String jobChangeValue;
  private String moveType;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Date getEnrollmentDate()
  {
    return enrollmentDate;
  }

  public void setEnrollmentDate( Date enrollmentDate )
  {
    this.enrollmentDate = enrollmentDate;
  }

  public Date getJobChangeDate()
  {
    return jobChangeDate;
  }

  public void setJobChangeDate( Date jobChangeDate )
  {
    this.jobChangeDate = jobChangeDate;
  }

  public boolean isTermsAccepted()
  {
    return termsAccepted;
  }

  public void setTermsAccepted( boolean termsAccepted )
  {
    this.termsAccepted = termsAccepted;
  }

  public Date getDateTermsAccepted()
  {
    return dateTermsAccepted;
  }

  public void setDateTermsAccepted( Date dateTermsAccepted )
  {
    this.dateTermsAccepted = dateTermsAccepted;
  }

  public boolean isInviteEmailSent()
  {
    return inviteEmailSent;
  }

  public void setInviteEmailSent( boolean inviteEmailSent )
  {
    this.inviteEmailSent = inviteEmailSent;
  }

  public String getJobChangeValue()
  {
    return jobChangeValue;
  }

  public void setJobChangeValue( String jobChangeValue )
  {
    this.jobChangeValue = jobChangeValue;
  }

  public String getMoveType()
  {
    return moveType;
  }

  public void setMoveType( String moveType )
  {
    this.moveType = moveType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return Objects.hash( dateTermsAccepted, enrollmentDate, inviteEmailSent, jobChangeDate, jobChangeValue, moveType, termsAccepted, userId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
      return true;
    if ( ! ( obj instanceof ClientCareerMoments ) )
      return false;
    ClientCareerMoments other = (ClientCareerMoments)obj;
    return Objects.equals( dateTermsAccepted, other.dateTermsAccepted ) && Objects.equals( enrollmentDate, other.enrollmentDate ) && inviteEmailSent == other.inviteEmailSent
        && Objects.equals( jobChangeDate, other.jobChangeDate ) && Objects.equals( jobChangeValue, other.jobChangeValue ) && Objects.equals( moveType, other.moveType )
        && termsAccepted == other.termsAccepted && Objects.equals( userId, other.userId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "ClientCareerMoments [userId=" + userId + ", enrollmentDate=" + enrollmentDate + ", jobChangeDate=" + jobChangeDate + ", termsAccepted=" + termsAccepted + ", dateTermsAccepted="
        + dateTermsAccepted + ", inviteEmailSent=" + inviteEmailSent + ", jobChangeValue=" + jobChangeValue + ", moveType=" + moveType + "]";
  }

}
