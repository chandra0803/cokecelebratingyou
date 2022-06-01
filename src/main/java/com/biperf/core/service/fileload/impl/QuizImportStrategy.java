/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/impl/QuizImportStrategy.java,v $
 */

package com.biperf.core.service.fileload.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.QuizQuestionStatusType;
import com.biperf.core.domain.enums.QuizType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.QuizImportRecord;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.UserManager;

public class QuizImportStrategy extends ImportStrategy
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final Logger log = Logger.getLogger( QuizImportStrategy.class );
  public static final String QUIZ_HEADER_MISSING = "quiz.errors";
  public static final String QUESTION_HEADER_MISSING = "quiz.errors";
  public static final String ANSWER_HEADER_MISSING = "quiz.errors";

  public static final String QUIZ_NAME = "quiz.form.NAME";
  public static final String QUIZ_DESCRIPTION = "quiz.form.DESCRIPTION";
  public static final String QUIZ_DISPLAY_METHOD = "quiz.form.DISPLAY_METHOD";
  public static final String QUIZ_PASSING_SCORE = "quiz.form.PASSING_SCORE";
  public static final String QUIZ_NUMBER_TO_ASK = "quiz.form.NUMBER_TO_ASK";
  public static final String QUIZ_QUESTION_TEXT = "quiz.question.QUESTION";
  public static final String QUIZ_QUESTION_REQUIRED = "quiz.question.QUESTION_REQUIRED";
  public static final String QUIZ_QUESTION_ANSWER_TEXT = "quiz.question_answer.ANSWER";
  public static final String QUIZ_QUESTION_ANSWER_CORRECT = "quiz.question_answer.CORRECT";

  public static final String ANSWER_CM_KEY = "QUESTION_ANSWER";
  public static final String ANSWER_EXPLANATION_CM_KEY = "QUESTION_ANSWER_EXPLANATION";

  private QuizService quizService;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Imports the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the records to import.
   * @throws com.biperf.core.exception.ServiceErrorException
   */
  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    int counter = 0;
    log.info( "importImportFile: Record count to process: " + counter );

    int headerCounter = 0;
    int questionCounter = 0;
    int answerCounter = 0;

    Quiz quiz = null;
    QuizQuestion quizQuestion = null;
    QuizQuestionAnswer quizQuestionAnswer = null;

    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;

      QuizImportRecord importRecord = (QuizImportRecord)iterator.next();
      if ( !importRecord.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      // persist quiz, quiz questions and quiz question answers
      if ( importRecord.getRecordType().equals( QuizImportRecord.HEADER_RECORD ) )
      {
        quiz = populateQuiz( importRecord );
        quiz = quizService.saveQuiz( quiz );
        // Note: need to flush to get the db id for the saveQuizQuestion() service call later
        HibernateSessionManager.getSession().flush();
        headerCounter++;
      }
      if ( importRecord.getRecordType().equals( QuizImportRecord.QUESTION_RECORD ) )
      {
        answerCounter = 0; // reset
        quizQuestion = populateQuizQuestion( importRecord, quiz, questionCounter );
        quizQuestion = quizService.saveQuizQuestion( quiz.getId(), quizQuestion, importRecord.getQuestion() );
        // Note: need to flush to get the db id for the saveQuizQuestionAnswer() service call later
        HibernateSessionManager.getSession().flush();
        questionCounter++;
      }
      if ( importRecord.getRecordType().equals( QuizImportRecord.ANSWER_RECORD ) )
      {
        quizQuestionAnswer = populateQuizAnswer( importRecord, quizQuestion );
        quizQuestionAnswer = quizService.saveQuizQuestionAnswer( quizQuestion.getId(), quizQuestionAnswer, importRecord.getAnswerChoice(), importRecord.getAnswerChoiceExplanation() );
        HibernateSessionManager.getSession().flush();
        answerCounter++;
      }
    }

    importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    importFile.setImportedBy( UserManager.getUserName() );
    importFile.setDateImported( DateUtils.getCurrentDate() );

    log.info( "importImportFile: total questions processed: " + questionCounter );
    log.info( "importImportFile: total processed record count: " + counter );

  }

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the recordst to import.
   */
  public void verifyImportFile( ImportFile importFile, List records )
  {
    int errorRecordCount = importFile.getImportRecordErrorCount();

    // Validate the import file's structure
    Collection structureErrors = validateImportRecordStructure( records, importFile );
    if ( !structureErrors.isEmpty() )
    {
      errorRecordCount = errorRecordCount + structureErrors.size();
      createAndAddImportRecordErrors( importFile, (QuizImportRecord)records.get( 0 ), structureErrors );
    }

    // Walk thru each record and validate required fields
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      QuizImportRecord record = (QuizImportRecord)iterator.next();

      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection dataErrors = validateImportRecord( record );
      if ( !dataErrors.isEmpty() )
      {
        errorRecordCount++;
        createAndAddImportRecordErrors( importFile, record, dataErrors );
      }

    }
    importFile.setImportRecordErrorCount( errorRecordCount );
    importFile.setDateVerified( DateUtils.getCurrentDate() );
    importFile.setVerifiedBy( UserManager.getUserName() );
    importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFIED ) );

  }

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the records to import.
   * @param notInUsed - not used, imposed by superclass
   * @throws ServiceErrorException
   */
  public void verifyImportFile( ImportFile importFile, List records, boolean notInUsed ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    verifyImportFile( importFile, records );

  }

  /**
   * Verify the quiz import file record structure is correct. Import File Format Requirements -
   * Header record first, followed by a question record, then multiple answer records, then another
   * question record, and multiple answer records, etc...
   * 
   * @param records
   * @param importFile
   * @return Collection
   */
  protected Collection validateImportRecordStructure( List records, ImportFile importFile )
  {
    Collection errors = new ArrayList();

    int headerCounter = 0;
    int questionCounter = 0;
    int answerCounter = 0;

    int totalNbrOfQuestionsOnFile = 0;

    String quizName = "TBD";
    String quizType = null;
    Integer passingScore = new Integer( 0 );
    Integer nbrOfQuestionsToAsk = new Integer( 0 );

    // Walk thru each record in Quiz Import file
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      QuizImportRecord record = (QuizImportRecord)iterator.next();

      // *** Validate header record ***
      if ( record.getRecordType().equals( QuizImportRecord.HEADER_RECORD ) )
      {
        quizName = record.getQuizName();
        quizType = record.getQuizType();
        passingScore = record.getQuizPassingScore();
        nbrOfQuestionsToAsk = record.getQuizNumberOfQuestionsAsked();

        headerCounter++;
        log.info( "validateImportRecordStructure: quiz header record count: " + headerCounter );
      }
      // *** Validate Question record ***
      else if ( record.getRecordType().equals( QuizImportRecord.QUESTION_RECORD ) )
      {
        totalNbrOfQuestionsOnFile++;

        // error if there hasn't been one header record previously in the file when hitting this
        // record type
        if ( headerCounter != 1 )
        {
          checkRequired( null, QUIZ_HEADER_MISSING, errors );
        }
        else
        {
          questionCounter++; // increment question counter
          log.info( "validateImportRecordStructure: quiz question record #: " + questionCounter );

          // previous question didn't have any answers on file
          if ( questionCounter > 1 && answerCounter == 0 )
          {
            checkRequired( null, ANSWER_HEADER_MISSING, errors );
          }

          answerCounter = 0; // reset question's answer counter
        }
      }
      // *** Validate Answer record ***
      else if ( record.getRecordType().equals( QuizImportRecord.ANSWER_RECORD ) )
      {
        answerCounter++; // increment answer counter
        log.info( "validateImportRecordStructure: quiz answer record #: " + answerCounter );

        // previous record on file was not a question record
        if ( questionCounter < 1 )
        {
          checkRequired( null, QUESTION_HEADER_MISSING, errors );
        }

      }
    } // END for
    log.info( "validateImportRecordStructure: total question record count: " + totalNbrOfQuestionsOnFile );

    // Verify for a Fixed Quiz...
    if ( quizType.equals( QuizType.FIXED ) )
    {
      // the passing score is not greater than the total number of questions in a quiz
      if ( passingScore.intValue() > totalNbrOfQuestionsOnFile )
      {
        errors.add( new ServiceError( "quiz.errors.PASSING_SCORE_TOO_HIGH", quizName ) );
      }
    }
    // Verify for a Random Quiz...
    else if ( quizType.equals( QuizType.RANDOM ) )
    {
      // the passing score is not greater than the numberOfQuestionsAsked
      if ( passingScore.intValue() > nbrOfQuestionsToAsk.intValue() )
      {
        errors.add( new ServiceError( "quiz.errors.PASSING_SCORE_TOO_HIGH", quizName ) );
      }
      // the numberOfQuestionsAsked is not greater than the total number of questions in
      // a quiz
      if ( nbrOfQuestionsToAsk.intValue() > totalNbrOfQuestionsOnFile )
      {
        errors.add( new ServiceError( "quiz.errors.QUIZ_RANDOM_QUESTION", quizName ) );
      }
    }

    return errors;
  }

  /**
   * Validates a Quiz import record.
   * 
   * @param importRecord the import record to be validated.
   * @return information about the errors for this import record, as a <code>Collection</code> of
   *         {@link ServiceError} objects.
   */
  protected Collection validateImportRecord( QuizImportRecord importRecord )
  {
    Collection errors = new ArrayList();

    // Verify Quiz's Required Fields on the Header Record
    if ( importRecord.getRecordType().equals( QuizImportRecord.HEADER_RECORD ) )
    {
      checkRequired( importRecord.getQuizName(), QUIZ_NAME, errors );
      // As per bug 15153, no error should be reported if the description is missing
      // checkRequired( importRecord.getQuizDescription(), QUIZ_DESCRIPTION, errors );
      checkRequired( importRecord.getQuizType(), QUIZ_DISPLAY_METHOD, errors );
      checkRequired( importRecord.getQuizPassingScore(), QUIZ_PASSING_SCORE, errors );
      // Verify Random Quiz
      if ( importRecord.getQuizType() != null && importRecord.getQuizType().equals( QuizType.RANDOM ) )
      {
        // Required if Random Quiz
        checkBothRequiredOrBothEmpty( importRecord.getQuizType(), QUIZ_DISPLAY_METHOD, importRecord.getQuizNumberOfQuestionsAsked(), QUIZ_NUMBER_TO_ASK, errors );
      }
    }

    // Verify Question's Required Fields on the Question Record
    if ( importRecord.getRecordType().equals( QuizImportRecord.QUESTION_RECORD ) )
    {
      checkRequired( importRecord.getQuestion(), QUIZ_QUESTION_TEXT, errors );
    }

    // Verify Answer's Required Fields on the Answer Record
    if ( importRecord.getRecordType().equals( QuizImportRecord.ANSWER_RECORD ) )
    {
      checkRequired( importRecord.getAnswerChoice(), QUIZ_QUESTION_ANSWER_TEXT, errors );
      checkRequired( importRecord.getAnswerCorrect(), QUIZ_QUESTION_ANSWER_CORRECT, errors );
    }

    return errors;
  }

  /**
   * Instantiate a quiz object based on the import file record
   * 
   * @param importRecord
   * @return Quiz
   */
  protected Quiz populateQuiz( QuizImportRecord importRecord )
  {
    Quiz quiz = new Quiz();
    quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    quiz.setName( importRecord.getQuizName() );
    quiz.setDescription( importRecord.getQuizDescription() );
    quiz.setQuizType( QuizType.lookup( importRecord.getQuizType() ) );
    quiz.setPassingScore( importRecord.getQuizPassingScore().intValue() );
    quiz.setNumberOfQuestionsAsked( importRecord.getQuizNumberOfQuestionsAsked().intValue() );

    return quiz;
  }

  /**
   * Instantiate a quiz question based on the import file record
   * 
   * @param importRecord
   * @param quiz
   * @param seqNumber
   * @return QuizQuestion
   */
  protected QuizQuestion populateQuizQuestion( QuizImportRecord importRecord, Quiz quiz, int seqNumber )
  {
    QuizQuestion quizQuestion = new QuizQuestion();
    quizQuestion.setQuiz( quiz );
    if ( quiz.getQuizType().equals( QuizType.lookup( QuizType.FIXED ) ) )
    {
      quizQuestion.setRequired( true );
    }
    else
    {
      quizQuestion.setRequired( importRecord.getQuestionRequired().booleanValue() );
    }
    quizQuestion.setSequenceNum( seqNumber );
    quizQuestion.setStatusType( QuizQuestionStatusType.lookup( importRecord.getQuestionStatusType() ) );

    return quizQuestion;
  }

  /**
   * Instantiate a quiz question answer based on the import file record
   * 
   * @param importRecord
   * @param quizQuestion
   * @return QuizQuestionAnswer
   */
  protected QuizQuestionAnswer populateQuizAnswer( QuizImportRecord importRecord, QuizQuestion quizQuestion )
  {
    QuizQuestionAnswer quizQuestionAnswer = new QuizQuestionAnswer();
    quizQuestionAnswer.setQuizQuestion( quizQuestion );
    quizQuestionAnswer.setAnswerCmKey( ANSWER_CM_KEY );
    quizQuestionAnswer.setExplanationCmKey( ANSWER_EXPLANATION_CM_KEY );
    quizQuestionAnswer.setCorrect( importRecord.getAnswerCorrect().booleanValue() );

    return quizQuestionAnswer;
  }

  // ---------------------------------------------------------------------------
  // Service Methods
  // ---------------------------------------------------------------------------

  public void setQuizService( QuizService quizService )
  {
    this.quizService = quizService;
  }

}
