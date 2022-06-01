/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/quiz/QuizService.java,v $
 */

package com.biperf.core.service.quiz;

import java.util.List;
import java.util.Set;

import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.quiz.QuizLearningSlideDetails;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.QuizFileUploadValue;

/**
 * QuizService.
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
public interface QuizService extends SAO
{

  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "quizService";

  /**
   * Get the Quiz from the database by the id.
   * 
   * @param id
   * @return Quiz
   */
  public Quiz getQuizById( Long id );

  /**
   * Retrieves all the Quizes from the database.
   * 
   * @return List a list of Quizes
   */
  public List getAll();

  /**
   * Retrieves a Quiz from the database with proper associations.
   * 
   * @param id
   * @param associationRequestCollection
   * @return Quiz
   */
  public Quiz getQuizByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Copies a Quiz
   * 
   * @param quizIdToCopy
   * @param newQuizName
   * @return Quiz (The copied Quiz)
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public Quiz copyQuiz( Long quizIdToCopy, String newQuizName ) throws UniqueConstraintViolationException, ServiceErrorException;

  /**
   * Saves the quiz to the database.
   * 
   * @param quiz
   * @return Quiz
   * @throws ServiceErrorException
   */
  public Quiz saveQuiz( Quiz quiz ) throws ServiceErrorException;

  /**
   * Deletes a list of quizes.
   * 
   * @param quizIdList - List of quiz.id
   * @throws ServiceErrorException
   */
  public void deleteQuizes( List quizIdList ) throws ServiceErrorException;

  /**
   * Move the specified question to the newIndex and shift all other QuizQuestions down.
   * 
   * @param quizId
   * @param questionId
   * @param newIndex
   */
  public void reorderQuestion( Long quizId, Long questionId, int newIndex );

  /**
   * @param alreadySelectedQuestions
   */
  public QuizQuestion getNextActiveQuestion( List alreadySelectedQuestions, Long quizId );

  /**
   * Save or update quiz question.
   * 
   * @param quizId
   * @param managedQuizQuestion
   * @param questionText
   * @return QuizQuestion
   * @throws ServiceErrorException
   */
  public QuizQuestion saveQuizQuestion( Long quizId, QuizQuestion managedQuizQuestion, String questionText ) throws ServiceErrorException;

  /**
   * Save or update quiz question answer.
   * 
   * @param quizQuestionId
   * @param managedQuizQuestionAnswer
   * @param questionAnswerText
   * @param questionAnswerExplanation
   * @return QuizQuestionAnswer
   * @throws ServiceErrorException
   */
  public QuizQuestionAnswer saveQuizQuestionAnswer( Long quizQuestionId, QuizQuestionAnswer managedQuizQuestionAnswer, String questionAnswerText, String questionAnswerExplanation )
      throws ServiceErrorException;

  /**
   * Delete a list of quizQuestionIds for this form.
   * 
   * @param quizId
   * @param questionIds
   */
  public void deleteQuizQuestions( Long quizId, List questionIds ) throws ServiceErrorException;

  /**
   * Deletes a quiz if it is under_construction or complete. If the quiz is assigned it cannot be
   * deleted. Overridden from
   * 
   * @see com.biperf.core.service.quiz.QuizService#deleteQuiz(Long)
   * @param quizId
   * @throws ServiceErrorException
   */
  public void deleteQuiz( Long quizId ) throws ServiceErrorException;

  /**
   * Delete the question answers associated to the questionAnswerIds param.
   * 
   * @param questionAnswerIds
   */
  public void deleteQuizQuestionAnswers( Long quizId, List questionAnswerIds );

  /**
   * Get the quiz question answers by quiz question answer id.
   * 
   * @param id
   * @return QuizQuestionAnswer
   */
  public QuizQuestionAnswer getQuizQuestionAnswerById( Long id );

  /**
   * Move the specified answer to the newIndex and shift all other QuizQuestionAnswers down.
   * 
   * @param questionId
   * @param answerId
   * @param newIndex
   */
  public void reorderAnswer( Long questionId, Long answerId, int newIndex );

  /**
   * Update the quiz question answer given quiz question answer
   * 
   * @param quizQuestionAnswer
   * @return QuizQuestionAnswer
   */
  public QuizQuestionAnswer updateQuizQuestionAnswer( QuizQuestionAnswer quizQuestionAnswer );

  /**
   * Get the quiz questions by quiz id.
   * 
   * @param quizId
   * @return List
   */
  public List getQuizQuestionsByQuizId( Long quizId );

  /**
   * Get the quiz question by quiz question id w/ associations (quiz question answers)
   * 
   * @param id
   * @param associationRequestCollection
   * @return QuizQuestion
   */
  public QuizQuestion getQuizQuestionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Gets a list of all quizzes which are completed
   * 
   * @return list
   */
  public List getAllCompletedAndAssignedQuizzes();

  /**
   * Updates the status of a quiz
   * 
   * @param quizId
   */
  public void updateQuizFormStatus( Long quizId );

  public boolean moveFileToWebdav( String mediaUrl );

  public QuizFileUploadValue uploadPhotoForQuizLibrary( QuizFileUploadValue data ) throws ServiceErrorException;

  /**
   * Saves the quiz learning object to the database.
   * 
   * @param QuizLearningObject
   * @return QuizLearningObject
   * @throws ServiceErrorException
   */
  public QuizLearningObject saveQuizLearning( QuizLearningObject quizLearning ) throws ServiceErrorException;

  public QuizLearningObject saveQuizLearningResources( QuizLearningObject quizLearning, String leftColumnHtml, String rightColumnHtml, String filePath ) throws ServiceErrorException;

  public QuizLearningObject saveQuizLearningVideo( QuizLearningObject quizLearning, String videoMp4Url, String videoWebmUrl, String video3gpUrl, String videoOggUrl, String rightColumnHtml )
      throws ServiceErrorException, Exception;

  /**
   * Get the quiz learning objects by quiz question answer id.
   * 
   * @param id
   * @return List<QuizLearningObject>
   */
  public List<QuizLearningObject> getQuizLearningObjectById( Long id );

  public List<QuizLearningSlideDetails> getQuizLearningObjectforQuizLibrary( Set learningObjects );

  public int getNextSlideIdForQuiz( Long id );

  public QuizFileUploadValue uploadPdfForQuizLibrary( QuizFileUploadValue data ) throws ServiceErrorException;

  public QuizFileUploadValue uploadVideoForQuizLibrary( QuizFileUploadValue data ) throws ServiceErrorException;

  public boolean validFileData( QuizFileUploadValue data );

  public List<QuizLearningDetails> getQuizLearningObjectforSlide( Set learningObjects, int slideNumber );

  public QuizLearningObject getQuizLearningObjectForSlide( Long quizId, int slideNumber );

  public void mergeQuizLearning( QuizLearningObject quizObjs );
}
