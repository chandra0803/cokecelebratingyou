
package com.biperf.core.service.survey;

import java.util.List;

import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

public interface SurveyService extends SAO
{
  public static final String BEAN_NAME = "surveyService";

  public Survey getSurveyById( Long id );

  public List<Survey> getAll();

  public Survey saveSurvey( Survey survey ) throws ServiceErrorException;

  public Survey getSurveyByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  public void deleteSurveyQuestions( Long surveyId, List questionIds ) throws ServiceErrorException;

  public Survey copySurvey( Long surveyIdToCopy, String newSurveyName, String surveyFormType ) throws UniqueConstraintViolationException, ServiceErrorException;

  public SurveyQuestion getSurveyQuestionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  public SurveyQuestion saveSurveyQuestion( Long surveyId, SurveyQuestion managedSurveyQuestion, String questionText ) throws ServiceErrorException;

  public void deleteSurveyQuestionResponses( Long questionId, List questionResponseIds );

  public void reorderResponse( Long questionId, Long responseId, int newIndex );

  public SurveyQuestionResponse getSurveyQuestionResponseById( Long id );

  public SurveyQuestionResponse saveSurveyQuestionResponse( Long surveyQuestionId, SurveyQuestionResponse managedSurveyQuestionResponse, String questionResponseText ) throws ServiceErrorException;

  public void deleteSurvey( Long surveyId ) throws ServiceErrorException;

  public void deleteSurveys( List surveyIdList ) throws ServiceErrorException;

  public void reorderQuestion( Long surveyId, Long questionId, int newIndex );

  /**
   * Updates the status of a survey
   * 
   * @param surveyId
   */
  public void updateSurveyFormStatus( Long surveyId );

  public List<Survey> getAllSurveyFormsNotUnderConstructionByModuleType( String moduleType );

  public List<Long> getSurveysTakenByPromotionId( Long promotionId, String surveyId );

  public List<PromotionGoalQuestSurvey> getPromotionGoalQuestSurveysByPromotionId( Long promotionId );

  public List<SurveyPromotion> getSurveysByPromotionId( Long promotionId );

}
