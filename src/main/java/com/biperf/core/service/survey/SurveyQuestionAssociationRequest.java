
package com.biperf.core.service.survey;

import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.service.BaseAssociationRequest;

public class SurveyQuestionAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public SurveyQuestionAssociationRequest( int hydrateLevel )
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
    SurveyQuestion surveyQuestion = (SurveyQuestion)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateSurveyQuestionResponses( surveyQuestion );
        hydrateSurveyQuestionSurvey( surveyQuestion );
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
  private void hydrateSurveyQuestionResponses( SurveyQuestion surveyQuestion )
  {
    initialize( surveyQuestion.getSurveyQuestionResponses() );
  }

  /**
   * Hydrate the survey question response list on each survey question
   * 
   * @param surveyQuestion
   */
  private void hydrateSurveyQuestionSurvey( SurveyQuestion surveyQuestion )
  {
    initialize( surveyQuestion.getSurvey().getSurveyQuestions() );
  }
}
