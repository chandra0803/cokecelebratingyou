
package com.biperf.core.domain.managertoolkit;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

public class AlertMessage extends BaseDomain
{
  private User submitter;
  private String subject;
  private String message;
  private String messageTo;
  private Date expiryDate;
  private boolean valid;
  private boolean sendEmail;
  private User proxyUser;
  private Long contestId;
  private String ssiAlertType;

  public String getSubject()
  {
    return subject;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getMessageTo()
  {
    return messageTo;
  }

  public void setMessageTo( String messageTo )
  {
    this.messageTo = messageTo;
  }

  public Date getExpiryDate()
  {
    return expiryDate;
  }

  public void setExpiryDate( Date expiryDate )
  {
    this.expiryDate = expiryDate;
  }

  public boolean isValid()
  {
    return valid;
  }

  public void setValid( boolean valid )
  {
    this.valid = valid;
  }

  public void setSubmitter( User submitter )
  {
    this.submitter = submitter;
  }

  public User getSubmitter()
  {
    return submitter;
  }

  public boolean isSendEmail()
  {
    return sendEmail;
  }

  public void setSendEmail( boolean sendEmail )
  {
    this.sendEmail = sendEmail;
  }

  public User getProxyUser()
  {
    return proxyUser;
  }

  public void setProxyUser( User proxyUser )
  {
    this.proxyUser = proxyUser;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public String getSsiAlertType()
  {
    return ssiAlertType;
  }

  public void setSsiAlertType( String ssiAlertType )
  {
    this.ssiAlertType = ssiAlertType;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    else if ( object instanceof AlertMessage )
    {
      AlertMessage alertMessage = (AlertMessage)object;

      if ( getId() != null ? !getId().equals( alertMessage.getId() ) : alertMessage.getId() != null )
      {
        return false;
      }

      return true;
    }
    else
    {
      return false;
    }
  }

  public int hashCode()
  {
    return getId() != null ? getId().hashCode() : 0;
  }

}
