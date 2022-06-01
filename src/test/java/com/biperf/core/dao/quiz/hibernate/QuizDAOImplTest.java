/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/quiz/hibernate/QuizDAOImplTest.java,v $
 */

package com.biperf.core.dao.quiz.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.quiz.QuizDAO;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.QuizQuestionStatusType;
import com.biperf.core.domain.enums.QuizType;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * QuizDAOImplTest.
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
 *
 */
public class QuizDAOImplTest extends BaseDAOTest
{

  /**
   * Tests getting a list of all completed quizzes.
   */
  public void testGetAllCompletedQuizzes()
  {

    // create a list of completed saved quizzes
    List expectedQuizList = new ArrayList();

    expectedQuizList.add( getSavedQuiz( "Q1" + buildUniqueString() ) );
    expectedQuizList.add( getSavedQuiz( "Q2" + buildUniqueString() ) );
    expectedQuizList.add( getSavedQuiz( "Q3" + buildUniqueString() ) );
    expectedQuizList.add( getSavedQuiz( "Q4" + buildUniqueString() ) );
    expectedQuizList.add( getSavedQuiz( "Q5" + buildUniqueString() ) );

    // get a list of all completed quizzes from the database
    List actualQuizList = this.getQuizDAO().getAllCompletedAndAssignedQuizzes();

    // assert the created list is contained within the list of ones fetched
    assertTrue( "List of completed quizzes wasn't in the actual list", actualQuizList.containsAll( expectedQuizList ) );
  }

  /**
   * Tests saving, gettingById and deleting a quiz.
   */
  public void testSaveGetByIdDeleteQuiz()
  {

    QuizDAO quizDAO = getQuizDAO();

    String uniqueString = getUniqueString();

    Quiz expectedQuiz = getSavedQuiz( uniqueString );

    Quiz saveQuiz = quizDAO.saveQuiz( expectedQuiz );

    assertEquals( "Actual Saved Quiz wasn't equals to what was expected.", expectedQuiz, saveQuiz );
    flushAndClearSession();

    Quiz getQuizById = quizDAO.getQuizById( saveQuiz.getId() );

    assertEquals( "Actual getQuizById Quiz wasn't equals to what was expected.", expectedQuiz, getQuizById );

    quizDAO.deleteQuiz( getQuizById );

  }

  /**
   * Test getting the quiz by the name.
   */
  public void testGetQuizByName()
  {
    QuizDAO quizDAO = getQuizDAO();

    String uniqueString = getUniqueString();

    Quiz expectedQuiz = getSavedQuiz( uniqueString );
    flushAndClearSession();

    Quiz getQuizByName = quizDAO.getQuizByName( expectedQuiz.getName() );

    assertEquals( "Actual getQuizByName isn't equals to what was expected.", expectedQuiz, getQuizByName );

  }

  /**
   * Test getting all quizzes.
   */
  public void testGetAll()
  {

    QuizDAO quizDAO = getQuizDAO();

    String uniqueString = getUniqueString();

    Quiz expectedQuiz1 = getSavedQuiz( "QUIZ1-" + uniqueString );
    Quiz expectedQuiz2 = getSavedQuiz( "QUIZ2-" + uniqueString );
    Quiz expectedQuiz3 = getSavedQuiz( "QUIZ3-" + uniqueString );
    Quiz expectedQuiz4 = getSavedQuiz( "QUIZ4-" + uniqueString );
    Quiz expectedQuiz5 = getSavedQuiz( "QUIZ5-" + uniqueString );

    List expectedQuizList = new ArrayList();
    expectedQuizList.add( expectedQuiz1 );
    expectedQuizList.add( expectedQuiz2 );
    expectedQuizList.add( expectedQuiz3 );
    expectedQuizList.add( expectedQuiz4 );
    expectedQuizList.add( expectedQuiz5 );

    flushAndClearSession();

    List allQuizzes = quizDAO.getAll();

    assertTrue( "AllQuizzes didn't contain the expectedQuizList", allQuizzes.containsAll( expectedQuizList ) );

  }

