
package com.biperf.core.dao.survey.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.survey.SurveyDAO;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class SurveyDAOImpl extends BaseDAO implements SurveyDAO
{
  @Override
  public Survey getSurveyById( Long surveyId )
  {
    return (Survey)getSession().get( Survey.class, surveyId );
  }

  @Override
  public List<Survey> getAll()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.promotion.AllSurveyList" ).list();
  }

  @Override
  public Survey saveSurvey( Survey survey )
  {
    getSession().save( survey );
    return survey;
  }

  @Override
  public Survey getSurveyByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Survey survey = (Survey)getSession().get( Survey.class, id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( survey );
    }
    return survey;
  }

  @Override
  public Survey getSurveyByName( String name )
  {
    return (Survey)getSession().getNamedQuery( "com.biperf.core.domain.promotion.SurveyByName" ).setString( "name", name.toUpperCase() ).uniqueResult();
  }

  @Override
  public Survey updateSurvey( Survey survey ) throws ConstraintViolationException
  {
    survey = (Survey)HibernateUtil.saveOrUpdateOrShallowMerge( survey );
    // Note: need to do the flush to get the Constraint Violation to bubble up.
    getSession().flush();

    return survey;
  }

  @Override
  public SurveyQuestion getSurveyQuestionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    SurveyQuestion surveyQuestion = (SurveyQuestion)getSession().get( SurveyQuestion.class, id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( surveyQuestion );
    }
    return surveyQuestion;
  }

  @Override
  public SurveyQuestion getSurveyQuestionById( Long surveyQuestionId )
  {
    return (SurveyQuestion)getSession().get( SurveyQuestion.class, surveyQuestionId );
  }

  @Override
  public com.biperf.core.domain.promotion.SurveyQuestionResponse getSurveyQuestionResponseById( Long surveyQuestionResponseId )
  {
    return (SurveyQuestionResponse)getSession().get( SurveyQuestionResponse.class, surveyQuestionResponseId );
  }

  @Override
  public void deleteSurvey( Survey survey )
  {
    getSession().delete( survey );
  }

  public List<Survey> getAllSurveyFormsNotUnderConstructionByModuleType( String moduleType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.claim.SurveyFormsNotUnderConstructionByModuleType" ).setString( "moduleType", moduleType );

    return query.list();
  }

  public List<Long> getSurveysTakenByPromotionId( Long promotionId, String surveyId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.survey.ParticipantSurvey.SurveyTakenList" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "surveyId", surveyId );

    return query.list();
  }

  public List<PromotionGoalQuestSurvey> getPromotionGoalQuestSurveysByPromotionId( Long promotionId )
  {
    Criteria criteria = getSession().createCriteria( PromotionGoalQuestSurvey.class );
    criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    return criteria.list();
  }

  public List<SurveyPromotion> getSurveysByPromotionId( Long promotionId )
  {
    Criteria criteria = getSession().createCriteria( SurveyPromotion.class );
    criteria.add( Restrictions.eq( "id", promotionId ) );
    return criteria.list();
  }
}
