
package com.biperf.core.service.survey;

import java.util.Iterator;

import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.service.BaseAssociationRequest;

public class SurveyAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Hyrate Level: SURVEY_QUESTION
   */
  public static final int SURVEY_QUESTION = 2;

  /**
   * Hyrate Level: SURVEY_QUESTION_RESPONSE
   */
  public static final int SURVEY_QUESTION_RESPONSE = 3;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public SurveyAssociationRequest( int hydrateLevel )
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
    Survey survey = (Survey)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateSurveyQuestions( survey );
        hydrateSurveyQuestionResponses( survey );
        break;

      case SURVEY_QUESTION:
        hydrateSurveyQuestions( survey );
        break;

      case SURVEY_QUESTION_RESPONSE:
        hydrateSurveyQuestions( survey );
        hydrateSurveyQuestionResponses( survey );
        break;

      default:
        break;
    } // switch
  }

  /**
   * Loads the survey question list attached on this survey
   * 
   * @param survey
   */
  private void hydrateSurveyQuestions( Survey survey )
  {
    initialize( survey.getSurveyQuestions() );
  }

  /**
   * Hydrate the survey question response list on each survey question in the list attached to this survey.
   * 
   * @param survey
   */
  private void hydrateSurveyQuestionResponses( Survey survey )
  {
    if ( survey.getSurveyQuestions() != null )
    {
      for ( Iterator iter = survey.getSurveyQuestions().iterator(); iter.hasNext(); )
      {
        SurveyQuestion question = (SurveyQuestion)iter.next();
        if ( question != null )
        {
          initialize( question.getSurveyQuestionResponses() );
        }
      }
    }
  }
}
