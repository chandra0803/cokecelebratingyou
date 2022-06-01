
package com.biperf.core.domain.survey;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;

public class ParticipantSurveyResponse extends BaseDomain implements Cloneable
{

  private int sequenceNum;
  private SurveyQuestionResponse surveyQuestionResponse;
  private SurveyQuestion surveyQuestion;
  private ParticipantSurvey participantSurvey;
  private String openEndedResponse;
  private Double sliderResponse;

  public ParticipantSurveyResponse()
  {
    super();
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public SurveyQuestionResponse getSurveyQuestionResponse()
  {
    return surveyQuestionResponse;
  }

  public void setSurveyQuestionResponse( SurveyQuestionResponse surveyQuestionResponse )
  {
    this.surveyQuestionResponse = surveyQuestionResponse;
  }

  public SurveyQuestion getSurveyQuestion()
  {
    return surveyQuestion;
  }

  public void setSurveyQuestion( SurveyQuestion surveyQuestion )
  {
    this.surveyQuestion = surveyQuestion;
  }

  public ParticipantSurvey getParticipantSurvey()
  {
    return participantSurvey;
  }

  public void setParticipantSurvey( ParticipantSurvey participantSurvey )
  {
    this.participantSurvey = participantSurvey;
  }

  public String getOpenEndedResponse()
  {
    return openEndedResponse;
  }

  public void setOpenEndedResponse( String openEndedResponse )
  {
    this.openEndedResponse = openEndedResponse;
  }

  public Object clone() throws CloneNotSupportedException
  {
    ParticipantSurveyResponse clonedpParticipantSurveyResponse = (ParticipantSurveyResponse)super.clone();
    clonedpParticipantSurveyResponse.setSequenceNum( 0 );
    clonedpParticipantSurveyResponse.setSurveyQuestion( null );
    clonedpParticipantSurveyResponse.setSurveyQuestionResponse( null );
    clonedpParticipantSurveyResponse.setParticipantSurvey( null );
    clonedpParticipantSurveyResponse.setId( null );
    clonedpParticipantSurveyResponse.setVersion( new Long( 0 ) );
    clonedpParticipantSurveyResponse.setAuditCreateInfo( new AuditCreateInfo() );
    return clonedpParticipantSurveyResponse;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( participantSurvey == null ? 0 : participantSurvey.hashCode() );
    result = prime * result + sequenceNum;
    result = prime * result + ( surveyQuestion == null ? 0 : surveyQuestion.hashCode() );
    /*
     * result = prime result + ((surveyQuestionResponse == null) ? 0 : surveyQuestionResponse
     * .hashCode());
     */
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
    ParticipantSurveyResponse other = (ParticipantSurveyResponse)obj;
    if ( participantSurvey == null )
    {
      if ( other.participantSurvey != null )
      {
        return false;
      }
    }
    else if ( !participantSurvey.equals( other.participantSurvey ) )
    {
      return false;
    }
    if ( sequenceNum != other.sequenceNum )
    {
      return false;
    }
    if ( surveyQuestion == null )
    {
      if ( other.surveyQuestion != null )
      {
        return false;
      }
    }
    else if ( !surveyQuestion.equals( other.surveyQuestion ) )
    {
      return false;
    }
    /*
     * if (surveyQuestionResponse == null) { if (other.surveyQuestionResponse != null) return false;
     * } else if (!surveyQuestionResponse.equals(other.surveyQuestionResponse)) return false;
     */
    return true;
  }

  public Double getSliderResponse()
  {
    return sliderResponse;
  }

  public void setSliderResponse( Double sliderResponse )
  {
    this.sliderResponse = sliderResponse;
  }

}
