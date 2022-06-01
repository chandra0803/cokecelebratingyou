
package com.biperf.core.dao.survey;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.service.AssociationRequestCollection;

public interface SurveyDAO extends DAO
{
  public static final String BEAN_NAME = "surveyDAO";

  public Survey getSurveyById( Long quizId );

  public List<Survey> getAll();

  public Survey saveSurvey( Survey survey );

  public Survey getSurveyByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  public Survey getSurveyByName( String name );

  public Survey updateSurvey( Survey survey ) throws ConstraintViolationException;

  public SurveyQuestion getSurveyQuestionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  public SurveyQuestion getSurveyQuestionById( Long surveyQuestionId );

  public SurveyQuestionResponse getSurveyQuestionResponseById( Long surveyQuestionResponseId );

  public void deleteSurvey( Survey survey );

  public List<Survey> getAllSurveyFormsNotUnderConstructionByModuleType( String moduleType );

  public List<Long> getSurveysTakenByPromotionId( Long promotionId, String surveyId );

  public List<PromotionGoalQuestSurvey> getPromotionGoalQuestSurveysByPromotionId( Long promotionId );

  public List<SurveyPromotion> getSurveysByPromotionId( Long promotionId );

}
