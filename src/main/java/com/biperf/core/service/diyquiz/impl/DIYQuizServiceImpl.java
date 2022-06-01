
package com.biperf.core.service.diyquiz.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.diyquiz.DIYQuizDAO;
import com.biperf.core.dao.quiz.QuizDAO;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.diyquiz.DIYQuizParticipant;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.QuizQuestionStatusType;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.diyquiz.DIYQuizService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * 
 * DIYQuizServiceImpl.
 * 
 * @author kandhi
 * @since Jul 9, 2013
 * @version 1.0
 */
public class DIYQuizServiceImpl implements DIYQuizService
{

  private DIYQuizDAO diyQuizDAO;
  private QuizDAO quizDAO;
  private PromotionService promotionService;
  private CMAssetService cmAssetService;
  private GamificationService gamificationService;
  private List<String> acceptableExtentions;
  private ParticipantService participantService;
  private SystemVariableService systemVariableService;

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  @Override
  public DIYQuiz getQuizById( Long id )
  {
    return this.diyQuizDAO.getQuizById( id );
  }

  public boolean isValidImageFormat( String format )
  {
    if ( format != null && acceptableExtentions.contains( format.toLowerCase() ) )
    {
      return true;
    }
    return false;
  }

  @Override
  public DIYQuiz saveDIYQuiz( DIYQuiz managedQuiz, Long promotionId ) throws ServiceErrorException
  {
    DIYQuiz attachedQuiz = null;
    BadgeRule badgeRule = null;
    PromotionCert certificate = null;

    Quiz quizByName = this.quizDAO.getQuizByName( managedQuiz.getName() );

    if ( quizByName != null )
    {
      if ( managedQuiz.getId() != null && managedQuiz.getId() > 0 && quizByName.getId().compareTo( managedQuiz.getId() ) != 0 )
      {
        // if we found a record in the database with the given form Name,
        // but the ids are not equal, we are trying to update to a
        // formName that already exists so throw a Duplicate Exception
        throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DUPLICATE_ERR );
      }
      else if ( managedQuiz.getId() == null || managedQuiz.getId().longValue() == 0 )
      {
        // if we found a record in the database with the given formName, ID is null or ZERO
        // (trying to add a new one), we are trying to insert a duplicate record.
        throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DUPLICATE_ERR );
      }
    }

    if ( managedQuiz.getBadgeRule() != null && managedQuiz.getBadgeRule().getId() > 0 )
    {
      badgeRule = gamificationService.getBadgeRuleById( managedQuiz.getBadgeRule().getId() );
      managedQuiz.setBadgeRule( badgeRule );
    }

    if ( managedQuiz.getCertificate() != null && managedQuiz.getCertificate().getId() > 0 )
    {
      certificate = promotionService.getPromoCertificateById( managedQuiz.getCertificate().getId() );
      managedQuiz.setCertificate( certificate );
    }

    if ( managedQuiz.getId() != null && managedQuiz.getId() > 0 )
    {
      // Update Quiz
      attachedQuiz = getQuizById( managedQuiz.getId() );
      changeStatusForRemovedPax( managedQuiz, attachedQuiz );
      addNewPax( managedQuiz, attachedQuiz, isNotificationTextModified( managedQuiz, attachedQuiz ) );
      removeQuizQuestions( managedQuiz, attachedQuiz );
      addQuizQuestions( managedQuiz, attachedQuiz );
      removeLearningObjects( managedQuiz, attachedQuiz );
      addLearningObjects( managedQuiz, attachedQuiz );
      attachedQuiz.setPassingScore( managedQuiz.getPassingScore() );
      attachedQuiz.setAllowUnlimitedAttempts( managedQuiz.isAllowUnlimitedAttempts() );
      attachedQuiz.setBadgeRule( managedQuiz.getBadgeRule() );
      attachedQuiz.setCertificate( managedQuiz.getCertificate() );
      attachedQuiz.setClaimFormStatusType( managedQuiz.getClaimFormStatusType() );
      attachedQuiz.setStartDate( managedQuiz.getStartDate() );
      attachedQuiz.setEndDate( managedQuiz.getEndDate() );
      attachedQuiz.setMaximumAttempts( managedQuiz.getMaximumAttempts() );
      attachedQuiz.setName( managedQuiz.getName() );
      attachedQuiz.setNumberOfQuestionsAsked( managedQuiz.getActualNumberOfQuestionsAsked() );
      attachedQuiz.setQuizType( managedQuiz.getQuizType() );
      attachedQuiz.setDescription( managedQuiz.getDescription() );
      attachedQuiz.setNotificationText( managedQuiz.getNotificationText() );
      attachedQuiz.setIntroductionText( managedQuiz.getIntroductionText() );

      if ( attachedQuiz.getPassingScore() > attachedQuiz.getActiveQuestions().size() )
      {
        throw new ServiceErrorException( "quiz.errors.NOT_ENOUGH_ACTIVE" );
      }
    }
    else
    {
      // New Quiz
      Promotion quizPromotion = (Promotion)promotionService.getPromotionById( promotionId );
      managedQuiz.setPromotion( quizPromotion );
      addQuizQuestions( managedQuiz, attachedQuiz );
      addLearningObjects( managedQuiz, attachedQuiz );
      attachedQuiz = diyQuizDAO.saveQuiz( managedQuiz );
    }
    return attachedQuiz;
  }

  private boolean isNotificationTextModified( DIYQuiz managedQuiz, DIYQuiz attachedQuiz )
  {
    boolean isNotificationTextModified = false;
    if ( attachedQuiz.getNotificationText() == null && managedQuiz.getNotificationText() != null )
    {
      isNotificationTextModified = true;
    }
    else if ( attachedQuiz.getNotificationText() != null && managedQuiz.getNotificationText() != null && !attachedQuiz.getNotificationText().equals( managedQuiz.getNotificationText() ) )
    {
      isNotificationTextModified = true;
    }
    return isNotificationTextModified;
  }

  @Override
  public void checkDuplicateQuizName( String quizName, Long quizId ) throws ServiceErrorException
  {
    Quiz quizByName = this.quizDAO.getQuizByName( quizName );

    if ( quizByName != null )
    {
      if ( quizId != null && quizId > 0 && quizByName.getId().compareTo( quizId ) != 0 )
      {
        // if we found a record in the database with the given form Name,
        // but the ids are not equal, we are trying to update to a
        // formName that already exists so throw a Duplicate Exception
        throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DUPLICATE_ERR );
      }
      else if ( quizId == null || quizId.longValue() == 0 )
      {
        // if we found a record in the database with the given formName, ID is null or ZERO
        // (trying to add a new one), we are trying to insert a duplicate record.
        throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DUPLICATE_ERR );
      }
    }
  }

  /**
   * Deletes a list of quizes. Overridden from
   * 
   * @see com.biperf.core.service.quiz.DIYQuizService#deleteQuizes(java.util.List)
   * @param quizIdList - List of quiz.id
   * @throws ServiceErrorException
   */
  @SuppressWarnings( "rawtypes" )
  public void deleteQuizes( List quizIdList ) throws ServiceErrorException
  {
    Iterator idIter = quizIdList.iterator();

    while ( idIter.hasNext() )
    {
      this.deleteQuiz( (Long)idIter.next() );
    }
  }

  /**
   * Deletes a quiz if it is under_construction or there are no claims associated. Otherwise the quiz cannot be
   * deleted. Overridden from
   * 
   * @see com.biperf.core.service.quiz.DIYQuizService#deleteQuiz(Long)
   * @param quizId
   * @throws ServiceErrorException
   */
  public void deleteQuiz( Long quizId ) throws ServiceErrorException
  {
    Quiz quizToDelete = quizDAO.getQuizById( quizId );

    if ( quizToDelete != null )
    {
      if ( quizToDelete.getQuizClaims() == null || quizToDelete.getQuizClaims().size() == 0 || quizToDelete.getClaimFormStatusType().isUnderConstruction() )
      {
        // if the quiz has a no claims associated, do a physical delete
        quizDAO.deleteQuiz( quizToDelete );
      }
      else
      {
        // The quiz is at a status that we cannot delete
        throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DELETE_STATUS_ERR );
      }
    }
  }

  private void addLearningObjects( DIYQuiz managedQuiz, DIYQuiz attachedQuiz )
  {
    // Case when a new learning material is added
    for ( QuizLearningObject managedLearningObject : managedQuiz.getLearningObjects() )
    {
      boolean found = false;
      if ( attachedQuiz != null )
      {
        for ( QuizLearningObject attachedLearningObject : attachedQuiz.getLearningObjects() )
        {
          if ( managedLearningObject.getId() != null && managedLearningObject.getId().equals( attachedLearningObject.getId() ) )
          {
            attachedLearningObject.setContentResourceCMCode( managedLearningObject.getContentResourceCMCode() );
            attachedLearningObject.setSlideNumber( managedLearningObject.getSlideNumber() );
            attachedLearningObject.setStatus( QuizLearningObject.ACTIVE_STATUS );
            found = true;
            break;
          }
        }
      }
      if ( !found )
      {
        if ( attachedQuiz != null )
        {
          attachedQuiz.addQuizLearningObject( managedLearningObject );
        }
      }
    }
  }

  private void removeLearningObjects( DIYQuiz managedQuiz, DIYQuiz attachedQuiz )
  {
    // Case when a quiz learning object is deleted
    for ( QuizLearningObject attachedQuizLearningObject : attachedQuiz.getLearningObjects() )
    {
      boolean found = false;
      for ( QuizLearningObject managedQuizLearningObject : managedQuiz.getLearningObjects() )
      {
        if ( managedQuizLearningObject.getId() != null && managedQuizLearningObject.getId().equals( attachedQuizLearningObject.getId() ) )
        {
          found = true;
          break;
        }
      }
      if ( !found )
      {
        // When deleted make the learning object status as inactive
        attachedQuizLearningObject.setStatus( QuizLearningObject.INACTIVE_STATUS );
      }
    }
  }

  private void addQuizQuestions( DIYQuiz managedQuiz, DIYQuiz attachedQuiz ) throws ServiceErrorException
  {
    // Case when a new quiz question is added
    for ( QuizQuestion managedQuizQuestion : managedQuiz.getQuizQuestions() )
    {
      boolean found = false;
      if ( attachedQuiz != null )
      {
        for ( QuizQuestion attachedQuizQuestion : attachedQuiz.getQuizQuestions() )
        {
          if ( managedQuizQuestion.getId() != null && managedQuizQuestion.getId().equals( attachedQuizQuestion.getId() ) )
          {
            try
            {
              CMDataElement cmDataElement = new CMDataElement( Quiz.CM_QUESTION_NAME_KEY_DESC, Quiz.CM_QUESTION_NAME_KEY, managedQuizQuestion.getText(), false );
              cmAssetService.createOrUpdateAsset( Quiz.CM_QUESTION_SECTION, Quiz.CM_QUESTION_ASSET_TYPE, Quiz.CM_QUESTION_NAME_KEY_DESC, attachedQuizQuestion.getCmAssetName(), cmDataElement );
              addQuizQuestionAnswer( managedQuizQuestion, attachedQuizQuestion );
            }
            catch( ServiceErrorException e )
            {
              throw new ServiceErrorException( "quiz.errors.DUPLICATE_QUESTION", e );
            }
            found = true;
            break;
          }
        }
      }
      if ( !found )
      {
        String cmKeyFragment = cmAssetService.getUniqueKeyFragment();
        managedQuizQuestion.setCmAssetName( Quiz.CM_QUESTION_ASSET_PREFIX + cmKeyFragment );
        CMDataElement cmDataElement = new CMDataElement( Quiz.CM_QUESTION_NAME_KEY_DESC, Quiz.CM_QUESTION_NAME_KEY, managedQuizQuestion.getText(), false );
        cmAssetService.createOrUpdateAsset( Quiz.CM_QUESTION_SECTION, Quiz.CM_QUESTION_ASSET_TYPE, Quiz.CM_QUESTION_NAME_KEY_DESC, managedQuizQuestion.getCmAssetName(), cmDataElement );
        addQuizQuestionAnswer( managedQuizQuestion, null );
        managedQuizQuestion.setQuiz( managedQuiz );
        if ( attachedQuiz != null )
        {
          attachedQuiz.addQuizQuestion( managedQuizQuestion );
        }
      }
    }
  }

  private void removeQuizQuestions( DIYQuiz managedQuiz, DIYQuiz attachedQuiz )
  {
    // Case when a quiz question is deleted
    for ( QuizQuestion attchedQuizQuestion : attachedQuiz.getQuizQuestions() )
    {
      boolean found = false;
      for ( QuizQuestion managedQuizQuestion : managedQuiz.getQuizQuestions() )
      {
        if ( managedQuizQuestion.getId() != null && managedQuizQuestion.getId().equals( attchedQuizQuestion.getId() ) )
        {
          found = true;
          break;
        }
      }
      if ( !found )
      {
        // When deleted make the question as inactive
        attchedQuizQuestion.setStatusType( QuizQuestionStatusType.lookup( QuizQuestionStatusType.INACTIVE ) );
      }
    }
  }

  protected void addQuizQuestionAnswer( QuizQuestion managedQuizQuestion, QuizQuestion attachedQuizQuestion ) throws ServiceErrorException
  {
    try
    {
      // Remove the ones that are deleted
      if ( attachedQuizQuestion != null )
      {
        Iterator<QuizQuestionAnswer> attachedQuizQuestionAnswerIter = attachedQuizQuestion.getQuizQuestionAnswers().iterator();
        while ( attachedQuizQuestionAnswerIter.hasNext() )
        {
          QuizQuestionAnswer attachedQuizQuestionAnswer = attachedQuizQuestionAnswerIter.next();
          boolean found = false;
          for ( QuizQuestionAnswer managedQuizQuestionAnswer : managedQuizQuestion.getQuizQuestionAnswers() )
          {
            if ( managedQuizQuestionAnswer.getId() != null && attachedQuizQuestionAnswer.getId().equals( managedQuizQuestionAnswer.getId() ) )
            {
              found = true;
            }
          }
          if ( !found )
          {
            attachedQuizQuestionAnswerIter.remove();
          }
        }
      }

      // Add newly added quiz question answers
      for ( QuizQuestionAnswer managedQuizQuestionAnswer : managedQuizQuestion.getQuizQuestionAnswers() )
      {
        boolean found = false;
        if ( attachedQuizQuestion != null )
        {
          for ( QuizQuestionAnswer attachedQuizQuestionAnswer : attachedQuizQuestion.getQuizQuestionAnswers() )
          {
            if ( attachedQuizQuestionAnswer.getId().equals( managedQuizQuestionAnswer.getId() ) )
            {
              attachedQuizQuestionAnswer.setCorrect( managedQuizQuestionAnswer.isCorrect() );
              found = true;
              break;
            }
          }
        }
        if ( !found )
        {
          managedQuizQuestionAnswer.setCmAssetCode( cmAssetService.getUniqueAssetCode( Quiz.CM_QUESTION_ANSWER_ASSET_PREFIX ) );
          managedQuizQuestionAnswer.setAnswerCmKey( Quiz.CM_QUESTION_ANSWER_KEY );
          managedQuizQuestionAnswer.setExplanationCmKey( Quiz.CM_QUESTION_ANSWER_EXPLANATION_KEY );
          managedQuizQuestionAnswer.setQuizQuestion( managedQuizQuestion );
          if ( attachedQuizQuestion != null )
          {
            attachedQuizQuestion.addQuizQuestionAnswer( managedQuizQuestionAnswer );
          }
        }

        CMDataElement cmDataElementAnswer = new CMDataElement( Quiz.CM_QUESTION_ANSWER_KEY_DESC, Quiz.CM_QUESTION_ANSWER_KEY, managedQuizQuestionAnswer.getText(), false );
        cmAssetService.createOrUpdateAsset( Quiz.CM_QUESTION_ANSWER_SECTION,
                                            Quiz.CM_QUESTION_ANSWER_ASSET_TYPE,
                                            Quiz.CM_QUESTION_ANSWER_KEY_DESC,
                                            managedQuizQuestionAnswer.getCmAssetCode(),
                                            cmDataElementAnswer );
      }
    }
    catch( Exception e )
    {
      HibernateSessionManager.getSession().clear();
      throw new ServiceErrorException( "quiz.errors.DUPLICATE_ANSWER", e );
    }
  }

  private void changeStatusForRemovedPax( DIYQuiz managedQuiz, DIYQuiz attachedQuiz )
  {
    for ( DIYQuizParticipant attachedPax : attachedQuiz.getParticipants() )
    {
      boolean found = false;
      for ( DIYQuizParticipant managedPax : managedQuiz.getParticipants() )
      {
        if ( managedPax.getId() != null && attachedPax.getId().equals( managedPax.getId() ) )
        {
          found = true;
          break;
        }
      }
      if ( !found )
      {
        attachedPax.setStatusType( ParticipantStatus.INACTIVE );
      }
    }
  }

  private void addNewPax( DIYQuiz managedQuiz, DIYQuiz attachedQuiz, boolean isNotificationTextModified )
  {
    List<DIYQuizParticipant> newlyAddedPaxList = new ArrayList<DIYQuizParticipant>();
    for ( DIYQuizParticipant managedPax : managedQuiz.getParticipants() )
    {
      boolean found = false;
      for ( DIYQuizParticipant attachedPax : attachedQuiz.getParticipants() )
      {
        if ( managedPax.getId() != null && attachedPax.getId().equals( managedPax.getId() ) )
        {
          // Bug 3971 - Resend the notification when the text is modified
          if ( isNotificationTextModified )
          {
            attachedPax.setIsNotificationSent( false );
          }
          found = true;
          break;
        }
      }
      if ( !found )
      {
        newlyAddedPaxList.add( managedPax );
      }
    }
    for ( DIYQuizParticipant newPax : newlyAddedPaxList )
    {
      attachedQuiz.addParticipant( newPax );
    }
  }

  @SuppressWarnings( "rawtypes" )
  public DIYQuiz getQuizByIdWithAssociations( Long id, AssociationRequestCollection associationRequests )
  {
    DIYQuiz quiz = getQuizById( id );
    for ( Iterator iterator = associationRequests.iterator(); iterator.hasNext(); )
    {
      AssociationRequest req = (AssociationRequest)iterator.next();
      if ( req instanceof ParticipantAssociationRequest )
      {
        Set<DIYQuizParticipant> paxs = quiz.getParticipants();
        for ( Iterator<DIYQuizParticipant> iter = paxs.iterator(); iter.hasNext(); )
        {
          DIYQuizParticipant diyPax = iter.next();
          req.execute( diyPax.getParticipant() );
        }
      }
      else
      {
        req.execute( quiz );
      }
    }
    return quiz;
  }

  @SuppressWarnings( "unchecked" )
  public MailingRecipient getMailingRecipient( Participant pax )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );

    MailingRecipientData firstName = new MailingRecipientData();
    firstName.setKey( "firstName" );
    firstName.setValue( pax.getFirstName() );
    firstName.setMailingRecipient( mailingRecipient );
    mailingRecipient.addMailingRecipientData( firstName );

    MailingRecipientData lastName = new MailingRecipientData();
    lastName.setKey( "lastName" );
    lastName.setValue( pax.getLastName() );
    lastName.setMailingRecipient( mailingRecipient );
    mailingRecipient.addMailingRecipientData( lastName );

    AssociationRequestCollection reqCollection = new AssociationRequestCollection();
    reqCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    pax = participantService.getParticipantByIdWithAssociations( pax.getId(), reqCollection );

    mailingRecipient.setLocale( pax.getLanguageType() != null ? pax.getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
    if ( pax.getPrimaryEmailAddress() != null )
    {
      mailingRecipient.setPreviewEmailAddress( pax.getPrimaryEmailAddress().getEmailAddr() );
    }
    mailingRecipient.setUser( pax );
    return mailingRecipient;
  }

  public List<DIYQuiz> getQuizByPromotionAndStatus( String status, Long promotionId )
  {
    return this.diyQuizDAO.getQuizByPromotionAndStatus( status, promotionId );
  }

  @Override
  public List<DIYQuiz> getAllQuizzesByPromotionId( Long promotionId )
  {
    return this.diyQuizDAO.getAllQuizzesByPromotionId( promotionId );
  }

  public List<DIYQuiz> getEligibleQuizzesForParticipantByPromotion( Long promotionId, Long participantId )
  {
    return this.diyQuizDAO.getEligibleQuizzesForParticipantByPromotion( promotionId, participantId );
  }

  public void setDiyQuizDAO( DIYQuizDAO diyQuizDAO )
  {
    this.diyQuizDAO = diyQuizDAO;
  }

  public void setQuizDAO( QuizDAO quizDAO )
  {
    this.quizDAO = quizDAO;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setAcceptableExtentions( List<String> acceptableExtentions )
  {
    this.acceptableExtentions = acceptableExtentions;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }
}
