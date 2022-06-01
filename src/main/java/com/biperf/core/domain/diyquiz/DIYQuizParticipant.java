
package com.biperf.core.domain.diyquiz;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.quiz.Quiz;

public class DIYQuizParticipant extends BaseDomain
{
  private static final long serialVersionUID = 1L;
  private Quiz quiz;
  private Participant participant;
  private String statusType;
  private boolean isNotificationSent;

  public Quiz getQuiz()
  {
    return quiz;
  }

  public void setQuiz( Quiz quiz )
  {
    this.quiz = quiz;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public String getStatusType()
  {
    return statusType;
  }

  public void setStatusType( String statusType )
  {
    this.statusType = statusType;
  }

  public boolean getIsNotificationSent()
  {
    return isNotificationSent;
  }

  public void setIsNotificationSent( boolean isNotificationSent )
  {
    this.isNotificationSent = isNotificationSent;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    DIYQuizParticipant other = (DIYQuizParticipant)obj;
    if ( quiz == null )
    {
      if ( other.quiz != null )
      {
        return false;
      }
    }
    else if ( !quiz.equals( other.quiz ) )
    {
      return false;
    }
    if ( participant == null )
    {
      if ( other.participant != null )
      {
        return false;
      }
    }
    else if ( !participant.equals( other.participant ) )
    {
      return false;
    }
    if ( statusType == null )
    {
      if ( other.statusType != null )
      {
        return false;
      }
    }
    else if ( !statusType.equals( other.statusType ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( quiz == null ? 0 : quiz.hashCode() );
    result = prime * result + ( participant == null ? 0 : participant.hashCode() );
    result = prime * result + ( statusType == null ? 0 : statusType.hashCode() );
    return result;
  }

}
