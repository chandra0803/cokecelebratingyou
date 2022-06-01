/*
 * (c) 2005 BI, Inc. All rights reserved. $Source:
 * /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/quiz/impl/QuizServiceImpl.java,v $
 */

package com.biperf.core.service.quiz.impl;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.dao.quiz.QuizDAO;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.QuizQuestionStatusType;
import com.biperf.core.domain.enums.QuizType;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.quiz.QuizLearningSlideDetails;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.mtc.MTCService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.strategy.ImageResizeStrategy;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.QuizFileUploadValue;
import com.biperf.core.value.mtc.v1.upload.UploadResponse;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.domain.enums.DataTypeEnum;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * QuizServiceImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>Oct 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class QuizServiceImpl implements QuizService
{

  private CMAssetService cmAssetService;

  private static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;
  private static final Log logger = LogFactory.getLog( QuizServiceImpl.class );

  /** QuizDAO */
  private QuizDAO quizDAO;

  private FileUploadStrategy appDataDirFileUploadStrategy;
  private FileUploadStrategy webdavFileUploadStrategy;
  private ImageCropStrategy imageCropStrategy;
  private ImageResizeStrategy imageResizeStrategy;
  private SystemVariableService systemVariableService;
  private List<String> acceptableExtentions;
  @Autowired
  private MTCService mtcService;
  @Autowired
  private MTCVideoService mtcVideoService;

  public MTCVideoService getMtcVideoService()
  {
    return mtcVideoService;
  }

  public void setMtcVideoService( MTCVideoService mtcVideoService )
  {
    this.mtcVideoService = mtcVideoService;
  }

  /**
   * Set the quizDAO through injection.
   *
   * @param quizDAO
   */
  public void setQuizDAO( QuizDAO quizDAO )
  {
    this.quizDAO = quizDAO;
  }

  /**
   * Set the CMAssetService through IoC
   *
   * @param cmAssetService
   */
  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  /**
   * Retrieves all Quizes from the database.
   *
   * @return List a list of Quizes
   */
  public List getAll()
  {
    return quizDAO.getAll();
  }

  /**
   * Deletes a quiz if it is under_construction or complete. If the quiz is assigned it cannot be
   * deleted. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#deleteQuiz(Long)
   * @param quizId
   * @throws ServiceErrorException
   */
  public void deleteQuiz( Long quizId ) throws ServiceErrorException
  {
    Quiz quizToDelete = quizDAO.getQuizById( quizId );

    if ( quizToDelete != null )
    {
      if ( quizToDelete.isDeleteable() )
      {
        // the quiz is either in under construction or complete status, do a physical delete
        quizDAO.deleteQuiz( quizToDelete );
      }
      else if ( quizToDelete.getClaimFormStatusType().isUnderConstruction() )
      {
        // The quiz is at a status that we cannot delete
        throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DELETE_STATUS_ERR );
      }
      else
      {
        // The quiz is linked to only expired promotions
        throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DELETE_LINKED_ERR );
      }
    }
  }

  /**
   * Deletes a list of quizes. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#deleteQuizes(java.util.List)
   * @param quizIdList - List of quiz.id
   * @throws ServiceErrorException
   */
  public void deleteQuizes( List quizIdList ) throws ServiceErrorException
  {
    Iterator idIter = quizIdList.iterator();

    while ( idIter.hasNext() )
    {
      this.deleteQuiz( (Long)idIter.next() );
    }
  }

  /**
   * Get the quiz by Id. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#getQuizById(java.lang.Long)
   * @param id
   * @return Quiz
   */
  public Quiz getQuizById( Long id )
  {
    Quiz quiz = this.quizDAO.getQuizById( id );
    return quiz;
  }

  /**
   * Retrieves all Quizes from the database with proper associations.
   *
   * @param associationRequestCollection
   * @return Quiz
   */
  public Quiz getQuizByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return quizDAO.getQuizByIdWithAssociations( id, associationRequestCollection );
  }

  /**
   * Copy the quiz. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#copyQuiz(java.lang.Long, java.lang.String)
   * @param quizIdToCopy
   * @param newQuizName
   * @return Quiz
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public Quiz copyQuiz( Long quizIdToCopy, String newQuizName ) throws UniqueConstraintViolationException, ServiceErrorException
  {
    Quiz copiedQuiz;

    Quiz savedQuiz = quizDAO.getQuizById( quizIdToCopy );

    Hibernate.initialize( savedQuiz.getQuizQuestions() );
    for ( Iterator iter = savedQuiz.getQuizQuestions().iterator(); iter.hasNext(); )
    {
      QuizQuestion question = (QuizQuestion)iter.next();
      Hibernate.initialize( question.getQuizQuestionAnswers() );
    }

    Quiz quizCopy = (Quiz)savedQuiz.deepCopy( true, newQuizName );

    try
    {
      copiedQuiz = saveQuiz( quizCopy );
    }
    catch( ConstraintViolationException cve )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DUPLICATE_ERR, cve );
    }

    return copiedQuiz;
  }

  /**
   * Save the quiz. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#saveQuiz(com.biperf.core.domain.quiz.Quiz)
   * @param quiz
   * @return Quiz
   * @throws ServiceErrorException
   */
  public Quiz saveQuiz( Quiz quiz ) throws ServiceErrorException
  {
    Quiz dbQuiz;
    // --------------------------------------------------------------
    // Check to see if the Quiz already exists in the database
    // ---------------------------------------------------------------
    if ( quiz.getId() != null && quiz.getId().longValue() > 0 )
    {
      dbQuiz = this.quizDAO.getQuizById( quiz.getId() );
      if ( dbQuiz != null )
      {
        try
        {
          Quiz quizByName = this.quizDAO.getQuizByName( quiz.getName() );

          // if we found a record in the database with the given form Name,
          // but the ids are not equal, we are trying to update to a
          // formName that already exists so throw a Duplicate Exception
          if ( quizByName != null && quizByName.getId().compareTo( quiz.getId() ) != 0 )
          {
            throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DUPLICATE_ERR );
          }
          try
          {
            // if quiz is complete or assigned, make sure we have enough active questions
            if ( dbQuiz.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.ASSIGNED ) || dbQuiz.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.COMPLETED ) )
            {
              if ( quiz.getPassingScore() > dbQuiz.getActiveQuestions().size() )
              {
                throw new ServiceErrorException( "quiz.errors.NOT_ENOUGH_ACTIVE" );
              }
            }
            dbQuiz = this.quizDAO.updateQuiz( quiz );
          }
          catch( ConstraintViolationException cve )
          {
            throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DUPLICATE_ERR, cve );
          }
        }
        catch( ConstraintViolationException cve )
        {
          // this happens when we try to delete a question that has been answered already
          throw new ServiceErrorException( "quiz.errors.CANNOT_DELETE_ANSWERED_QUESTION", cve );
        }
      }
    }
    else
    {
      dbQuiz = this.quizDAO.getQuizByName( quiz.getName() );
      if ( dbQuiz != null )
      {
        // if we found a record in the database with the given formName,
        // and our claimFormToSave ID is null or ZERO (trying to add a new one),
        // we are trying to insert a duplicate record.
        if ( quiz.getId() == null || quiz.getId().longValue() == 0 )
        {
          throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DUPLICATE_ERR );
        }
      }

      try
      {
        dbQuiz = this.quizDAO.saveQuiz( quiz );
      }
      catch( ConstraintViolationException cve )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.QUIZ_DUPLICATE_ERR, cve );
      }
    }
    return dbQuiz;
  }

  /**
   * Save the quiz learning. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#saveQuizLearning(com.biperf.core.domain.quiz.QuizLearningObject)
   * @param quizLearning
   * @return QuizLearningObject
   * @throws ServiceErrorException
   */
  public QuizLearningObject saveQuizLearning( QuizLearningObject quizLearning ) throws ServiceErrorException
  {
    return this.quizDAO.saveQuizLearning( quizLearning );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#getNextActiveQuestion(java.util.List,
   *      java.lang.Long)
   * @param alreadySelectedQuestions
   */
  public QuizQuestion getNextActiveQuestion( List alreadySelectedQuestions, Long quizId )
  {
    QuizQuestion nextQuestion = null;

    Quiz quiz = quizDAO.getQuizById( quizId );

    QuizType quizType = quiz.getQuizType();
    if ( quizType.getCode().equals( QuizType.FIXED ) )
    {
      nextQuestion = getNextFixedActiveQuestion( alreadySelectedQuestions, quiz );
    }
    else if ( quizType.getCode().equals( QuizType.RANDOM ) )
    {
      nextQuestion = getRandomActiveQuestion( alreadySelectedQuestions, quiz );
    }
    else
    {
      throw new BeaconRuntimeException( "Unknown quiz type: " + quizType );
    }

    return nextQuestion;
  }

  private QuizQuestion getNextFixedActiveQuestion( List alreadySelectedQuestions, Quiz quiz )
  {
    QuizQuestion nextQuestion = null;

    // First get all required questions that are available
    List activeQuestions = quiz.getActiveQuestions();

    // Remove all already selected questions so selection is fresh.
    activeQuestions.removeAll( alreadySelectedQuestions );

    if ( activeQuestions.isEmpty() )
    {
      nextQuestion = null;
    }
    else
    {
      // get first question from filtered list
      nextQuestion = (QuizQuestion)activeQuestions.get( 0 );
    }

    return nextQuestion;
  }

  private QuizQuestion getRandomActiveQuestion( List alreadySelectedQuestions, Quiz quiz )
  {
    QuizQuestion randomQuestion = null;

    // First get all required questions that are available
    List requiredQuestions = quiz.getRequiredActiveQuestions();

    // Remove all already selected questions so selection is fresh.
    requiredQuestions.removeAll( alreadySelectedQuestions );

    if ( !requiredQuestions.isEmpty() )
    {
      randomQuestion = getRandomQuestion( requiredQuestions );
    }
    else
    {
      // Attempt to retrieve an optional question
      List optionalQuestions = quiz.getOptionalActiveQuestions();

      // Remove all already selected questions so selection is fresh.
      optionalQuestions.removeAll( alreadySelectedQuestions );

      if ( !optionalQuestions.isEmpty() )
      {
        randomQuestion = getRandomQuestion( optionalQuestions );
      }
      else
      {
        // No optional or random question available
        throw new BeaconRuntimeException( "Quiz promotion rule violated - total question list can't have less available questions than questions to be asked" );
      }
    }

    return randomQuestion;
  }

  /**
   * Return a random question from the questions list.
   */
  private QuizQuestion getRandomQuestion( List questions )
  {
    QuizQuestion randomQuestion;

    if ( questions.isEmpty() )
    {
      randomQuestion = null;
    }
    else if ( questions.size() == 1 )
    {
      randomQuestion = (QuizQuestion)questions.get( 0 );
    }
    else
    {
      Random random = new Random();
      randomQuestion = (QuizQuestion)questions.get( random.nextInt( questions.size() - 1 ) );
    }

    return randomQuestion;
  }

  /**
   * Reorder the questions on the quiz. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#reorderQuestion(java.lang.Long, java.lang.Long,
   *      int)
   * @param quizId
   * @param questionId
   * @param newIndex
   */
  public void reorderQuestion( Long quizId, Long questionId, int newIndex )
  {
    QuizQuestion question = null;
    Quiz quiz = null;

    question = quizDAO.getQuizQuestionById( questionId );

    quiz = quizDAO.getQuizById( quizId );

    quiz.getQuizQuestions().remove( question );

    if ( newIndex < 0 )
    {
      // add the question to the begining of the list
      newIndex = 0;
    }

    if ( newIndex < quiz.getQuizQuestions().size() )
    {
      quiz.getQuizQuestions().add( newIndex, question );
    }
    else
    {
      quiz.getQuizQuestions().add( question );
    }
  }

  /**
   * Delete the questions associated to the questionIds param. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#deleteQuizQuestions(Long, java.util.List)
   * @param questionIds
   */
  public void deleteQuizQuestions( Long quizId, List questionIds ) throws ServiceErrorException
  {
    Quiz quiz = getQuizById( quizId );

    Iterator quizQuestionIter = quiz.getQuizQuestions().iterator();
    while ( quizQuestionIter.hasNext() )
    {
      QuizQuestion quizQuestion = (QuizQuestion)quizQuestionIter.next();
      if ( questionIds.contains( quizQuestion.getId() ) )
      {
        quizQuestionIter.remove();
      }
    }
    // if quiz is completed or assigned, make sure we still have enough active questions
    if ( quiz.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.ASSIGNED ) || quiz.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.COMPLETED ) )
    {
      if ( quiz.getPassingScore() > quiz.getActiveQuestions().size() - 1 )
      {
        throw new ServiceErrorException( "quiz.errors.NOT_ENOUGH_ACTIVE" );
      }
    }
    saveQuiz( quiz );
  }

  /**
   * Delete the question answers associated to the questionAnswerIds param. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#deleteQuizQuestionAnswers(java.lang.Long,
   *      java.util.List)
   * @param questionId
   * @param questionAnswerIds
   */
  public void deleteQuizQuestionAnswers( Long questionId, List questionAnswerIds )
  {
    QuizQuestion quizQuestion = this.quizDAO.getQuizQuestionById( questionId );

    for ( int i = 0; i < questionAnswerIds.size(); i++ )
    {
      Long id = (Long)questionAnswerIds.get( i );
      Iterator questionAnswerIter = quizQuestion.getQuizQuestionAnswers().iterator();
      while ( questionAnswerIter.hasNext() )
      {
        QuizQuestionAnswer answer = (QuizQuestionAnswer)questionAnswerIter.next();
        if ( answer.getId().equals( id ) )
        {
          questionAnswerIter.remove();
        }
      }
    }
  }

  /**
   * Get the quiz question answers by quiz question answer id.
   *
   * @param id
   * @return QuizQuestionAnswer
   */
  public QuizQuestionAnswer getQuizQuestionAnswerById( Long id )
  {
    return this.quizDAO.getQuizQuestionAnswerById( id );
  }

  /**
   * Update the quiz question answer given quiz question answer
   *
   * @param quizQuestionAnswer
   * @return QuizQuestionAnswer
   */
  public QuizQuestionAnswer updateQuizQuestionAnswer( QuizQuestionAnswer quizQuestionAnswer )
  {
    return this.quizDAO.updateQuizQuestionAnswer( quizQuestionAnswer );
  }

  /**
   * Reorder the answers on the quiz. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#reorderAnswer(java.lang.Long, java.lang.Long,
   *      int)
   * @param questionId
   * @param answerId
   * @param newIndex
   */
  public void reorderAnswer( Long questionId, Long answerId, int newIndex )
  {
    QuizQuestionAnswer answer = null;
    QuizQuestion question = null;

    answer = quizDAO.getQuizQuestionAnswerById( answerId );

    question = quizDAO.getQuizQuestionById( questionId );

    question.getQuizQuestionAnswers().remove( answer );

    if ( newIndex < 0 )
    {
      newIndex = 0;
    }
    if ( newIndex < question.getQuizQuestionAnswers().size() )
    {
      question.getQuizQuestionAnswers().add( newIndex, answer );
    }
    else
    {
      question.getQuizQuestionAnswers().add( answer );
    }
  }

  /**
   * Get the quiz questions by quiz id.
   *
   * @param quizId
   * @return List
   */
  public List getQuizQuestionsByQuizId( Long quizId )
  {
    return this.quizDAO.getQuizQuestionsByQuizId( quizId );
  }

  /**
   * Get the quiz question by quiz question id w/ associations (quiz question answers)
   *
   * @param id
   * @param associationRequestCollection
   * @return QuizQuestion
   */
  public QuizQuestion getQuizQuestionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.quizDAO.getQuizQuestionByIdWithAssociations( id, associationRequestCollection );
  }

  /**
   * Gets a list of all completed quizzes. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#getAllCompletedAndAssignedQuizzes()
   * @return List
   */
  public List getAllCompletedAndAssignedQuizzes()
  {
    return this.quizDAO.getAllCompletedAndAssignedQuizzes();
  }

  /**
   * Save or Update the changes made to the QuizQuestion. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#saveQuizQuestion(java.lang.Long,com.biperf.core.domain.quiz.QuizQuestion,String)
   * @param quizId
   * @param managedQuizQuestion
   * @param questionText
   * @return QuizQuestion
   * @throws ServiceErrorException
   */
  public QuizQuestion saveQuizQuestion( Long quizId, QuizQuestion managedQuizQuestion, String questionText ) throws ServiceErrorException
  {
    Quiz quiz = this.quizDAO.getQuizById( quizId );

    QuizQuestion attachedQuestion = null;

    // Saving the quizQuestion could result in a unique constraint on the name within the
    // quizQuestion.
    try
    {
      // Check to see if this is a new or updated question
      if ( null != managedQuizQuestion.getId() && managedQuizQuestion.getId().longValue() != 0 )
      {

        // ---------------
        // update question
        // ---------------
        // CM Integration
        CMDataElement cmDataElement = new CMDataElement( Quiz.CM_QUESTION_NAME_KEY_DESC, Quiz.CM_QUESTION_NAME_KEY, questionText, false );

        cmAssetService.createOrUpdateAsset( Quiz.CM_QUESTION_SECTION, Quiz.CM_QUESTION_ASSET_TYPE, Quiz.CM_QUESTION_NAME_KEY_DESC, managedQuizQuestion.getCmAssetName(), cmDataElement );

        Iterator attachedQuestionIter = quiz.getQuizQuestions().iterator();
        while ( attachedQuestionIter.hasNext() )
        {
          attachedQuestion = (QuizQuestion)attachedQuestionIter.next();
          if ( attachedQuestion.getId().equals( managedQuizQuestion.getId() ) )
          {
            // if inactivating a previously active question and quiz is completed or assigned, make
            // sure we still have enough active questions
            if ( managedQuizQuestion.getStatusType().getCode().equals( QuizQuestionStatusType.INACTIVE ) && attachedQuestion.getStatusType().getCode().equals( QuizQuestionStatusType.ACTIVE )
                && ( quiz.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.ASSIGNED ) || quiz.getClaimFormStatusType().getCode().equals( ClaimFormStatusType.COMPLETED ) ) )
            {
              if ( quiz.getPassingScore() > quiz.getActiveQuestions().size() - 1 )
              {
                throw new ServiceErrorException( "quiz.errors.NOT_ENOUGH_ACTIVE" );
              }
            }
            attachedQuestion.setCmAssetName( managedQuizQuestion.getCmAssetName() );
            attachedQuestion.setRequired( managedQuizQuestion.isRequired() );
            attachedQuestion.setStatusType( managedQuizQuestion.getStatusType() );
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
        managedQuizQuestion.setCmAssetName( Quiz.CM_QUESTION_ASSET_PREFIX + cmKeyFragment );
        CMDataElement cmDataElement = new CMDataElement( Quiz.CM_QUESTION_NAME_KEY_DESC, Quiz.CM_QUESTION_NAME_KEY, questionText, false );

        cmAssetService.createOrUpdateAsset( Quiz.CM_QUESTION_SECTION, Quiz.CM_QUESTION_ASSET_TYPE, Quiz.CM_QUESTION_NAME_KEY_DESC, managedQuizQuestion.getCmAssetName(), cmDataElement );

        quiz.addQuizQuestion( managedQuizQuestion );
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

      throw new ServiceErrorException( "quiz.errors.DUPLICATE_QUESTION", e );
    }
    return managedQuizQuestion;

  }

  /**
   * Save or Update the changes made to the QuizQuestion. Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#saveQuizQuestionAnswer(java.lang.Long,com.biperf.core.domain.quiz.QuizQuestionAnswer,String,String)
   * @param quizQuestionId
   * @param managedQuizQuestionAnswer
   * @param questionAnswerText
   * @param questionAnswerExplanation
   * @return QuizQuestionAnswer
   * @throws ServiceErrorException
   */
  public QuizQuestionAnswer saveQuizQuestionAnswer( Long quizQuestionId, QuizQuestionAnswer managedQuizQuestionAnswer, String questionAnswerText, String questionAnswerExplanation )
      throws ServiceErrorException
  {
    QuizQuestion quizQuestion = this.quizDAO.getQuizQuestionById( quizQuestionId );

    QuizQuestionAnswer attachedQuestionAnswer = null;

    // Saving the quizQuestionAnswer could result in a unique constraint on the name within the
    // quizQuestionAnswer.
    try
    {
      // Check to see if this is a new or updated question answer
      if ( null != managedQuizQuestionAnswer.getId() && managedQuizQuestionAnswer.getId().longValue() != 0 )
      {
        Iterator attachedQuestionAnswerIter = quizQuestion.getQuizQuestionAnswers().iterator();
        while ( attachedQuestionAnswerIter.hasNext() )
        {
          attachedQuestionAnswer = (QuizQuestionAnswer)attachedQuestionAnswerIter.next();
          if ( attachedQuestionAnswer.getId().equals( managedQuizQuestionAnswer.getId() ) )
          {
            attachedQuestionAnswer.setCmAssetCode( managedQuizQuestionAnswer.getCmAssetCode() );
            attachedQuestionAnswer.setAnswerCmKey( managedQuizQuestionAnswer.getAnswerCmKey() );
            attachedQuestionAnswer.setExplanationCmKey( managedQuizQuestionAnswer.getExplanationCmKey() );
            attachedQuestionAnswer.setCorrect( managedQuizQuestionAnswer.isCorrect() );
            attachedQuestionAnswer.setQuizQuestion( quizQuestion );
          }
        }
      }
      else
      {
        saveAnswer( managedQuizQuestionAnswer, quizQuestion );

      }

      CMDataElement cmDataElementAnswer = new CMDataElement( Quiz.CM_QUESTION_ANSWER_KEY_DESC, Quiz.CM_QUESTION_ANSWER_KEY, questionAnswerText, false );

      CMDataElement cmDataElementExplanation = new CMDataElement( Quiz.CM_QUESTION_ANSWER_EXPLANATION_KEY_DESC, Quiz.CM_QUESTION_ANSWER_EXPLANATION_KEY, questionAnswerExplanation, false );
      List elementList = new ArrayList();
      elementList.add( cmDataElementAnswer );
      elementList.add( cmDataElementExplanation );

      cmAssetService.createOrUpdateAsset( Quiz.CM_QUESTION_ANSWER_SECTION,
                                          Quiz.CM_QUESTION_ANSWER_ASSET_TYPE,
                                          Quiz.CM_QUESTION_ANSWER_KEY_DESC,
                                          managedQuizQuestionAnswer.getCmAssetCode(),
                                          elementList );
    }
    catch( Exception e )
    {

      HibernateSessionManager.getSession().clear();
      throw new ServiceErrorException( "quiz.errors.MULTIPLE_TRUE_ANSWER", e );
    }

    return managedQuizQuestionAnswer;

  }

  /**
   * Check for any correct answer is save or not
   *
   * @see com.biperf.core.service.quiz.QuizServiceImpl#checkCorrectAns(QuizQuestion quizQuestion)
   * @param quizQuestion
   * @return boolean
   */
  private boolean checkCorrectAns( QuizQuestion quizQuestion )
  {
    QuizQuestionAnswer attachedQuestionAnswer = null;
    boolean correctAns = false;
    if ( !quizQuestion.getQuizQuestionAnswers().isEmpty() )
    {
      Iterator attachedQuestionAnswerIter = quizQuestion.getQuizQuestionAnswers().iterator();
      while ( attachedQuestionAnswerIter.hasNext() )
      {
        attachedQuestionAnswer = (QuizQuestionAnswer)attachedQuestionAnswerIter.next();
        if ( attachedQuestionAnswer.isCorrect() )
        {
          correctAns = true;
        }
      }
      return correctAns;
    }
    else
    {
      return correctAns;
    }
  }

  /**
  * save the answers after the appropriate condition check
  *
  * @see com.biperf.core.service.quiz.QuizServiceImpl#saveAnswer(managedQuizQuestionAnswer,quizQuestion)
  * @param managedQuizQuestionAnswer
  * @param quizQuestion
  * @throws ServiceErrorException
  */
  private void saveAnswer( QuizQuestionAnswer managedQuizQuestionAnswer, QuizQuestion quizQuestion ) throws ServiceErrorException
  {
    managedQuizQuestionAnswer.setCmAssetCode( cmAssetService.getUniqueAssetCode( Quiz.CM_QUESTION_ANSWER_ASSET_PREFIX ) );
    managedQuizQuestionAnswer.setAnswerCmKey( Quiz.CM_QUESTION_ANSWER_KEY );
    managedQuizQuestionAnswer.setExplanationCmKey( Quiz.CM_QUESTION_ANSWER_EXPLANATION_KEY );

    quizQuestion.addQuizQuestionAnswer( managedQuizQuestionAnswer );

  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.quiz.QuizService#updateQuizFormStatus(java.lang.Long)
   * @param quizFormId
   */
  public void updateQuizFormStatus( Long quizFormId )
  {
    Quiz quiz = getQuizById( quizFormId );

    if ( quiz.getPromotions().isEmpty() )
    {
      if ( quiz.isAssigned() )
      {
        quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
      }
    }
    else
    { // per Change Request dated 7/29, set status to completed if linked to only expired promotions
      boolean expiredOnly = true;
      for ( Iterator iter = quiz.getPromotions().iterator(); iter.hasNext(); )
      {
        Promotion promo = (Promotion)iter.next();
        if ( !promo.isExpired() )
        {
          // found a promotion that is not expired
          expiredOnly = false;
          break;
        }
      }
      if ( expiredOnly )
      {
        quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
      }
      else
      {
        quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ) );
      }
    }

  }

  public boolean moveFileToWebdav( String mediaUrl )
  {
    try
    {
      byte[] media = appDataDirFileUploadStrategy.getFileData( mediaUrl );
      webdavFileUploadStrategy.uploadFileData( mediaUrl, media );

      appDataDirFileUploadStrategy.delete( mediaUrl );
      return true;
    }
    catch( Throwable e )
    {
      // Must not have the file in AppDataDir of server executing this process
    }
    return false;
  }

  public QuizFileUploadValue uploadPhotoForQuizLibrary( QuizFileUploadValue data ) throws ServiceErrorException
  {

    if ( validFileData( data ) )
    {
      try
      {
        data.setFull( ImageUtils.getQuizDetailPath( data.getId(), data.getQuizFormName(), data.getName() ) );
        appDataDirFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );
        String fileExtension = ImageUtils.getFileExtension( data.getFull() );
        // BufferedImage thumb = ImageUtils.convertToBufferedImage(data.getData());
        ImageUtils imgInstance = new ImageUtils();
        BufferedImage thumb = imgInstance.readImage( data.getData() );
        int targetCropDimension = thumb.getHeight() < thumb.getWidth() ? thumb.getHeight() : thumb.getWidth();
        thumb = imageCropStrategy.process( thumb, targetCropDimension, targetCropDimension );
        thumb = imageResizeStrategy.process( thumb, QuizFileUploadValue.QUIZ_THUMB_DIMENSION, QuizFileUploadValue.QUIZ_THUMB_DIMENSION );
        data.setThumb( ImageUtils.getQuizThumbPath( data.getId(), data.getQuizFormName(), data.getName() ) );
        appDataDirFileUploadStrategy.uploadFileData( data.getThumb(), ImageUtils.convertToByteArray( thumb, fileExtension ) );

      }
      catch( Exception e )
      {
        logger.error( "Got error while uploading the image for quiz library:", e );
        throw new ServiceErrorException( "quiz.learning.IMAGE_UPLOAD_FAILED:" + e );
      }
    }
    else
    {
      throw new ServiceErrorException( "quiz.learning.IMAGE_UPLOAD_INVALID" );
    }
    return data;
  }

  public boolean validFileData( QuizFileUploadValue data )
  {
    // Check file size
    if ( QuizFileUploadValue.TYPE_PICTURE.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();
      double lowerSizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * .001;
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
      else if ( data.getSize() <= lowerSizeLimit )
      {
        return false;
      }

    }
    else if ( QuizFileUploadValue.TYPE_VIDEO.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_VIDEO_SIZE_LIMIT ).getIntVal();
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
    }
    else if ( QuizFileUploadValue.TYPE_PDF.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_PDF_SIZE_LIMIT ).getIntVal();
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
    }

    return true;
  }

  public QuizFileUploadValue uploadPdfForQuizLibrary( QuizFileUploadValue data ) throws ServiceErrorException
  {
    if ( data.getType().equalsIgnoreCase( "pdf" ) )
    {
      try
      {
        data.setFull( ImageUtils.getQuizDetailPath( data.getId(), data.getQuizFormName(), data.getName() ) );
        appDataDirFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );

        /*
         * BufferedImage thumb = ImageUtils.convertToBufferedImage(data.getData()); int
         * targetCropDimension =
         * (thumb.getHeight()<thumb.getWidth())?thumb.getHeight():thumb.getWidth(); thumb =
         * imageCropStrategy.process(thumb, targetCropDimension, targetCropDimension); thumb =
         * imageResizeStrategy.process(thumb, QuizFileUploadValue.QUIZ_THUMB_DIMENSION,
         * QuizFileUploadValue.QUIZ_THUMB_DIMENSION); data.setThumb(
         * ImageUtils.getQuizThumbPath(data.getId(),data.getQuizFormName(),data.getName()) );
         * appDataDirFileUploadStrategy.uploadFileData(data.getThumb(),
         * ImageUtils.convertToByteArray(thumb));
         */

      }
      catch( Exception e )
      {
        throw new ServiceErrorException( "quiz.learning.IMAGE_UPLOAD_FAILED:" + e );
      }
    }
    else
    {
      throw new ServiceErrorException( "quiz.learning.PDF_UPLOAD_INVALID" );
    }
    return data;
  }

  public QuizFileUploadValue uploadVideoForQuizLibrary( QuizFileUploadValue data ) throws ServiceErrorException
  {

    try
    {
      data.setFull( ImageUtils.getQuizDetailPath( data.getId(), data.getQuizFormName(), data.getName() ) );
      String filePath = data.getFull().substring( 0, data.getFull().lastIndexOf( "." ) );
      appDataDirFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );
      /*
       * appDataDirFileUploadStrategy.uploadFileData(filePath+".webm", data.getData());
       * appDataDirFileUploadStrategy.uploadFileData(filePath+".ogg", data.getData());
       * appDataDirFileUploadStrategy.uploadFileData(filePath+".mp4", data.getData());
       * appDataDirFileUploadStrategy.uploadFileData(filePath+".3gp", data.getData());
       */
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "quiz.learning.IMAGE_UPLOAD_FAILED:" + e );
    }

    return data;
  }

  public QuizLearningObject saveQuizLearningResources( QuizLearningObject quizLearning, String leftColumnHtml, String rightColumnHtml, String filePath ) throws ServiceErrorException
  {
    try
    {
      if ( StringUtils.isEmpty( quizLearning.getContentResourceCMCode() ) )
      {
        // Create and set asset to QuizLearningObject
        String newAssetName = cmAssetService.getUniqueAssetCode( QuizLearningObject.QUIZ_LEARNING_CMASSET_PREFIX );
        quizLearning.setContentResourceCMCode( newAssetName );
      }

      CMDataElement cmDataElementLeft = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME, QuizLearningObject.QUIZ_LEARNING_CMASSET_LEFT_KEY, leftColumnHtml, false, DataTypeEnum.HTML );
      CMDataElement cmDataElementRight = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                            QuizLearningObject.QUIZ_LEARNING_CMASSET_RIGHT_KEY,
                                                            rightColumnHtml,
                                                            false,
                                                            DataTypeEnum.HTML );
      CMDataElement cmDataElementFilePath = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                               QuizLearningObject.QUIZ_LEARNING_CMASSET_FILE_PATH_KEY,
                                                               filePath,
                                                               false,
                                                               DataTypeEnum.STRING );
      CMDataElement cmDataElementVideoMp4 = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME, QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_MP4_KEY, "", false, DataTypeEnum.HTML );
      CMDataElement cmDataElementVideoWebm = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME, QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_WEBM_KEY, "", false, DataTypeEnum.HTML );
      CMDataElement cmDataElementVideo3gp = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME, QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_3GP_KEY, "", false, DataTypeEnum.HTML );
      CMDataElement cmDataElementVideoOgg = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME, QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_OGG_KEY, "", false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElementLeft );
      elements.add( cmDataElementRight );
      elements.add( cmDataElementFilePath );
      elements.add( cmDataElementVideoMp4 );
      elements.add( cmDataElementVideoWebm );
      elements.add( cmDataElementVideo3gp );
      elements.add( cmDataElementVideoOgg );

      cmAssetService.createOrUpdateAsset( QuizLearningObject.QUIZ_SECTION_CODE,
                                          QuizLearningObject.QUIZ_LEARNING_CMASSET_TYPE_NAME,
                                          QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                          quizLearning.getContentResourceCMCode(),
                                          elements,
                                          ContentReaderManager.getCurrentLocale(),
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return quizLearning;
  }

  public QuizLearningObject saveQuizLearningVideo( QuizLearningObject quizLearning, String videoMp4Url, String videoWebmUrl, String video3gpUrl, String videoOggUrl, String rightColumnHtml )
      throws Exception

  {
    try
    {
      if ( StringUtils.isEmpty( quizLearning.getContentResourceCMCode() ) )
      {
        // Create and set asset to QuizLearningObject
        String newAssetName = cmAssetService.getUniqueAssetCode( QuizLearningObject.QUIZ_LEARNING_CMASSET_PREFIX );
        quizLearning.setContentResourceCMCode( newAssetName );
      }
      UploadResponse uploadResponse = getMtcService().uploadVideo( new URL( videoMp4Url ) );

      CMDataElement cmDataElementLeft = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                           QuizLearningObject.QUIZ_LEARNING_CMASSET_LEFT_KEY,
                                                           videoMp4Url + ActionConstants.REQUEST_ID + uploadResponse.getRequestId(),
                                                           false,
                                                           DataTypeEnum.HTML );
      CMDataElement cmDataElementRight = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                            QuizLearningObject.QUIZ_LEARNING_CMASSET_RIGHT_KEY,
                                                            rightColumnHtml,
                                                            false,
                                                            DataTypeEnum.HTML );
      // CMDataElement cmDataElementFilePath = new CMDataElement(
      // QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
      // QuizLearningObject.QUIZ_LEARNING_CMASSET_FILE_PATH_KEY, filePath, false,
      // DataTypeEnum.STRING );
      CMDataElement cmDataElementVideoMp4 = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                               QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_MP4_KEY,
                                                               videoMp4Url + ActionConstants.REQUEST_ID + uploadResponse.getRequestId(),
                                                               false,
                                                               DataTypeEnum.HTML );
      uploadResponse = getMtcService().uploadVideo( new URL( videoWebmUrl ) );
      CMDataElement cmDataElementVideoWebm = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                                QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_WEBM_KEY,
                                                                videoWebmUrl + ActionConstants.REQUEST_ID + uploadResponse.getRequestId(),
                                                                false,
                                                                DataTypeEnum.HTML );

      CMDataElement cmDataElementVideo3gp = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                               QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_3GP_KEY,
                                                               video3gpUrl,
                                                               false,
                                                               DataTypeEnum.HTML );

      CMDataElement cmDataElementVideoOgg = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                               QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_OGG_KEY,
                                                               videoOggUrl,
                                                               false,
                                                               DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElementLeft );
      elements.add( cmDataElementVideoMp4 );
      elements.add( cmDataElementRight );
      elements.add( cmDataElementVideoWebm );
      elements.add( cmDataElementVideo3gp );
      elements.add( cmDataElementVideoOgg );

      cmAssetService.createOrUpdateAsset( QuizLearningObject.QUIZ_SECTION_CODE,
                                          QuizLearningObject.QUIZ_LEARNING_CMASSET_TYPE_NAME,
                                          QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                          quizLearning.getContentResourceCMCode(),
                                          elements,
                                          ContentReaderManager.getCurrentLocale(),
                                          null );
    }
    catch( ServiceErrorException | MalformedURLException | JSONException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return quizLearning;
  }

  public List<QuizLearningSlideDetails> getQuizLearningObjectforQuizLibrary( Set learningObjects )
  {
    List<QuizLearningDetails> quizLearningDetails = new ArrayList<QuizLearningDetails>();
    List<QuizLearningSlideDetails> quizLearningSlideDetails = new ArrayList<QuizLearningSlideDetails>();
    QuizLearningDetails quizLearning = new QuizLearningDetails();
    quizLearningDetails.add( quizLearning );

    Iterator setItr = learningObjects.iterator();
    while ( setItr.hasNext() )
    {
      QuizLearningObject quizObj = (QuizLearningObject)setItr.next();
      if ( quizObj.getStatus().equalsIgnoreCase( QuizLearningObject.ACTIVE_STATUS ) )
      {
        String cmCode = quizObj.getContentResourceCMCode();
        quizLearningDetails = getContentFromCM( cmCode );
        java.util.Collections.sort( quizLearningDetails );
        QuizLearningSlideDetails quizSlideDetails = new QuizLearningSlideDetails();
        quizSlideDetails.setSlideNumber( quizObj.getSlideNumber() );
        quizSlideDetails.setDetailList( quizLearningDetails );
        quizSlideDetails.setQuizFormId( quizObj.getQuiz().getId() );
        quizLearningSlideDetails.add( quizSlideDetails );
      }
    }
    // code to get values from CM

    return quizLearningSlideDetails;
  }

  public List<QuizLearningDetails> getQuizLearningObjectforSlide( Set learningObjects, int slideNumber )
  {
    List<QuizLearningDetails> quizLearningDetails = new ArrayList<QuizLearningDetails>();
    List<QuizLearningSlideDetails> quizLearningSlideDetails = new ArrayList<QuizLearningSlideDetails>();

    Iterator setItr = learningObjects.iterator();
    while ( setItr.hasNext() )
    {
      QuizLearningObject quizObj = (QuizLearningObject)setItr.next();
      if ( quizObj.getSlideNumber() == slideNumber )
      {
        String cmCode = quizObj.getContentResourceCMCode();
        quizLearningDetails = getContentFromCM( cmCode );
        break;
      }
    }
    // code to get values from CM

    return quizLearningDetails;
  }

  public List getContentFromCM( String code )
  {
    List returnContent = new ArrayList();
    List contentList = new ArrayList();
    Content content = null;
    ContentReader contentReader = ContentReaderManager.getContentReader();
    if ( contentReader.getContent( code, Locale.ENGLISH ) instanceof java.util.List )
    {
      contentList = (List)contentReader.getContent( code, UserManager.getLocale() ); // bug 57398
      content = (Content)contentList.get( 0 );
    }
    else
    {
      content = (Content)contentReader.getContent( code, UserManager.getLocale() ); // bug 57398
    }

    Map m = content.getContentDataMapList();
    String leftColumn = "";
    String rightColumn = "";
    String filePath = "";
    String videoUrlMp4 = "";
    String videoUrlWebm = "";
    String videoUrl3gp = "";
    String videoUrlOgg = "";
    Translations leftObject = (Translations)m.get( "LEFT_COLUMN" );
    Translations rightObject = (Translations)m.get( "RIGHT_COLUMN" );
    Translations filePathObject = (Translations)m.get( "FILE_PATH" );
    Translations videoMp4Object = (Translations)m.get( "VIDEO_MP4_URL" );
    Translations videoWebmObject = (Translations)m.get( "VIDEO_WEBM_URL" );
    Translations video3gpObject = (Translations)m.get( "VIDEO_3GP_URL" );
    Translations videoOggObject = (Translations)m.get( "VIDEO_OGG_URL" );
    if ( leftObject != null )
    {

      leftColumn = leftObject.getValue();
      if ( leftColumn.contains( ActionConstants.REQUEST_ID ) )
      {

        MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( getRequestId( leftColumn ) );

        if ( Objects.nonNull( mtcVideo ) )
        {
          leftColumn = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();

        }
        else
        {
          leftColumn = getActualCardUrl( leftColumn );

        }

      }

    }
    if ( rightObject != null )
    {
      rightColumn = rightObject.getValue();
    }
    if ( filePathObject != null )
    {
      filePath = filePathObject.getValue();
    }
    if ( videoMp4Object != null )
    {
      videoUrlMp4 = videoMp4Object.getValue();
      if ( videoUrlMp4.contains( ActionConstants.REQUEST_ID ) )
      {

        MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( getRequestId( videoUrlMp4 ) );

        if ( Objects.nonNull( mtcVideo ) )
        {
          videoUrlMp4 = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();

        }
        else
        {
          videoUrlMp4 = getActualCardUrl( videoUrlMp4 );

        }

      }
    }
    if ( videoWebmObject != null )
    {
      videoUrlWebm = videoWebmObject.getValue();
      if ( videoUrlWebm.contains( ActionConstants.REQUEST_ID ) )
      {

        MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( getRequestId( videoUrlWebm ) );

        if ( Objects.nonNull( mtcVideo ) )
        {
          videoUrlWebm = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();

        }
        else
        {
          videoUrlWebm = getActualCardUrl( videoUrlWebm );

        }

      }
    }
    if ( video3gpObject != null )
    {
      videoUrl3gp = video3gpObject.getValue();
    }
    if ( videoOggObject != null )
    {
      videoUrlOgg = videoOggObject.getValue();
    }
    if ( !StringUtils.isEmpty( videoUrlMp4 ) )
    {
      leftColumn = getVideoHtmlString( videoUrlMp4, videoUrlWebm, videoUrl3gp, videoUrlOgg );
    }
    QuizLearningDetails quizLearn = new QuizLearningDetails();
    quizLearn.setLeftColumn( leftColumn );
    quizLearn.setRightColumn( rightColumn );
    quizLearn.setFilePath( filePath );
    quizLearn.setVideoUrlMp4( videoUrlMp4 );
    quizLearn.setVideoUrlWebm( videoUrlWebm );
    quizLearn.setVideoUrl3gp( videoUrl3gp );
    quizLearn.setVideoUrlOgg( videoUrlOgg );

    returnContent.add( quizLearn );

    return returnContent;
  }

  public String getVideoHtmlString( String videoUrlMp4, String videoUrlWebm, String videoUrl3gp, String videoUrlOgg )
  {
    StringBuilder videoHtml = new StringBuilder( "" );
    String globalUniqueId = String.valueOf( new Date().getTime() );

    if ( videoUrlMp4 != null && ( videoUrlMp4.indexOf( "http://" ) > -1 || videoUrlMp4.indexOf( "https://" ) > -1 ) && videoUrlMp4.indexOf( "-cm/cm3dam" ) < 0 )
    {
      // Note: If div id or class is changed for this then the code in DIYQuizPageView.java needs to
      // be changed to match the new value
      videoHtml.append( "<div id=\"PURLMainVideoWrapper\" class=\"PURLMainVideoWrapper\">" );
      videoHtml.append( "<a href=\"" + videoUrlMp4 + "\" target=\"_blank\">" + CmsResourceBundle.getCmsBundle().getString( "quiz.diy.form.VIDEO_LINK" ) + "</a>" );
      videoHtml.append( "</div>" );
    }
    else
    {
      videoHtml.append( "<div id=\"PURLMainVideoWrapper\" class=\"PURLMainVideoWrapper\">" );
      videoHtml.append( "<video id='example_video_1" + globalUniqueId + "' class='video-js vjs-default-skin'  controls width='250' preload='auto' data-setup='{}'>" );
      videoHtml.append( "<source type='video/mp4' src='" + videoUrlMp4 + "'/>" );
      videoHtml.append( "<source type='video/webm' src='" + videoUrlWebm + "'/>" );
      videoHtml.append( "<source type='video/ogg' src='" + videoUrlOgg + "'/>" );
      videoHtml.append( "<source type='video/3gp' src='" + videoUrl3gp + "'/>" );
      videoHtml.append( "</video>" );
      videoHtml.append( "<script>var myPlayer" + globalUniqueId + " = _V_('example_video_1" + globalUniqueId + "');</script>" );
      videoHtml.append( "</div>" );
    }
    return videoHtml.toString();
  }

  public int getNextSlideIdForQuiz( Long id )
  {
    return this.quizDAO.getNextSlideIdForQuiz( id );
  }

  public List<QuizLearningObject> getQuizLearningObjectById( Long id )
  {
    return this.quizDAO.getQuizLearningObjectById( id );
  }

  public QuizLearningObject getQuizLearningObjectForSlide( Long quizId, int slideNumber )
  {
    return this.quizDAO.getQuizLearningObjectForSlide( quizId, slideNumber );
  }

  public void setAppDataDirFileUploadStrategy( FileUploadStrategy appDataDirFileUploadStrategy )
  {
    this.appDataDirFileUploadStrategy = appDataDirFileUploadStrategy;
  }

  public void setWebdavFileUploadStrategy( FileUploadStrategy webdavFileUploadStrategy )
  {
    this.webdavFileUploadStrategy = webdavFileUploadStrategy;
  }

  public void setImageCropStrategy( ImageCropStrategy imageCropStrategy )
  {
    this.imageCropStrategy = imageCropStrategy;
  }

  public void setImageResizeStrategy( ImageResizeStrategy imageResizeStrategy )
  {
    this.imageResizeStrategy = imageResizeStrategy;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setAcceptableExtentions( List<String> acceptableExtentions )
  {
    this.acceptableExtentions = acceptableExtentions;
  }

  @Override
  public void mergeQuizLearning( QuizLearningObject quizObjs )
  {
    this.quizDAO.mergeQuizLearning( quizObjs );
  }

  public MTCService getMtcService()
  {
    return mtcService;
  }

  public void setMtcService( MTCService mtcService )
  {
    this.mtcService = mtcService;
  }

  public String getRequestId( String videoUrl )
  {

    return videoUrl.substring( videoUrl.lastIndexOf( ":" ) + 1 );
  }

  public String getActualCardUrl( String path )
  {
    return path.substring( 0, path.lastIndexOf( ActionConstants.REQUEST_ID ) );
  }
}