  /**
   * Test updating the quiz.
   */
  public void testUpdateQuiz()
  {

    String uniqueString = getUniqueString();
    String updatedQuizName = "UPDATED QUIZ NAME";

    Quiz expectedQuiz = getSavedQuiz( uniqueString );
    expectedQuiz.setName( updatedQuizName );

    Quiz updatedQuiz = getQuizDAO().updateQuiz( expectedQuiz );

    assertTrue( "UpdatedQuiz wasn't updated as expected.", updatedQuiz.getName().equals( updatedQuizName ) );

  }

  /**
   * Get the list of quizQuestions for a quizId.
   */
  public void testGetQuizQuestions()
  {

    Quiz savedQuiz = getSavedQuiz();

    List quizQuestionList = getQuizDAO().getQuizQuestionsByQuizId( savedQuiz.getId() );

    assertTrue( "List of quizQuestions wasn't what was expected.", quizQuestionList.containsAll( savedQuiz.getQuizQuestions() ) );

  }

  /**
   * Tests getting a quizQuestion by quizId.
   */
  public void testGetQuizQuestionById()
  {

    Quiz savedQuiz = getSavedQuiz();

    QuizQuestion quizQuestionFromSavedQuiz = (QuizQuestion)savedQuiz.getQuizQuestions().get( 0 );

    QuizQuestion getQuizQuestionById = getQuizDAO().getQuizQuestionById( quizQuestionFromSavedQuiz.getId() );

    assertEquals( "QuizQuestions weren't equals as expected.", quizQuestionFromSavedQuiz, getQuizQuestionById );

  }

  /**
   * Test getting a list of quizQuestionAnswers for the quizQuestionId they're associated to.
   */
  public void testGetQuizQuestionAnswersByQuestionId()
  {

    Quiz savedQuiz = getSavedQuiz();

    QuizQuestion quizQuestion = (QuizQuestion)savedQuiz.getQuizQuestions().get( 0 );
    flushAndClearSession();

    List expectedQuizQuestionAnswerList = quizQuestion.getQuizQuestionAnswers();

    List actualQuizQuestionAnswerList = getQuizDAO().getQuizQuestionAnswersByQuizQuestionId( quizQuestion.getId() );

    assertEquals( "Actual quizQuestionAnswer list wasn't equals to what was expected", expectedQuizQuestionAnswerList, actualQuizQuestionAnswerList );

  }

  /**
   * Test getting a QuizQuestionAnswer by id.
   */
  public void testGetQuizQuestionAnswerById()
  {

    Quiz savedQuiz = getSavedQuiz();
    flushAndClearSession();

    QuizQuestionAnswer expectedQuizQuestionAnswer = (QuizQuestionAnswer) ( (QuizQuestion)savedQuiz.getQuizQuestions().get( 0 ) ).getQuizQuestionAnswers().get( 0 );

    QuizQuestionAnswer actualQuizQuestionAnswer = getQuizDAO().getQuizQuestionAnswerById( expectedQuizQuestionAnswer.getId() );

    assertEquals( "Actual quizQuestionAnswer wasn't equals to expected.", expectedQuizQuestionAnswer, actualQuizQuestionAnswer );

  }

  /**
   * Test deleting the quizQuestioAnswer.
   */
  public void testDeleteQuizQuestionAnswer()
  {

    Quiz savedQuiz = getSavedQuiz();
    flushAndClearSession();

    QuizQuestionAnswer expectedQuizQuestionAnswer = (QuizQuestionAnswer) ( (QuizQuestion)savedQuiz.getQuizQuestions().get( 0 ) ).getQuizQuestionAnswers().get( 0 );

    QuizDAO quizDAO = getQuizDAO();

    quizDAO.deleteQuizQuestionAnswer( expectedQuizQuestionAnswer.getId() );
  }

