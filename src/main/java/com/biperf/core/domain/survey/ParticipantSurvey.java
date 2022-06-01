
package com.biperf.core.domain.survey;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

public class ParticipantSurvey extends BaseDomain implements Cloneable
{
  private User participant;
  private Date surveyDate;
  private Long surveyId;
  private Long promotionId;
  private boolean completed;
  private Long nodeId;

  private Set participantSurveyResponse = new LinkedHashSet();

  public User getParticipant()
  {
    return participant;
  }

  public void setParticipant( User participant )
  {
    this.participant = participant;
  }

  public Date getSurveyDate()
  {
    return surveyDate;
  }

  public void setSurveyDate( Date surveyDate )
  {
    this.surveyDate = surveyDate;
  }

  public Object clone() throws CloneNotSupportedException
  {
    ParticipantSurvey clonedParticipantSurvey = (ParticipantSurvey)super.clone();
    clonedParticipantSurvey.setParticipant( null );
    clonedParticipantSurvey.setSurveyDate( null );
    clonedParticipantSurvey.setSurveyId( null );
    clonedParticipantSurvey.setPromotionId( null );
    clonedParticipantSurvey.setId( null );
    clonedParticipantSurvey.setVersion( new Long( 0 ) );
    clonedParticipantSurvey.setAuditCreateInfo( new AuditCreateInfo() );
    return clonedParticipantSurvey;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( participant == null ? 0 : participant.hashCode() );
    result = prime * result + ( surveyId == null ? 0 : surveyId.hashCode() );
    result = prime * result + ( promotionId == null ? 0 : promotionId.hashCode() );
    result = prime * result + ( surveyDate == null ? 0 : surveyDate.hashCode() );
    return result;
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
    ParticipantSurvey other = (ParticipantSurvey)obj;
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
    if ( surveyId == null )
    {
      if ( other.surveyId != null )
      {
        return false;
      }
    }
    else if ( !surveyId.equals( other.surveyId ) )
    {
      return false;
    }

    if ( promotionId == null )
    {
      if ( other.promotionId != null )
      {
        return false;
      }
    }
    else if ( !promotionId.equals( other.promotionId ) )
    {
      return false;
    }
    if ( surveyDate == null )
    {
      if ( other.surveyDate != null )
      {
        return false;
      }
    }
    else if ( !surveyDate.equals( other.surveyDate ) )
    {
      return false;
    }
    return true;
  }

  public Long getSurveyId()
  {
    return surveyId;
  }

  public void setSurveyId( Long surveyId )
  {
    this.surveyId = surveyId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Set getParticipantSurveyResponse()
  {
    return participantSurveyResponse;
  }

  public void setParticipantSurveyResponse( Set participantSurveyResponse )
  {
    this.participantSurveyResponse = participantSurveyResponse;
  }

  public void addParticipantSurveyResponse( ParticipantSurveyResponse participantSurveyResponse )
  {
    participantSurveyResponse.setParticipantSurvey( this );
    this.participantSurveyResponse.add( participantSurveyResponse );
  }

  public boolean isCompleted()
  {
    return completed;
  }

  public void setCompleted( boolean completed )
  {
    this.completed = completed;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

}
