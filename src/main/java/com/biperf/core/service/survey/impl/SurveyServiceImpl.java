
package com.biperf.core.service.survey.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.dao.survey.SurveyDAO;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.SurveyQuestionStatusType;
import com.biperf.core.domain.enums.SurveyResponseType;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.HibernateSessionManager;

public class SurveyServiceImpl implements SurveyService
{
  private CMAssetService cmAssetService;
  private SurveyDAO surveyDAO;

  @Override
  public Survey getSurveyById( Long id )
  {
    Survey survey = this.surveyDAO.getSurveyById( id );
    return survey;
  }

  @Override
  public List<Survey> getAll()
  {
    return surveyDAO.getAll();
  }

  @Override
  public Survey saveSurvey( Survey survey ) throws ServiceErrorException
  {
    Survey dbSurvey;
    // --------------------------------------------------------------
    // Check to see if the Survey already exists in the database
    // ---------------------------------------------------------------
    if ( survey.getId() != null && survey.getId().longValue() > 0 )
    {
      dbSurvey = this.surveyDAO.getSurveyById( survey.getId() );
      if ( dbSurvey != null )
      {
        try
        {
          Survey surveyByName = this.surveyDAO.getSurveyByName( survey.getName() );

          // if we found a record in the database with the given form Name,
          // but the ids are not equal, we are trying to update to a
          // formName that already exists so throw a Duplicate Exception
          if ( surveyByName != null && surveyByName.getId().compareTo( survey.getId() ) != 0 )
          {
            throw new ServiceErrorException( ServiceErrorMessageKeys.SURVEY_DUPLICATE_ERR );
          }
          try
          {
            dbSurvey = this.surveyDAO.updateSurvey( survey );
          }
          catch( ConstraintViolationException cve )
          {
            throw new ServiceErrorException( ServiceErrorMessageKeys.SURVEY_DUPLICATE_ERR, cve );
          }
        }
        catch( ConstraintViolationException cve )
        {
          // this happens when we try to delete a question that has been responseed already
          throw new ServiceErrorException( "survey.errors.CANNOT_DELETE_ANSWERED_QUESTION", cve );
        }
      }
    }
    else
    {
      dbSurvey = this.surveyDAO.getSurveyByName( survey.getName() );
      if ( dbSurvey != null )
      {
        // if we found a record in the database with the given formName,
        // and our claimFormToSave ID is null or ZERO (trying to add a new one),
        // we are trying to insert a duplicate record.
        if ( survey.getId() == null || survey.getId().longValue() == 0 )
        {
          throw new ServiceErrorException( ServiceErrorMessageKeys.SURVEY_DUPLICATE_ERR );
        }
      }

      try
      {
        dbSurvey = this.surveyDAO.saveSurvey( survey );
      }
      catch( ConstraintViolationException cve )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.SURVEY_DUPLICATE_ERR, cve );
      }
    }
    return dbSurvey;

  }

  @Override
  public Survey getSurveyByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return surveyDAO.getSurveyByIdWithAssociations( id, associationRequestCollection );
  }

  public void deleteSurveyQuestions( Long surveyId, List questionIds ) throws ServiceErrorException
  {
    Survey survey = getSurveyById( surveyId );
    int activeQuestionsCount = survey.getActiveQuestions().size();

    Iterator surveyQuestionIter = survey.getSurveyQuestions().iterator();
    while ( surveyQuestionIter.hasNext() )
    {
      SurveyQuestion surveyQuestion = (SurveyQuestion)surveyQuestionIter.next();
      if ( questionIds.contains( surveyQuestion.getId() ) )
      {
        surveyQuestionIter.remove();
        activeQuestionsCount -= 1;
      }
    }

    // if survey is completed or assigned, make sure we still have enough active questions
    if ( survey.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.ASSIGNED ) || survey.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.COMPLETED ) )
    {
      if ( activeQuestionsCount < 1 )
      {
        throw new ServiceErrorExceptionWithRollback( "survey.errors.NOT_ENOUGH_ACTIVE" );
      }
    }

    saveSurvey( survey );
  }

  public Survey copySurvey( Long surveyIdToCopy, String newSurveyName, String surveyFormType ) throws UniqueConstraintViolationException, ServiceErrorException
  {
    Survey copiedSurvey;

    Survey savedSurvey = surveyDAO.getSurveyById( surveyIdToCopy );

    Hibernate.initialize( savedSurvey.getSurveyQuestions() );
    for ( Iterator<SurveyQuestion> iter = savedSurvey.getSurveyQuestions().iterator(); iter.hasNext(); )
    {
      SurveyQuestion question = iter.next();
      Hibernate.initialize( question.getSurveyQuestionResponses() );
    }

    Survey surveyCopy = (Survey)savedSurvey.deepCopy( true, newSurveyName, surveyFormType );
    createSurveyQuestionCMData( surveyCopy );// New ones need to be created Bug - 4385
    try
    {
      copiedSurvey = saveSurvey( surveyCopy );
    }
    catch( ConstraintViolationException cve )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SURVEY_DUPLICATE_ERR, cve );
    }

    return copiedSurvey;
  }

  private void createSurveyQuestionCMData( Survey survey ) throws ServiceErrorException
  {
    if ( survey.getSurveyQuestions() != null )
    {
      for ( SurveyQuestion managedSurveyQuestion : survey.getSurveyQuestions() )
      {
        try
        {
          String cmKeyFragment = cmAssetService.getUniqueKeyFragment();
          CMDataElement cmDataElement = new CMDataElement( Survey.CM_QUESTION_NAME_KEY_DESC, Survey.CM_QUESTION_NAME_KEY, managedSurveyQuestion.getQuestionText(), false );
          managedSurveyQuestion.setCmAssetName( Survey.CM_QUESTION_ASSET_PREFIX + cmKeyFragment );
          cmAssetService.createOrUpdateAsset( Survey.CM_QUESTION_SECTION, Survey.CM_QUESTION_ASSET_TYPE, Survey.CM_QUESTION_NAME_KEY_DESC, managedSurveyQuestion.getCmAssetName(), cmDataElement );
        }
        catch( ServiceErrorException e )
        {
          HibernateSessionManager.getSession().clear();
          throw new ServiceErrorException( "survey.errors.DUPLICATE_QUESTION", e );
        }
        createSurveyQuestionResponseCMData( managedSurveyQuestion );
      }
    }
  }

  protected void createSurveyQuestionResponseCMData( SurveyQuestion managedSurveyQuestion ) throws ServiceErrorException
  {
    if ( managedSurveyQuestion != null && managedSurveyQuestion.getSurveyQuestionResponses() != null )
    {
      try
      {
        for ( SurveyQuestionResponse managedSurveyQuestionResponse : managedSurveyQuestion.getSurveyQuestionResponses() )
        {
          String cmKeyFragment = cmAssetService.getUniqueKeyFragment();
          CMDataElement cmDataElement = new CMDataElement( Survey.CM_QUESTION_RESPONSE_KEY_DESC, Survey.CM_QUESTION_RESPONSE_KEY, managedSurveyQuestionResponse.getQuestionResponseText(), false );
          managedSurveyQuestionResponse.setCmAssetCode( Survey.CM_QUESTION_RESPONSE_ASSET_PREFIX + cmKeyFragment );
          cmAssetService.createOrUpdateAsset( Survey.CM_QUESTION_RESPONSE_SECTION,
                                              Survey.CM_QUESTION_RESPONSE_ASSET_TYPE,
                                              Survey.CM_QUESTION_RESPONSE_KEY_DESC,
                                              managedSurveyQuestionResponse.getCmAssetCode(),
                                              cmDataElement );
        }
      }
      catch( ServiceErrorException e )
      {
        HibernateSessionManager.getSession().clear();
        throw new ServiceErrorException( "survey.errors.DUPLICATE_RESPONSE", e );
      }
    }
  }

  public SurveyQuestion getSurveyQuestionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.surveyDAO.getSurveyQuestionByIdWithAssociations( id, associationRequestCollection );
  }

  public SurveyQuestion saveSurveyQuestion( Long surveyId, SurveyQuestion managedSurveyQuestion, String questionText ) throws ServiceErrorException
  {
    Survey survey = this.surveyDAO.getSurveyById( surveyId );

    SurveyQuestion attachedQuestion = null;

    // Saving the surveyQuestion could result in a unique constraint on the name within the
    // surveyQuestion.
    try
    {
      // Check to see if this is a new or updated question
      if ( null != managedSurveyQuestion.getId() && managedSurveyQuestion.getId().longValue() != 0 )
      {
        int activeQuestionsCount = survey.getActiveQuestions().size();

        // ---------------
        // update question
        // ---------------
        // CM Integration
        CMDataElement cmDataElement = new CMDataElement( Survey.CM_QUESTION_NAME_KEY_DESC, Survey.CM_QUESTION_NAME_KEY, questionText, false );

        cmAssetService.createOrUpdateAsset( Survey.CM_QUESTION_SECTION, Survey.CM_QUESTION_ASSET_TYPE, Survey.CM_QUESTION_NAME_KEY_DESC, managedSurveyQuestion.getCmAssetName(), cmDataElement );

        Iterator attachedQuestionIter = survey.getSurveyQuestions().iterator();
        while ( attachedQuestionIter.hasNext() )
        {
          attachedQuestion = (SurveyQuestion)attachedQuestionIter.next();
          if ( attachedQuestion.getId().equals( managedSurveyQuestion.getId() ) )
          {
            if ( managedSurveyQuestion.getStatusType().getCode().equals( SurveyQuestionStatusType.INACTIVE ) && attachedQuestion.getStatusType().getCode().equals( SurveyQuestionStatusType.ACTIVE ) )
            {
              activeQuestionsCount -= 1;
            }

            attachedQuestion.setCmAssetName( managedSurveyQuestion.getCmAssetName() );
            attachedQuestion.setStatusType( managedSurveyQuestion.getStatusType() );
            attachedQuestion.setResponseType( managedSurveyQuestion.getResponseType() );
            attachedQuestion.setOpenEndedRequired( managedSurveyQuestion.isOpenEndedRequired() );
            if ( attachedQuestion.getResponseType().getCode().equals( SurveyResponseType.SLIDER_SELECTION ) )
            {
              attachedQuestion.setStartSelectionLabel( managedSurveyQuestion.getStartSelectionLabel() );
              attachedQuestion.setEndSelectionLabel( managedSurveyQuestion.getEndSelectionLabel() );
              attachedQuestion.setStartSelectionValue( managedSurveyQuestion.getStartSelectionValue() );
              attachedQuestion.setEndSelectionValue( managedSurveyQuestion.getEndSelectionValue() );
              attachedQuestion.setPrecisionValue( managedSurveyQuestion.getPrecisionValue() );
            }
          }
        }

        // if inactivating a previously active question and survey is completed or assigned,
        // make sure we still have enough active questions
        if ( survey.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.ASSIGNED ) || survey.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.COMPLETED ) )
        {
          if ( activeQuestionsCount < 1 )
          {
            throw new ServiceErrorException( "survey.errors.NOT_ENOUGH_ACTIVE" );
          }
        }
      }
      else
      {

        // -----------
        // new question
        // -----------
        // CM Integration
        String cmKeyFragment = cmAssetService.getUniqueKeyFragment();
        managedSurveyQuestion.setCmAssetName( Survey.CM_QUESTION_ASSET_PREFIX + cmKeyFragment );
        CMDataElement cmDataElement = new CMDataElement( Survey.CM_QUESTION_NAME_KEY_DESC, Survey.CM_QUESTION_NAME_KEY, questionText, false );

        cmAssetService.createOrUpdateAsset( Survey.CM_QUESTION_SECTION, Survey.CM_QUESTION_ASSET_TYPE, Survey.CM_QUESTION_NAME_KEY_DESC, managedSurveyQuestion.getCmAssetName(), cmDataElement );

        survey.addSurveyQuestion( managedSurveyQuestion );
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
      HibernateSessionManager.getSession().clear();
      if ( e instanceof ServiceErrorException )
      {
        throw (ServiceErrorException)e;
      }

      throw new ServiceErrorException( "survey.errors.DUPLICATE_QUESTION", e );
    }
    return managedSurveyQuestion;
  }

  public void deleteSurveyQuestionResponses( Long questionId, List questionResponseIds )
  {
    SurveyQuestion surveyQuestion = this.surveyDAO.getSurveyQuestionById( questionId );

    for ( int i = 0; i < questionResponseIds.size(); i++ )
    {
      Long id = (Long)questionResponseIds.get( i );
      Iterator questionResponseIter = surveyQuestion.getSurveyQuestionResponses().iterator();
      while ( questionResponseIter.hasNext() )
      {
        SurveyQuestionResponse response = (SurveyQuestionResponse)questionResponseIter.next();
        if ( response.getId().equals( id ) )
        {
          questionResponseIter.remove();
        }
      }
    }
  }

  public void reorderResponse( Long questionId, Long responseId, int newIndex )
  {
    SurveyQuestionResponse response = null;
    SurveyQuestion question = null;

    response = surveyDAO.getSurveyQuestionResponseById( responseId );

    question = surveyDAO.getSurveyQuestionById( questionId );

    question.getSurveyQuestionResponses().remove( response );

    if ( newIndex < 0 )
    {
      newIndex = 0;
    }
    if ( newIndex < question.getSurveyQuestionResponses().size() )
    {
      question.getSurveyQuestionResponses().add( newIndex, response );
    }
    else
    {
      question.getSurveyQuestionResponses().add( response );
    }
  }

  public SurveyQuestionResponse getSurveyQuestionResponseById( Long id )
  {
    return this.surveyDAO.getSurveyQuestionResponseById( id );
  }

  public SurveyQuestionResponse saveSurveyQuestionResponse( Long surveyQuestionId, SurveyQuestionResponse managedSurveyQuestionResponse, String questionResponseText ) throws ServiceErrorException
  {
    SurveyQuestion surveyQuestion = this.surveyDAO.getSurveyQuestionById( surveyQuestionId );

    SurveyQuestionResponse attachedQuestionResponse = null;

    // Saving the surveyQuestionResponse could result in a unique constraint on the name within the
    // surveyQuestionResponse.
    try
    {
      // Check to see if this is a new or updated question response
      if ( null != managedSurveyQuestionResponse.getId() && managedSurveyQuestionResponse.getId().longValue() != 0 )
      {
        Iterator attachedQuestionResponseIter = surveyQuestion.getSurveyQuestionResponses().iterator();
        while ( attachedQuestionResponseIter.hasNext() )
        {
          attachedQuestionResponse = (SurveyQuestionResponse)attachedQuestionResponseIter.next();
          if ( attachedQuestionResponse.getId().equals( managedSurveyQuestionResponse.getId() ) )
          {
            attachedQuestionResponse.setCmAssetCode( managedSurveyQuestionResponse.getCmAssetCode() );
            attachedQuestionResponse.setStatusType( managedSurveyQuestionResponse.getStatusType() );
            attachedQuestionResponse.setSurveyQuestion( surveyQuestion );
          }
        }
      }
      else
      {
        managedSurveyQuestionResponse.setCmAssetCode( cmAssetService.getUniqueAssetCode( Survey.CM_QUESTION_RESPONSE_ASSET_PREFIX ) );
        surveyQuestion.addSurveyQuestionResponse( managedSurveyQuestionResponse );
      }

      CMDataElement cmDataElementResponse = new CMDataElement( Survey.CM_QUESTION_RESPONSE_KEY_DESC, Survey.CM_QUESTION_RESPONSE_KEY, questionResponseText, false );

      List<CMDataElement> elementList = new ArrayList<CMDataElement>();
      elementList.add( cmDataElementResponse );

      cmAssetService.createOrUpdateAsset( Survey.CM_QUESTION_RESPONSE_SECTION,
                                          Survey.CM_QUESTION_RESPONSE_ASSET_TYPE,
                                          Survey.CM_QUESTION_RESPONSE_KEY_DESC,
                                          managedSurveyQuestionResponse.getCmAssetCode(),
                                          elementList );
    }
    catch( Exception e )
    {

      HibernateSessionManager.getSession().clear();
      throw new ServiceErrorException( "survey.errors.DUPLICATE_RESPONSE", e );
    }

    return managedSurveyQuestionResponse;
  }

  public void deleteSurveys( List surveyIdList ) throws ServiceErrorException
  {
    Iterator idIter = surveyIdList.iterator();

    while ( idIter.hasNext() )
    {
      this.deleteSurvey( (Long)idIter.next() );
    }
  }

  public void deleteSurvey( Long surveyId ) throws ServiceErrorException
  {
    Survey surveyToDelete = surveyDAO.getSurveyById( surveyId );

    if ( surveyToDelete != null )
    {
      if ( surveyToDelete.isDeleteable() )
      {
        // the survey is either in under construction or complete status, do a physical delete
        surveyDAO.deleteSurvey( surveyToDelete );
      }
      else if ( surveyToDelete.getClaimFormStatusType().isUnderConstruction() )
      {
        // The survey is at a status that we cannot delete
        throw new ServiceErrorException( ServiceErrorMessageKeys.SURVEY_DELETE_STATUS_ERR );
      }
      else
      {
        // The survey is linked to only expired promotions
        throw new ServiceErrorException( ServiceErrorMessageKeys.SURVEY_DELETE_LINKED_ERR );
      }
    }
  }

  public void reorderQuestion( Long surveyId, Long questionId, int newIndex )
  {
    SurveyQuestion question = null;
    Survey survey = null;

    question = surveyDAO.getSurveyQuestionById( questionId );

    survey = surveyDAO.getSurveyById( surveyId );

    survey.getSurveyQuestions().remove( question );

    if ( newIndex < 0 )
    {
      // add the question to the begining of the list
      newIndex = 0;
    }

    if ( newIndex < survey.getSurveyQuestions().size() )
    {
      survey.getSurveyQuestions().add( newIndex, question );
    }
    else
    {
      survey.getSurveyQuestions().add( question );
    }
  }

  /**
   * Updates the status of a survey
   * 
   * @param surveyId
   */
  public void updateSurveyFormStatus( Long surveyId )
  {
    Survey survey = getSurveyById( surveyId );
    if ( survey.getPromotions().isEmpty() && survey.getGqPromotions().isEmpty() )
    {
      if ( survey.isAssigned() )
      {
        survey.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
      }
    }
    else
    {
      boolean expiredOnly = true;
      if ( !survey.getPromotions().isEmpty() )
      {
        for ( Iterator iter = survey.getPromotions().iterator(); iter.hasNext(); )
        {
          Promotion promo = (Promotion)iter.next();
          if ( !promo.isExpired() )
          {
            // found a promotion that is not expired
            expiredOnly = false;
            break;
          }
        }
      }
      else if ( !survey.getGqPromotions().isEmpty() )
      {
        for ( Iterator iter = survey.getGqPromotions().iterator(); iter.hasNext(); )
        {
          PromotionGoalQuestSurvey gqSurvey = (PromotionGoalQuestSurvey)iter.next();
          if ( !gqSurvey.getPromotion().isExpired() )
          {
            // found a promotion that is not expired
            expiredOnly = false;
            break;
          }
        }
      }

      if ( expiredOnly )
      {
        survey.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
      }
      else
      {
        survey.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ) );
      }
    }
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setSurveyDAO( SurveyDAO surveyDAO )
  {
    this.surveyDAO = surveyDAO;
  }

  public List<Survey> getAllSurveyFormsNotUnderConstructionByModuleType( String moduleType )
  {
    return surveyDAO.getAllSurveyFormsNotUnderConstructionByModuleType( moduleType );
  }

  public List<Long> getSurveysTakenByPromotionId( Long promotionId, String surveyId )
  {
    return surveyDAO.getSurveysTakenByPromotionId( promotionId, surveyId );
  }

  public List<PromotionGoalQuestSurvey> getPromotionGoalQuestSurveysByPromotionId( Long promotionId )
  {
    return surveyDAO.getPromotionGoalQuestSurveysByPromotionId( promotionId );
  }

  public List<SurveyPromotion> getSurveysByPromotionId( Long promotionId )
  {
    return surveyDAO.getSurveysByPromotionId( promotionId );
  }

}