  /**
   * Test deleting the quizQuestion.
   */
  public void testDeleteQuizQuestion()
  {

    Quiz savedQuiz = getSavedQuiz();
    flushAndClearSession();

    QuizQuestion expectedQuizQuestion = (QuizQuestion)savedQuiz.getQuizQuestions().get( 0 );

    QuizDAO quizDAO = getQuizDAO();

    quizDAO.deleteQuizQuestion( expectedQuizQuestion.getId() );
  }

  /**
   * Build a quiz with the uniqueString param and saves it for testing.
   * 
   * @param uniqueString
   * @return Quiz
   */
  private Quiz getSavedQuiz( String uniqueString )
  {
    return getQuizDAO().saveQuiz( buildCompletedQuiz( uniqueString ) );
  }

  /**
   * Build a quiz and saves it for testing.
   * 
   * @return Quiz
   */
  private Quiz getSavedQuiz()
  {
    return getSavedQuiz( getUniqueString() );
  }

  /**
   * Builds a static quiz for testing.
   * 
   * @param uniqueString
   * @return Quiz
   */
  public static Quiz buildCompletedQuiz( String uniqueString )
  {
    Quiz quiz = new Quiz();

    quiz.setName( uniqueString );
    quiz.setDescription( uniqueString + " - DESCRIPTION" );
    quiz.setQuizType( (QuizType)QuizType.getList().get( 0 ) );
    quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
    quiz.setNumberOfQuestionsAsked( 123 );
    quiz.setPassingScore( 12 );

    quiz.addQuizQuestion( QuizDAOImplTest.buildQuizQuestion( "1-Question-" + uniqueString ) );
    quiz.addQuizQuestion( QuizDAOImplTest.buildQuizQuestion( "2-Question-" + uniqueString ) );
    quiz.addQuizQuestion( QuizDAOImplTest.buildQuizQuestion( "3-Question-" + uniqueString ) );

    return quiz;
  }

  /**
   * Builds a static quizQuestion for testing.
   * 
   * @param uniqueString
   * @return QuizQuestion
   */
  public static QuizQuestion buildQuizQuestion( String uniqueString )
  {

    QuizQuestion quizQuestion = new QuizQuestion();

    quizQuestion.setCmAssetName( uniqueString + "-CMASSETNAME" );
    quizQuestion.setSequenceNum( 1 );
    quizQuestion.setRequired( true );
    quizQuestion.setStatusType( (QuizQuestionStatusType)QuizQuestionStatusType.getList().get( 0 ) );

    quizQuestion.addQuizQuestionAnswer( buildQuizQuestionAnswer( "QQA1-" + uniqueString ) );
    quizQuestion.addQuizQuestionAnswer( buildQuizQuestionAnswer( "QQA2-" + uniqueString ) );
    quizQuestion.addQuizQuestionAnswer( buildQuizQuestionAnswer( "QQA3-" + uniqueString ) );

    return quizQuestion;

  }

  /**
   * Builds a static quizQuestionAnswer for testing.
   * 
   * @param uniqueString
   * @return QuizQuestionAnswer
   */
  public static QuizQuestionAnswer buildQuizQuestionAnswer( String uniqueString )
  {

    QuizQuestionAnswer quizQuestionAnswer = new QuizQuestionAnswer();
    quizQuestionAnswer.setCmAssetCode( uniqueString + "-CMASSETNAMEANSWER" );
    quizQuestionAnswer.setAnswerCmKey( uniqueString + "-CMASSETMKEYTEXT" );
    quizQuestionAnswer.setExplanationCmKey( uniqueString + "-CMASSETMKEYEXPLAINATION" );

    if ( ( Math.random() % 1 ) == 0 )
    {
      quizQuestionAnswer.setCorrect( false );
    }
    else
    {
      quizQuestionAnswer.setCorrect( true );
    }

    return quizQuestionAnswer;
  }

  /**
   * Get the QuizDAO.
   * 
   * @return QuizDAO
   */
  private QuizDAO getQuizDAO()
  {
    return (QuizDAO)ApplicationContextFactory.getApplicationContext().getBean( "quizDAO" );
  }

}
