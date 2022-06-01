/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/quiz/QuizDAO.java,v $
 */

package com.biperf.core.dao.quiz;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.service.AssociationRequestCollection;

/**
 * QuizDAO.
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
 * <td>Oct 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface QuizDAO extends DAO
{

  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "quizDAO";

  /**
   * Gets a quiz from the database by id.
   * 
   * @param quizId
   * @return Quiz
   */
  public Quiz getQuizById( Long quizId );

  /**
   * Saves a quiz.
   * 
   * @param quiz
   * @return Quiz
   */
  public Quiz saveQuiz( Quiz quiz );

  /**
   * Updates a quiz checking for name uniqueness.
   * 
   * @param quiz
   * @return Quiz
   * @throws ConstraintViolationException
   */
  public Quiz updateQuiz( Quiz quiz ) throws ConstraintViolationException;

  /**
   * Delete the Quiz.
   * 
   * @param quiz
   */
  public void deleteQuiz( Quiz quiz );

  /**
   * Gets a quiz from the database by name.
   * 
   * @param name
   * @return Quiz
   */
  public Quiz getQuizByName( String name );

  /**
   * Get all Quizzes from the database.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Get a Quiz from the database with proper associations.
   * 
   * @param id
   * @param associationRequestCollection
   * @return Quiz
   */
  public Quiz getQuizByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the quizQuestion by its id.
   * 
   * @param quizQuestionId
   * @return QuizQuestion
   */
  public QuizQuestion getQuizQuestionById( Long quizQuestionId );

  /**
   * Get all quizQuestions by the quiz id.
   * 
   * @param quizId
   * @return List
   */
  public List getQuizQuestionsByQuizId( Long quizId );

  /**
   * Get the quizQuestionAnswer by the id param.
   * 
   * @param quizQuestionAnswerId
   * @return QuizQuestionAnswer
   */
  public QuizQuestionAnswer getQuizQuestionAnswerById( Long quizQuestionAnswerId );

  /**
   * Get a list of quizQuestionAnswers by the QuizQuestion id to which they're associated.
   * 
   * @param quizQuestionId
   * @return List
   */
  public List getQuizQuestionAnswersByQuizQuestionId( Long quizQuestionId );

  /**
   * Delete the quizQuestionAnswer.
   * 
   * @param quizQuestionAnswerId
   */
  public void deleteQuizQuestionAnswer( Long quizQuestionAnswerId );

  /**
   * Delete the quizQuestion.
   * 
   * @param quizQuestionId
   */
  public void deleteQuizQuestion( Long quizQuestionId );

  /**
   * Update the quizQuestionAnswer.
   * 
   * @param quizQuestionAnswer
   * @return QuizQuestionAnswer
   */
  public QuizQuestionAnswer updateQuizQuestionAnswer( QuizQuestionAnswer quizQuestionAnswer );

  public QuizQuestion getQuizQuestionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Gets a list of all completed and assigned quizzes.
   * 
   * @return List
   */
  public List getAllCompletedAndAssignedQuizzes();

  public QuizLearningObject saveQuizLearning( QuizLearningObject quizLearning );

  public List<QuizLearningObject> getQuizLearningObjectById( Long id );

  public int getNextSlideIdForQuiz( Long id );

  public QuizLearningObject getQuizLearningObjectForSlide( Long quizId, int slideNumber );

  public void mergeQuizLearning( QuizLearningObject quizObjs );
}
