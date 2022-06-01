
package com.biperf.core.service.survey;

import java.util.Iterator;

import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.domain.survey.ParticipantSurveyResponse;
import com.biperf.core.service.BaseAssociationRequest;

public class ParticipantSurveyAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;
  public static final int SURVEY_RESPONSE_QUESTIONS = 2;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public ParticipantSurveyAssociationRequest( int hydrateLevel )
  {
    super();
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    ParticipantSurvey participantSurvey = (ParticipantSurvey)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateParticipantSurveyResponses( participantSurvey );
        break;
      case SURVEY_RESPONSE_QUESTIONS:
        hydrateParticipantSurveyResponses( participantSurvey );
        hydrateParticipantSurveyResponseQuestions( participantSurvey );
        break;

      default:
        break;
    } // switch
  }

  /**
   * Hydrate the survey question response list on each survey question
   * 
   * @param surveyQuestion
   */
  private void hydrateParticipantSurveyResponses( ParticipantSurvey participantSurvey )
  {

    if ( participantSurvey.getParticipantSurveyResponse() != null )
    {
      for ( Iterator iter = participantSurvey.getParticipantSurveyResponse().iterator(); iter.hasNext(); )
      {
        ParticipantSurveyResponse surveyResponse = (ParticipantSurveyResponse)iter.next();
        if ( surveyResponse != null )
        {
          initialize( surveyResponse );
        }
      }
    }
  }

  private void hydrateParticipantSurveyResponseQuestions( ParticipantSurvey participantSurvey )
  {
    if ( participantSurvey.getParticipantSurveyResponse() != null )
    {
      for ( Iterator iter = participantSurvey.getParticipantSurveyResponse().iterator(); iter.hasNext(); )
      {
        ParticipantSurveyResponse surveyResponse = (ParticipantSurveyResponse)iter.next();
        if ( surveyResponse != null )
        {
          for ( Iterator iter1 = surveyResponse.getSurveyQuestion().getSurveyQuestionResponses().iterator(); iter1.hasNext(); )
          {
            SurveyQuestionResponse surveyQuestionResponse = (SurveyQuestionResponse)iter1.next();
            if ( surveyQuestionResponse != null )
            {
              initialize( surveyQuestionResponse );
            }
          }
        }
      }
    }
  }
}
