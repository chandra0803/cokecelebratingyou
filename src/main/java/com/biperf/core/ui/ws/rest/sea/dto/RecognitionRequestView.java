
package com.biperf.core.ui.ws.rest.sea.dto;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.biperf.core.security.ws.rest.SecureToken;

@XmlRootElement
public class RecognitionRequestView extends BaseRestObject implements SecureToken
{
  private String senderEmail;
  private Set<String> recipientEmails = new HashSet<String>();;
  private String initialComments;
  private String latestComments;
  private HashTag behaviorHashTag = new HashTag();
  private int points;
  
  private String messageStatus ;

  public String getSenderEmail()
  {
    return senderEmail;
  }

  public void setSenderEmail( String senderEmail )
  {
    this.senderEmail = senderEmail;
  }

  public Set<String> getRecipientEmails()
  {
    return recipientEmails;
  }

  public void setRecipientEmails( Set<String> recipientEmails )
  {
    this.recipientEmails = recipientEmails;
  }

  public String getInitialComments()
  {
    return initialComments;
  }

  public void setInitialComments( String initialComments )
  {
    this.initialComments = initialComments;
  }

  public String getLatestComments()
  {
    return latestComments;
  }

  public void setLatestComments( String latestComments )
  {
    this.latestComments = latestComments;
  }

  public HashTag getBehaviorHashTag()
  {
    return behaviorHashTag;
  }

  public void setBehaviorHashTag( HashTag behaviorHashTag )
  {
    this.behaviorHashTag = behaviorHashTag;
  }

  public int getPoints()
  {
    return points;
  }

  public void setPoints( int points )
  {
    this.points = points;
  }

  @Override
  public String getHashableAttribute()
  {
    return this.getSenderEmail();
  }
  
  public String getMessageStatus()
  {
    return messageStatus;
  }

  public void setMessageStatus( String messageStatus )
  {
    this.messageStatus = messageStatus;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( behaviorHashTag == null ) ? 0 : behaviorHashTag.hashCode() );
    result = prime * result + points;
    result = prime * result + ( ( recipientEmails == null ) ? 0 : recipientEmails.hashCode() );
    result = prime * result + ( ( initialComments == null ) ? 0 : initialComments.hashCode() );
    result = prime * result + ( ( latestComments == null ) ? 0 : latestComments.hashCode() );
    result = prime * result + ( ( senderEmail == null ) ? 0 : senderEmail.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
      return true;
    if ( obj == null )
      return false;
    if ( getClass() != obj.getClass() )
      return false;
    RecognitionRequestView other = (RecognitionRequestView)obj;
    if ( behaviorHashTag == null )
    {
      if ( other.behaviorHashTag != null )
        return false;
    }
    else if ( !behaviorHashTag.equals( other.behaviorHashTag ) )
      return false;
    if ( points != other.points )
      return false;
    if ( recipientEmails == null )
    {
      if ( other.recipientEmails != null )
        return false;
    }
    else if ( !recipientEmails.equals( other.recipientEmails ) )
      return false;
    if ( initialComments == null )
    {
      if ( other.initialComments != null )
        return false;
    }
    else if ( !initialComments.equals( other.initialComments ) )
      return false;
    if ( latestComments == null )
    {
      if ( other.latestComments != null )
        return false;
    }
    else if ( !latestComments.equals( other.latestComments ) )
      return false;
    if ( senderEmail == null )
    {
      if ( other.senderEmail != null )
        return false;
    }
    else if ( !senderEmail.equals( other.senderEmail ) )
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "RecognitionRequestView [senderEmail=" + senderEmail + ", recipientEmails=" + recipientEmails + ", intialComments=" + initialComments + ", latestComments=" + latestComments
        + ", behaviorHashTag=" + behaviorHashTag + ", points=" + points + "]";
  }

}
