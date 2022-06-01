/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/quiz/impl/QuizServiceImplTest.java,v $
 */

package com.biperf.core.service.quiz.impl;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.jmock.core.Constraint;

import com.biperf.core.dao.quiz.QuizDAO;
import com.biperf.core.dao.quiz.hibernate.QuizDAOImplTest;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.QuizQuestionStatusType;
import com.biperf.core.domain.enums.QuizType;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.quiz.QuizQuestionAssociationRequest;

/**
 * QuizServiceImplTest.
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
 * <td>Oct 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizServiceImplTest extends BaseServiceTest
{
  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public QuizServiceImplTest( String test )
  {
    super( test );
  }

  /** QuizService */
  private QuizServiceImpl quizService = new QuizServiceImpl();

  /** mockQuizDAO */
  private Mock mockQuizDAO = null;
  private Mock mockCmAssetService = null;
  private CMAssetService cmAssetServiceProxy = null;

  /**
   * Test get all completed quizzes.
   */
  public void testGetAllCompleteQuizzes()
  {

    List expectedCompletedQuizzes = new ArrayList();

    expectedCompletedQuizzes.add( buildCompleteRandomQuiz() );
    expectedCompletedQuizzes.add( buildCompleteRandomQuiz() );
    expectedCompletedQuizzes.add( buildCompleteRandomQuiz() );
    expectedCompletedQuizzes.add( buildCompleteRandomQuiz() );

    mockQuizDAO.expects( once() ).method( "getAllCompletedAndAssignedQuizzes" ).will( returnValue( expectedCompletedQuizzes ) );

    List actualCompletedQuizzes = this.quizService.getAllCompletedAndAssignedQuizzes();

    assertTrue( "List of actualQuizzes didn't contain expected quizzes.", actualCompletedQuizzes.containsAll( expectedCompletedQuizzes ) );
  }

  public void testGetNextActiveQuestionRandomQuiz()
  {
    Quiz randomQuiz = buildRandomQuiz();
    String uniqueString = getUniqueString();
    randomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Qi1-" + uniqueString, true, QuizQuestionStatusType.INACTIVE ) );
    randomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Qi2-" + uniqueString, false, QuizQuestionStatusType.INACTIVE ) );
    randomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Qi3-" + uniqueString, false, QuizQuestionStatusType.INACTIVE ) );
    randomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Qi4-" + uniqueString, false, QuizQuestionStatusType.INACTIVE ) );
    randomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Q1-" + uniqueString, true, QuizQuestionStatusType.ACTIVE ) );
    randomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Q2-" + uniqueString, true, QuizQuestionStatusType.ACTIVE ) );
    randomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Q3-" + uniqueString, false, QuizQuestionStatusType.ACTIVE ) );
    randomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Q4-" + uniqueString, false, QuizQuestionStatusType.ACTIVE ) );
    randomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Q5-" + uniqueString, false, QuizQuestionStatusType.ACTIVE ) );
    Long quizId = null;
    ArrayList selectedQuestions = new ArrayList();

    QuizQuestion nextActiveQuestion;

    mockQuizDAO.expects( this.atLeastOnce() ).method( "getQuizById" ).will( returnValue( randomQuiz ) );

    // //Fetch next 4 active questions - First two return must be required, next two must be
    // optional
    // Q1
    nextActiveQuestion = quizService.getNextActiveQuestion( selectedQuestions, quizId );
    assertTrue( nextActiveQuestion.isActive() );
    assertTrue( nextActiveQuestion.isRequired() );
    assertFalse( selectedQuestions.contains( nextActiveQuestion ) );
    selectedQuestions.add( nextActiveQuestion );

    // Q2
    nextActiveQuestion = quizService.getNextActiveQuestion( selectedQuestions, quizId );
    assertTrue( nextActiveQuestion.isActive() );
    assertTrue( nextActiveQuestion.isRequired() );
    assertFalse( selectedQuestions.contains( nextActiveQuestion ) );
    selectedQuestions.add( nextActiveQuestion );

    // Q3
    nextActiveQuestion = quizService.getNextActiveQuestion( selectedQuestions, quizId );
    assertTrue( nextActiveQuestion.isActive() );
    assertTrue( !nextActiveQuestion.isRequired() );
    assertFalse( selectedQuestions.contains( nextActiveQuestion ) );
    selectedQuestions.add( nextActiveQuestion );

    // Q4
    nextActiveQuestion = quizService.getNextActiveQuestion( selectedQuestions, quizId );
    assertTrue( nextActiveQuestion.isActive() );
    assertTrue( !nextActiveQuestion.isRequired() );
    assertFalse( selectedQuestions.contains( nextActiveQuestion ) );

  }

  public void testGetNextActiveQuestionFixedQuiz()
  {
    Quiz fixedQuiz = buildRandomQuiz();
    fixedQuiz.setQuizType( QuizType.lookup( QuizType.FIXED ) );

    String uniqueString = getUniqueString();
    fixedQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Qi1-" + uniqueString, true, QuizQuestionStatusType.INACTIVE ) );
    fixedQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Qi3-" + uniqueString, false, QuizQuestionStatusType.INACTIVE ) );

    QuizQuestion question1 = buildCompleteQuizQuestion( "Q1-" + uniqueString, true, QuizQuestionStatusType.ACTIVE );
    fixedQuiz.addQuizQuestion( question1 );

    fixedQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Qi2-" + uniqueString, false, QuizQuestionStatusType.INACTIVE ) );
    QuizQuestion question2 = buildCompleteQuizQuestion( "Q2-" + uniqueString, true, QuizQuestionStatusType.ACTIVE );
    fixedQuiz.addQuizQuestion( question2 );

    fixedQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Qi4-" + uniqueString, false, QuizQuestionStatusType.INACTIVE ) );

    QuizQuestion question3 = buildCompleteQuizQuestion( "Q3-" + uniqueString, true, QuizQuestionStatusType.ACTIVE );
    fixedQuiz.addQuizQuestion( question3 );

    Long quizId = null;
    ArrayList selectedQuestions = new ArrayList();

    QuizQuestion nextActiveQuestion;

    mockQuizDAO.expects( this.atLeastOnce() ).method( "getQuizById" ).will( returnValue( fixedQuiz ) );

    nextActiveQuestion = quizService.getNextActiveQuestion( selectedQuestions, quizId );
    assertEquals( question1, nextActiveQuestion );
    selectedQuestions.add( nextActiveQuestion );

    nextActiveQuestion = quizService.getNextActiveQuestion( selectedQuestions, quizId );
    assertEquals( question2, nextActiveQuestion );
    selectedQuestions.add( nextActiveQuestion );

    nextActiveQuestion = quizService.getNextActiveQuestion( selectedQuestions, quizId );
    assertEquals( question3, nextActiveQuestion );
    selectedQuestions.add( nextActiveQuestion );
  }

  /**
   * Tests getting all of the Quizes
   */
  public void testGetAll()
  {
    List quizList = new ArrayList();
    quizList.add( new Quiz() );
    quizList.add( new Quiz() );
    mockQuizDAO.expects( once() ).method( "getAll" ).will( returnValue( quizList ) );

    List returnedQuizList = quizService.getAll();
    assertTrue( returnedQuizList.size() == 2 );
  } // end testGetAll

  /**
   * Test getting the Quiz by id.
   */
  public void testGetQuizById()
  {
    // Get the test Quiz.
    Quiz quiz = new Quiz();
    quiz.setId( new Long( 1 ) );
    quiz.setName( "A Test Quiz" );
    quiz.setDescription( "Description of the test claim form" );
    quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    quiz.setPassingScore( 10 );
    quiz.setQuizType( QuizType.lookup( QuizType.FIXED ) );

    // QuizDAO expected to call getClaimFormById once with the ClaimFormId which will return
    // the Quiz expected
    mockQuizDAO.expects( once() ).method( "getQuizById" ).with( same( quiz.getId() ) ).will( returnValue( quiz ) );

    Quiz actualQuiz = quizService.getQuizById( quiz.getId() );
    assertEquals( "Actual quiz wasn't equal to what was expected", quiz, actualQuiz );

    mockQuizDAO.verify();
  } // end testGetQuizById

  /**
   * Test adding the Quiz through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveQuizInsert() throws ServiceErrorException
  {
    // Create the test Quiz.
    Quiz quiz = new Quiz();
    quiz.setName( "A Test Form" );
    quiz.setDescription( "Description of the test claim form" );
    quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    quiz.setPassingScore( 10 );
    quiz.setQuizType( QuizType.lookup( QuizType.FIXED ) );

    Quiz savedQuiz = new Quiz();
    savedQuiz.setId( new Long( 1 ) );
    savedQuiz.setName( "A Test Form" );
    savedQuiz.setDescription( "Description of the test claim form" );
    savedQuiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    savedQuiz.setPassingScore( 10 );
    savedQuiz.setQuizType( QuizType.lookup( QuizType.FIXED ) );

    // find the quiz by Name
    mockQuizDAO.expects( once() ).method( "getQuizByName" ).with( same( quiz.getName() ) ).will( returnValue( null ) );

    // insert a new quiz
    mockQuizDAO.expects( once() ).method( "saveQuiz" ).with( same( quiz ) ).will( returnValue( savedQuiz ) );

    // test Adding with the QuizService.saveQuiz
    Quiz returnedQuiz = quizService.saveQuiz( quiz );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    assertEquals( "Actual returned Quiz wasn't equal to what was expected", savedQuiz, returnedQuiz );
    mockQuizDAO.verify();
  } // end testSaveQuizInsert

  /**
   * Test adding, then updating the Quiz through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveQuizUpdate() throws ServiceErrorException
  {
    // Create the test Quiz.
    Quiz quiz = new Quiz();
    quiz.setName( "A Test Form" );
    quiz.setDescription( "Description of the test quiz" );
    quiz.setQuizType( QuizType.lookup( QuizType.FIXED ) );
    quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    quiz.setPassingScore( 10 );

    Quiz savedQuiz = new Quiz();
    savedQuiz.setId( new Long( 1 ) );
    savedQuiz.setName( "A Test Form" );
    savedQuiz.setDescription( "Description of the test quiz" );
    savedQuiz.setQuizType( QuizType.lookup( QuizType.FIXED ) );
    savedQuiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    savedQuiz.setPassingScore( 10 );

    // find the quiz by Name
    mockQuizDAO.expects( once() ).method( "getQuizByName" ).with( same( quiz.getName() ) ).will( returnValue( null ) );

    // insert a new quiz
    mockQuizDAO.expects( once() ).method( "saveQuiz" ).with( same( quiz ) ).will( returnValue( savedQuiz ) );

    // test Adding the QuizService.saveQuiz
    Quiz returnedQuiz = quizService.saveQuiz( quiz );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    assertEquals( "Actual returned Quiz wasn't equal to what was expected", savedQuiz, returnedQuiz );
    mockQuizDAO.verify();

    // ---------------------
    // Update the Quiz
    // ---------------------
    returnedQuiz.setName( "Updated Quiz Name" );

    // find the quiz by Id
    mockQuizDAO.expects( once() ).method( "getQuizById" ).with( same( returnedQuiz.getId() ) ).will( returnValue( returnedQuiz ) );

    // find the quiz by Name
    mockQuizDAO.expects( once() ).method( "getQuizByName" ).with( same( returnedQuiz.getName() ) ).will( returnValue( null ) );

    // update a new quiz
    mockQuizDAO.expects( once() ).method( "updateQuiz" ).with( same( returnedQuiz ) ).will( returnValue( returnedQuiz ) );

    Quiz returnedUpdatedQuiz = quizService.saveQuiz( returnedQuiz );
    assertEquals( "Actual returned Quiz wasn't equal to what was expected", returnedUpdatedQuiz, returnedQuiz );
    mockQuizDAO.verify();

  } // end testSaveQuizUpdate

  /**
   * Test deleting the Quiz through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testDeleteQuiz() throws ServiceErrorException
  {
    // Create the test Quiz.
    Quiz quiz = new Quiz();
    quiz.setName( "A Test Form" );
    quiz.setDescription( "Description of the test quiz" );
    quiz.setQuizType( QuizType.lookup( QuizType.FIXED ) );
    quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    quiz.setPassingScore( 10 );

    mockQuizDAO.expects( once() ).method( "getQuizById" ).with( same( quiz.getId() ) ).will( returnValue( quiz ) );

    mockQuizDAO.expects( once() ).method( "deleteQuiz" ).with( same( quiz ) );

    // test the QuizService.deleteQuiz
    quizService.deleteQuiz( quiz.getId() );

    // Verify the mockDAO deletes.
    mockQuizDAO.verify();
  }

  /**
   * Tests deleting multiple Quizes
   * 
   * @throws ServiceErrorException
   */
  public void testDeleteQuizes() throws ServiceErrorException
  {
    List quizIdDeleteList = new ArrayList();

    // Create the test Quiz.
    Quiz quiz = new Quiz();
    quiz.setId( new Long( 1 ) );
    quiz.setName( "A Test Quiz" );
    quiz.setDescription( "Description of the test quiz" );
    quiz.setQuizType( QuizType.lookup( QuizType.FIXED ) );
    quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    quiz.setPassingScore( 10 );
    quizIdDeleteList.add( quiz.getId() );

    // Create a second test Quiz.
    Quiz quiz2 = new Quiz();
    quiz2.setId( new Long( 2 ) );
    quiz2.setName( "A Second Test Quiz" );
    quiz2.setDescription( "Description of the second test quiz" );
    quiz2.setQuizType( QuizType.lookup( QuizType.FIXED ) );
    quiz2.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    quiz2.setPassingScore( 5 );
    quizIdDeleteList.add( quiz2.getId() );

    mockQuizDAO.expects( once() ).method( "getQuizById" ).with( same( quiz.getId() ) ).will( returnValue( quiz ) );
    mockQuizDAO.expects( once() ).method( "deleteQuiz" ).with( same( quiz ) );

    mockQuizDAO.expects( once() ).method( "getQuizById" ).with( same( quiz2.getId() ) ).will( returnValue( quiz2 ) );
    mockQuizDAO.expects( once() ).method( "deleteQuiz" ).with( same( quiz2 ) );

    quizService.deleteQuizes( quizIdDeleteList );

    mockQuizDAO.verify();
  } // end testDeleteQuizes

  /**
   * Tests deleting the questions associated to a quiz.
   */
  public void testDeleteQuizQuestions() throws Exception
  {
    List questionsToDelete = new ArrayList();
    Quiz quiz = this.buildCompleteRandomQuiz();

    QuizQuestion question = this.buildQuizQuestion( "sdasdas" );
    QuizQuestion question2 = this.buildQuizQuestion( "sdq334e3dasdas" );
    QuizQuestion question3 = this.buildQuizQuestion( "55g4tvvfv" );

    quiz.addQuizQuestion( question );
    quiz.addQuizQuestion( question2 );
    quiz.addQuizQuestion( question3 );

    questionsToDelete.add( question.getId() );
    questionsToDelete.add( question2.getId() );

    mockQuizDAO.expects( once() ).method( "getQuizById" ).with( same( quiz.getId() ) ).will( returnValue( quiz ) );

    // find the quiz by Id
    mockQuizDAO.expects( once() ).method( "getQuizById" ).with( same( quiz.getId() ) ).will( returnValue( quiz ) );

    // find the quiz by Name
    mockQuizDAO.expects( once() ).method( "getQuizByName" ).with( same( quiz.getName() ) ).will( returnValue( null ) );

    // update a new quiz
    mockQuizDAO.expects( once() ).method( "updateQuiz" ).with( same( quiz ) ).will( returnValue( quiz ) );

    quizService.deleteQuizQuestions( quiz.getId(), questionsToDelete );

  }

  /**
   * Test reorderQuizQuestion
   */
  public void testReorderQuizQuestion()
  {
    int reorderToPosition = 2;

    Quiz quiz = QuizDAOImplTest.buildCompletedQuiz( "Test Quiz" );
    quiz.setId( new Long( 1 ) );
    QuizQuestion questionToReorder = (QuizQuestion)quiz.getQuizQuestions().get( 0 );
    questionToReorder.setId( new Long( 2 ) );
    ( (QuizQuestion)quiz.getQuizQuestions().get( 1 ) ).setId( new Long( 3 ) );
    ( (QuizQuestion)quiz.getQuizQuestions().get( 2 ) ).setId( new Long( 4 ) );

    mockQuizDAO.expects( once() ).method( "getQuizQuestionById" ).with( same( questionToReorder.getId() ) ).will( returnValue( questionToReorder ) );

    mockQuizDAO.expects( once() ).method( "getQuizById" ).with( same( quiz.getId() ) ).will( returnValue( quiz ) );

    quizService.reorderQuestion( quiz.getId(), questionToReorder.getId(), reorderToPosition );

    assertEquals( ( (QuizQuestion)quiz.getQuizQuestions().get( reorderToPosition ) ).getId(), questionToReorder.getId() );
  }

  /**
   * Test the copyQuiz method.
   * 
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public void testCopyQuiz() throws UniqueConstraintViolationException, ServiceErrorException
  {
    // create a Quiz
    Quiz quizToCopy = buildCompletedQuiz( "Test Quiz" );
    Quiz expectedQuiz = buildCompletedQuiz( "Copied Quiz" );

    Quiz copiedQuiz = null;

    mockQuizDAO.expects( once() ).method( "getQuizById" ).with( same( quizToCopy.getId() ) ).will( returnValue( quizToCopy ) );

    mockQuizDAO.expects( once() ).method( "getQuizByName" ).with( same( expectedQuiz.getName() ) ).will( returnValue( null ) );

    mockQuizDAO.expects( once() ).method( "saveQuiz" ).will( returnValue( expectedQuiz ) );
    
    mockCmAssetService.expects( atLeastOnce() ).method( "getUniqueAssetCode" ).with( same( Quiz.CM_QUESTION_ASSET_PREFIX ) ).will( returnValue( "quizquestionasset" ) );
    mockCmAssetService.expects( atLeastOnce() ).method( "getUniqueAssetCode" ).with( same( Quiz.CM_QUESTION_ANSWER_ASSET_PREFIX ) ).will( returnValue( "answerasset" ) );
    mockCmAssetService.expects( atLeastOnce() ).method( "copyCMAsset" );

    copiedQuiz = quizService.copyQuiz( quizToCopy.getId(), expectedQuiz.getName() );

    assertSame( "Actual quiz didn't match with what is expected", expectedQuiz, copiedQuiz );
  }
  
  private void mockQuizQuestionCMService( QuizQuestion question )
  {
    
  }

  public void testDeleteQuizQuestionAnswers()
  {
    List questionAnswersToDelete = new ArrayList();
    Quiz quiz = this.buildFixedQuiz();

    QuizQuestion question = this.buildQuizQuestion( "Quiz Question 1" );
    QuizQuestionAnswer questionAnswer = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 1" );
    question.addQuizQuestionAnswer( questionAnswer );

    quiz.addQuizQuestion( question );

    questionAnswersToDelete.add( questionAnswer.getId() );

    mockQuizDAO.expects( once() ).method( "getQuizQuestionById" ).with( same( question.getId() ) ).will( returnValue( question ) );

    quizService.deleteQuizQuestionAnswers( question.getId(), questionAnswersToDelete );
  }

  public void testGetQuizQuestionAnswerById()
  {
    Quiz quiz = this.buildFixedQuiz();

    QuizQuestion question = this.buildQuizQuestion( "Quiz Question 1" );
    QuizQuestionAnswer questionAnswer = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 1" );
    question.addQuizQuestionAnswer( questionAnswer );

    QuizQuestion question2 = this.buildQuizQuestion( "Quiz Question 2" );
    QuizQuestionAnswer questionAnswer2 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 2" );
    question2.addQuizQuestionAnswer( questionAnswer2 );

    QuizQuestion question3 = this.buildQuizQuestion( "Quiz Question 3" );
    QuizQuestionAnswer questionAnswer3 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 3" );
    question3.addQuizQuestionAnswer( questionAnswer3 );

    quiz.addQuizQuestion( question );
    quiz.addQuizQuestion( question2 );
    quiz.addQuizQuestion( question3 );

    // QuizDAO expected to call getClaimFormById once with the ClaimFormId which will return
    // the Quiz expected
    mockQuizDAO.expects( once() ).method( "getQuizQuestionAnswerById" ).with( same( questionAnswer2.getId() ) ).will( returnValue( questionAnswer2 ) );

    QuizQuestionAnswer actualQuizQuestionAnswer = quizService.getQuizQuestionAnswerById( questionAnswer2.getId() );
    assertEquals( "Actual quiz question answer wasn't equal to what was expected", questionAnswer2, actualQuizQuestionAnswer );

    mockQuizDAO.verify();
  }

  public void testGetQuizQuestionsByQuizId()
  {

    Quiz quiz = this.buildFixedQuiz();

    QuizQuestion question = this.buildQuizQuestion( "Quiz Question 1" );
    QuizQuestionAnswer questionAnswer = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 1" );
    question.addQuizQuestionAnswer( questionAnswer );

    QuizQuestion question2 = this.buildQuizQuestion( "Quiz Question 2" );
    QuizQuestionAnswer questionAnswer2 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 2" );
    question2.addQuizQuestionAnswer( questionAnswer2 );

    QuizQuestion question3 = this.buildQuizQuestion( "Quiz Question 3" );
    QuizQuestionAnswer questionAnswer3 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 3" );
    question3.addQuizQuestionAnswer( questionAnswer3 );

    quiz.addQuizQuestion( question );
    quiz.addQuizQuestion( question2 );
    quiz.addQuizQuestion( question3 );

    // QuizDAO expected to call getClaimFormById once with the ClaimFormId which will return
    // the Quiz expected
    mockQuizDAO.expects( once() ).method( "getQuizQuestionsByQuizId" ).with( same( quiz.getId() ) ).will( returnValue( quiz.getQuizQuestions() ) );

    List actualQuizQuestions = quizService.getQuizQuestionsByQuizId( quiz.getId() );
    assertEquals( "Actual quiz questions wasn't equal to what was expected", quiz.getQuizQuestions(), actualQuizQuestions );

    mockQuizDAO.verify();
  }

  public void testGetQuizQuestionByIdWithAssociations()
  {
    Quiz quiz = this.buildFixedQuiz();

    QuizQuestion question = this.buildQuizQuestion( "Quiz Question 1" );
    QuizQuestionAnswer questionAnswer = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 1" );
    question.addQuizQuestionAnswer( questionAnswer );

    QuizQuestion question2 = this.buildQuizQuestion( "Quiz Question 2" );
    QuizQuestionAnswer questionAnswer2 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 2" );
    question2.addQuizQuestionAnswer( questionAnswer2 );

    QuizQuestion question3 = this.buildQuizQuestion( "Quiz Question 3" );
    QuizQuestionAnswer questionAnswer3 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 3" );
    QuizQuestionAnswer questionAnswer4 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 4" );
    QuizQuestionAnswer questionAnswer5 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 5" );
    question3.addQuizQuestionAnswer( questionAnswer3 );
    question3.addQuizQuestionAnswer( questionAnswer4 );
    question3.addQuizQuestionAnswer( questionAnswer5 );

    quiz.addQuizQuestion( question );
    quiz.addQuizQuestion( question2 );
    quiz.addQuizQuestion( question3 );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    AssociationRequest associationRequest = new QuizQuestionAssociationRequest( QuizQuestionAssociationRequest.ALL );

    Constraint[] arguments = { eq( question3.getId() ), eq( associationRequestCollection ) };

    // QuizDAO expected to call getClaimFormById once with the ClaimFormId which will return
    // the Quiz expected
    mockQuizDAO.expects( once() ).method( "getQuizQuestionByIdWithAssociations" ).with( arguments ).will( returnValue( question3 ) );

    associationRequestCollection.add( associationRequest );
    QuizQuestion actualQuizQuestion = quizService.getQuizQuestionByIdWithAssociations( question3.getId(), associationRequestCollection );
    assertEquals( "Actual quiz question wasn't equal to what was expected", question3, actualQuizQuestion );
    assertTrue( "Actual quiz question answers size was not equal to what was expected.", actualQuizQuestion.getQuizQuestionAnswers().size() == 3 );

    mockQuizDAO.verify();
  }

  public void testUpdateQuizQuestionAnswer()
  {
    Quiz quiz = this.buildFixedQuiz();

    QuizQuestion question = this.buildQuizQuestion( "Quiz Question 1" );
    QuizQuestionAnswer questionAnswer = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 1" );
    question.addQuizQuestionAnswer( questionAnswer );

    QuizQuestion question2 = this.buildQuizQuestion( "Quiz Question 2" );
    QuizQuestionAnswer questionAnswer2 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 2" );
    question2.addQuizQuestionAnswer( questionAnswer2 );

    QuizQuestion question3 = this.buildQuizQuestion( "Quiz Question 3" );
    QuizQuestionAnswer questionAnswer3 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 3" );
    QuizQuestionAnswer questionAnswer4 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 4" );
    QuizQuestionAnswer questionAnswer5 = this.buildCorrectQuizQuestionAnswer( "Quiz Question Answer 5" );
    question3.addQuizQuestionAnswer( questionAnswer3 );
    question3.addQuizQuestionAnswer( questionAnswer4 );
    question3.addQuizQuestionAnswer( questionAnswer5 );

    quiz.addQuizQuestion( question );
    quiz.addQuizQuestion( question2 );
    quiz.addQuizQuestion( question3 );

    mockQuizDAO.expects( once() ).method( "updateQuizQuestionAnswer" ).with( same( questionAnswer4 ) ).will( returnValue( questionAnswer4 ) );

    QuizQuestionAnswer actualQuizQuestionAnswer = quizService.updateQuizQuestionAnswer( questionAnswer4 );
    assertEquals( "Actual quiz question wasn't equal to what was expected", questionAnswer4, actualQuizQuestionAnswer );

    mockQuizDAO.verify();
  }

  /**
   * Setup the test. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    mockQuizDAO = new Mock( QuizDAO.class );
    mockCmAssetService = new Mock( CMAssetService.class );
    cmAssetServiceProxy = (CMAssetService)mockCmAssetService.proxy();
    quizService.setQuizDAO( (QuizDAO)mockQuizDAO.proxy() );
    quizService.setCmAssetService( cmAssetServiceProxy );
  }

  /**
   * Builds a random quiz with no questions
   * 
   * @return Quiz
   */
  private Quiz buildRandomQuiz()
  {

    return buildRandomQuiz( getUniqueString() );

  }

  private Quiz buildRandomQuiz( String uniqueString )
  {

    Quiz randomQuiz = new Quiz();
    randomQuiz.setId( Long.valueOf( uniqueString ) );
    randomQuiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    randomQuiz.setQuizType( QuizType.lookup( QuizType.RANDOM ) );
    randomQuiz.setName( "NAME-" + uniqueString );
    randomQuiz.setDescription( "DESCRIPTION-" + uniqueString );
    randomQuiz.setNumberOfQuestionsAsked( 4 );
    randomQuiz.setPassingScore( 5 );

    return randomQuiz;
  }

  /**
   * Builds a random quiz with no questions
   * 
   * @return Quiz
   */
  private Quiz buildFixedQuiz()
  {

    String uniqueString = getUniqueString();

    Quiz randomQuiz = new Quiz();
    randomQuiz.setId( Long.valueOf( uniqueString ) );
    randomQuiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    randomQuiz.setQuizType( QuizType.lookup( QuizType.FIXED ) );
    randomQuiz.setName( "NAME-" + uniqueString );
    randomQuiz.setDescription( "DESCRIPTION-" + uniqueString );
    randomQuiz.setNumberOfQuestionsAsked( 4 );
    randomQuiz.setPassingScore( 5 );

    return randomQuiz;

  }

  /**
   * Builds a random quiz with question.
   * 
   * @return Quiz
   */
  private Quiz buildCompleteRandomQuiz()
  {

    String uniqueString = getUniqueString();

    Quiz completeRandomQuiz = this.buildRandomQuiz( uniqueString );

    // Quiz completeRandomQuiz = new Quiz();
    // completeRandomQuiz.setId(Long.valueOf(uniqueString));
    // completeRandomQuiz.setClaimFormStatusType( ClaimFormStatusType.lookup(
    // ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    // completeRandomQuiz.setQuizType( QuizType.lookup(QuizType.RANDOM) );
    // completeRandomQuiz.setName("NAME-" + uniqueString);
    // completeRandomQuiz.setDescription("DESCRIPTION-" + uniqueString);
    // completeRandomQuiz.setNumberOfQuestionsAsked(4);
    // completeRandomQuiz.setPassingScore(5);

    // Set the Questions
    completeRandomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Q1-" + uniqueString, true, QuizQuestionStatusType.ACTIVE ) );
    completeRandomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Q2-" + uniqueString, true, QuizQuestionStatusType.ACTIVE ) );
    completeRandomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Q3-" + uniqueString, true, QuizQuestionStatusType.ACTIVE ) );
    completeRandomQuiz.addQuizQuestion( buildCompleteQuizQuestion( "Q4-" + uniqueString, true, QuizQuestionStatusType.ACTIVE ) );

    return completeRandomQuiz;

  }

  /**
   * Build a quizQuestion with answers for testing.
   * 
   * @param uniqueString
   * @param required TODO
   * @return QuizQuestion
   */
  private QuizQuestion buildCompleteQuizQuestion( String uniqueString, boolean required, String statusTypeCode )
  {

    QuizQuestion quizQuestion = new QuizQuestion();

    quizQuestion.setId( new Long( Math.round( Math.random() % 47909287 ) ) );
    quizQuestion.setCmAssetName( "CMASSETNAME-" + uniqueString );
    quizQuestion.setRequired( required );
    quizQuestion.setStatusType( QuizQuestionStatusType.lookup( statusTypeCode ) );

    quizQuestion.addQuizQuestionAnswer( buildCorrectQuizQuestionAnswer( "QQA1-" + uniqueString ) );
    quizQuestion.addQuizQuestionAnswer( buildIncorrectQuizQuestionAnswer( "QQA2-" + uniqueString ) );
    quizQuestion.addQuizQuestionAnswer( buildCorrectQuizQuestionAnswer( "QQA3-" + uniqueString ) );

    return quizQuestion;

  }

  /**
   * Build a quizQuestion for testing.
   * 
   * @param uniqueString
   * @return QuizQuestion
   */
  private QuizQuestion buildQuizQuestion( String uniqueString )
  {

    QuizQuestion quizQuestion = new QuizQuestion();

    quizQuestion.setId( new Long( Math.round( ( Math.random() * 10000000 ) % 47909287 ) ) );
    quizQuestion.setCmAssetName( "CMASSETNAME-" + uniqueString );
    quizQuestion.setRequired( true );

    return quizQuestion;

  }

  /**
   * Builds an incorrect quizQuestionAnswer.
   * 
   * @param uniqueString
   * @return QuizQuestionAnswer
   */
  private QuizQuestionAnswer buildIncorrectQuizQuestionAnswer( String uniqueString )
  {
    return buildQuizQuestionAnswer( uniqueString, false );
  }

  /**
   * Builds a correct quizQuestionAnswer.
   * 
   * @param uniqueString
   * @return QuizQuestionAnswer
   */
  private QuizQuestionAnswer buildCorrectQuizQuestionAnswer( String uniqueString )
  {
    return buildQuizQuestionAnswer( uniqueString, true );
  }

  /**
   * Builds a quizQuestionAnswer for testing.
   * 
   * @param uniqueString
   * @param isCorrect
   * @return QuizQuestionAnswer
   */
  private QuizQuestionAnswer buildQuizQuestionAnswer( String uniqueString, boolean isCorrect )
  {

    QuizQuestionAnswer quizQuestionAnswer = new QuizQuestionAnswer();

    quizQuestionAnswer.setId( new Long( Math.round( Math.random() % 342890 ) ) );
    quizQuestionAnswer.setCmAssetCode( "CMASSESTNAMEANSWER-" + uniqueString );
    quizQuestionAnswer.setAnswerCmKey( "CMASSESTKEYTEXT-" + uniqueString );
    quizQuestionAnswer.setExplanationCmKey( "CMASSESTKEYEXPLAINATION-" + uniqueString );
    quizQuestionAnswer.setCorrect( isCorrect );

    return quizQuestionAnswer;

  }

  // Junit test case methods for quiz Learning objects

  /**
   * Test get the next slide number
   */
  public void testGetNextSlide() throws ServiceErrorException
  {
    int nextSlideNumber = 10;
    // Long quizLibId=5021L;
    QuizLearningObject quizObj = buildQuizLearning();
    Long quizLibId = quizObj.getQuiz().getId();

    mockQuizDAO.expects( once() ).method( "getNextSlideIdForQuiz" ).will( returnValue( nextSlideNumber ) );

    int actualSlideNumber = this.quizService.getNextSlideIdForQuiz( quizLibId );

    assertEquals( "Next expected slide number  didn't match the actuals.", actualSlideNumber, nextSlideNumber );
  }

  /**
   * Test save the quiz learning
   */
  public void testsaveQuizLearning() throws ServiceErrorException
  {
    QuizLearningObject quizObj = buildQuizLearning();
    mockQuizDAO.expects( once() ).method( "saveQuizLearning" ).will( returnValue( quizObj ) );

    QuizLearningObject actualsavedQuiz = this.quizService.saveQuizLearning( quizObj );

    assertEquals( "Actual saved quiz learning object didn't match the expected object.", actualsavedQuiz.getContentResourceCMCode(), quizObj.getContentResourceCMCode() );
  }

  /**
   * Test save the quiz learning
   */
  public void testsaveQuizLearningResources() throws ServiceErrorException
  {
    String uniqueAssetName = "Quiz Learning test Name";
    QuizLearningObject quizObj = buildQuizLearning();
    String url = "http://bonfireqagf.biworldwide.com/bonfire-cm/cm3dam/quiz/jada1/5003_01_telephone.jpg";
    StringBuilder htmlImageString = new StringBuilder( "" );
    htmlImageString.append( "<p><img src=\"" + url + "\" alt=\"Photo\" class=\"thumb\" /></p>" );
    mockCmAssetService.expects( once() ).method( "getUniqueAssetCode" ).will( returnValue( uniqueAssetName ) );
    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );
    QuizLearningObject savedCMQuiz = this.quizService.saveQuizLearningResources( quizObj, htmlImageString.toString(), "test desc junit", "quiz/jada1/5003_01_telephone.jpg" );

    assertTrue( "Actual saved quiz learning object didn't match the expected object.", savedCMQuiz.getContentResourceCMCode() != null );
  }

  private QuizLearningObject buildQuizLearning() throws ServiceErrorException
  {
    int nextSlideNumber = 10;
    Quiz quiz = buildFixedQuiz();
    QuizLearningObject quizObj = new QuizLearningObject();
    quizObj.setQuiz( quiz );
    quizObj.setSlideNumber( nextSlideNumber );
    quizObj.setStatus( QuizLearningObject.ACTIVE_STATUS );
    // quizObj.setContentResourceCMCode("quiz_learning_data.learning.10000869");
    return quizObj;
  }
  
  /**
   * Builds a static quiz for testing.
   * 
   * @param uniqueString
   * @return Quiz
   */
  public Quiz buildCompletedQuiz( String uniqueString )
  {
    Quiz quiz = new Quiz();

    quiz.setName( uniqueString );
    quiz.setDescription( uniqueString + " - DESCRIPTION" );
    quiz.setQuizType( (QuizType)QuizType.getList().get( 0 ) );
    quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
    quiz.setNumberOfQuestionsAsked( 123 );
    quiz.setPassingScore( 12 );

    quiz.addQuizQuestion( buildQuizQuestionWithAnswers( "1-Question-" + uniqueString ) );
    quiz.addQuizQuestion( buildQuizQuestionWithAnswers( "2-Question-" + uniqueString ) );
    quiz.addQuizQuestion( buildQuizQuestionWithAnswers( "3-Question-" + uniqueString ) );

    return quiz;
  }

  /**
   * Builds a static quizQuestion for testing.
   * 
   * @param uniqueString
   * @return QuizQuestion
   */
  public QuizQuestion buildQuizQuestionWithAnswers( String uniqueString )
  {

    QuizQuestion quizQuestion = new QuizQuestion()
    {
      protected CMAssetService getCMAssetService()
      {
        return cmAssetServiceProxy;
      }
    };

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
  public QuizQuestionAnswer buildQuizQuestionAnswer( String uniqueString )
  {

    QuizQuestionAnswer quizQuestionAnswer = new QuizQuestionAnswer()
    {
      protected CMAssetService getCMAssetService()
      {
        return cmAssetServiceProxy;
      }
    };
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
}