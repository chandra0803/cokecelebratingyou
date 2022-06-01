
package com.biperf.core.service.fileload.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.easymock.EasyMock;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.QuizQuestionStatusType;
import com.biperf.core.domain.enums.QuizType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecordError;
import com.biperf.core.domain.fileload.ProductImportRecord;
import com.biperf.core.domain.fileload.QuizImportRecord;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.quiz.QuizService;

public class QuizImportStrategyTest extends BaseServiceTest
{
  private QuizService quizServiceMock;

  private QuizImportStrategy importStrategyUnderTest;

  public void setUp() throws Exception
  {
    super.setUp();
    importStrategyUnderTest = new QuizImportStrategy();

    quizServiceMock = EasyMock.createMock( QuizService.class );
    importStrategyUnderTest.setQuizService( quizServiceMock );

  }

  public void testVerifyCleanImportFile()
  {
    ImportFile file = buildCleanQuizImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );
    for ( Iterator iter = file.getQuizImportRecords().iterator(); iter.hasNext(); )
    {
      QuizImportRecord record = (QuizImportRecord)iter.next();
      // quizServiceControl.expectAndReturn( quizServiceMock
      // .getProductByNameAndCategoryId( record.getProductName(), null ), null );
    }

    EasyMock.replay( quizServiceMock );
    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getQuizImportRecords() ) );
    EasyMock.verify( quizServiceMock );

    assertTrue( file.getImportRecordErrors().isEmpty() );
  }

  public void testImportCleanImportFile()
  {
    ImportFile file = buildCleanQuizImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.IMPORT_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );

    try
    {
      int i = 0;
      for ( Iterator iter = file.getProductImportRecords().iterator(); iter.hasNext(); i++ )
      {
        ProductImportRecord productImportRecord = (ProductImportRecord)iter.next();

        // productCharacteristicServiceControl
        // .expectAndReturn( productCharacteristicServiceMock
        // .getCharacteristicById( productImportRecord
        // .getCharacteristicId1() ),
        // new ProductCharacteristicType() );
        //
        // productServiceControl.expectAndReturn( productServiceMock.save( null, new Product() ),
        // new Product() );
        // if ( i == 0 )
        // {
        // ProductCategory category = new ProductCategory();
        // category.setName( "Name" );
        // productCategoryServiceControl.expectAndReturn( productCategoryServiceMock
        // .saveProductCategory( category, null ), category );
        // }
      }
      EasyMock.replay( quizServiceMock );
      // productCategoryServiceControl.replay();
      // productCharacteristicServiceControl.replay();
      importStrategyUnderTest.importImportFile( file, new ArrayList( file.getProductImportRecords() ) );
      EasyMock.verify( quizServiceMock );
      // productCategoryServiceControl.verify();
    }
    catch( ServiceErrorException e )
    {
      fail( "" + e );
    }
    // catch( UniqueConstraintViolationException e )
    // {
    // fail( "" + e );
    // }

    assertTrue( file.getImportRecordErrors().isEmpty() );
  }

  public void testVerifyDirtyImportFile()
  {
    ImportFile file = buildBadQuizImportFileStructureProblems();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );

    for ( Iterator iter = file.getQuizImportRecords().iterator(); iter.hasNext(); )
    {
      QuizImportRecord record = (QuizImportRecord)iter.next();
      // productServiceControl.expectAndReturn( productServiceMock
      // .getProductByNameAndCategoryId( record.getProductName(), null ), null );
    }
    EasyMock.replay( quizServiceMock );
    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getQuizImportRecords() ) );
    EasyMock.verify( quizServiceMock );

    assertFalse( file.getImportRecordErrors().isEmpty() );
    assertEquals( 4, file.getImportRecordErrorCount() );
  }

  public static ImportFile buildCleanQuizImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.QUIZ ) );
    LinkedHashSet records = new LinkedHashSet( 16 );

    QuizImportRecord headerRecord = new QuizImportRecord();
    headerRecord.setActionType( "H" );
    headerRecord.setRecordType( QuizImportRecord.HEADER_RECORD );
    headerRecord.setQuizName( "The test quiz" );
    headerRecord.setQuizDescription( "This quiz tests your knowledge of stuff." );
    headerRecord.setQuizType( QuizType.lookup( QuizType.RANDOM ).getCode() );
    headerRecord.setQuizPassingScore( new Integer( 2 ) );
    headerRecord.setQuizNumberOfQuestionsAsked( new Integer( 3 ) );
    records.add( headerRecord );

    // Question 1
    QuizImportRecord questionRecord1 = new QuizImportRecord();
    questionRecord1.setActionType( "Q" );
    questionRecord1.setRecordType( QuizImportRecord.QUESTION_RECORD );
    questionRecord1.setQuestion( "What is the name of our state flower?" );
    questionRecord1.setQuestionRequired( new Boolean( true ) );
    questionRecord1.setQuestionStatusType( QuizQuestionStatusType.lookup( QuizQuestionStatusType.ACTIVE ).getCode() );
    records.add( questionRecord1 );

    QuizImportRecord answerRecord1 = new QuizImportRecord();
    answerRecord1.setActionType( "A" );
    answerRecord1.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord1.setAnswerChoice( "Black-eyed Susan" );
    answerRecord1.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord1 );

    QuizImportRecord answerRecord2 = new QuizImportRecord();
    answerRecord2.setActionType( "A" );
    answerRecord2.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord2.setAnswerChoice( "Lady Slipper" );
    answerRecord2.setAnswerCorrect( new Boolean( true ) );
    answerRecord2.setAnswerChoiceExplanation( "Lady Slipper is the state flower of Minnesota." );
    records.add( answerRecord2 );

    QuizImportRecord answerRecord3 = new QuizImportRecord();
    answerRecord3.setActionType( "A" );
    answerRecord3.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord3.setAnswerChoice( "Rugosa Rose" );
    answerRecord3.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord3 );

    QuizImportRecord answerRecord4 = new QuizImportRecord();
    answerRecord4.setActionType( QuizImportRecord.ANSWER_RECORD );
    answerRecord4.setRecordType( "A" );
    answerRecord4.setAnswerChoice( "Balloon Flower" );
    answerRecord4.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord4 );

    // Question 2
    QuizImportRecord questionRecord2 = new QuizImportRecord();
    questionRecord2.setActionType( "Q" );
    questionRecord2.setRecordType( QuizImportRecord.QUESTION_RECORD );
    questionRecord2.setQuestion( "How many days are there in a non-leap year?" );
    questionRecord2.setQuestionRequired( new Boolean( true ) );
    questionRecord2.setQuestionStatusType( QuizQuestionStatusType.lookup( QuizQuestionStatusType.ACTIVE ).getCode() );
    records.add( questionRecord2 );

    QuizImportRecord answerRecord5 = new QuizImportRecord();
    answerRecord5.setActionType( "A" );
    answerRecord5.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord5.setAnswerChoice( "366" );
    answerRecord5.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord5 );

    QuizImportRecord answerRecord6 = new QuizImportRecord();
    answerRecord6.setActionType( "A" );
    answerRecord6.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord6.setAnswerChoice( "365" );
    answerRecord6.setAnswerCorrect( new Boolean( true ) );
    answerRecord6.setAnswerChoiceExplanation( "365 is the right answer." );
    records.add( answerRecord6 );

    QuizImportRecord answerRecord7 = new QuizImportRecord();
    answerRecord7.setActionType( "A" );
    answerRecord7.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord7.setAnswerChoice( "364" );
    answerRecord7.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord7 );

    QuizImportRecord answerRecord8 = new QuizImportRecord();
    answerRecord8.setActionType( "A" );
    answerRecord8.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord8.setAnswerChoice( "367" );
    answerRecord8.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord8 );

    // Question 3
    QuizImportRecord questionRecord3 = new QuizImportRecord();
    questionRecord3.setActionType( "Q" );
    questionRecord3.setRecordType( QuizImportRecord.QUESTION_RECORD );
    questionRecord3.setQuestion( "Who is the father of Surrealism?" );
    questionRecord3.setQuestionRequired( new Boolean( true ) );
    questionRecord3.setQuestionStatusType( QuizQuestionStatusType.lookup( QuizQuestionStatusType.ACTIVE ).getCode() );
    records.add( questionRecord3 );

    QuizImportRecord answerRecord9 = new QuizImportRecord();
    answerRecord9.setActionType( "A" );
    answerRecord9.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord9.setAnswerChoice( "Salvador Dali" );
    answerRecord9.setAnswerChoiceExplanation( "Dali is the man!" );
    answerRecord9.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord9 );

    QuizImportRecord answerRecord10 = new QuizImportRecord();
    answerRecord10.setActionType( "A" );
    answerRecord10.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord10.setAnswerChoice( "Pablo Picasso." );
    answerRecord10.setAnswerCorrect( new Boolean( true ) );
    records.add( answerRecord10 );

    QuizImportRecord answerRecord11 = new QuizImportRecord();
    answerRecord11.setActionType( "A" );
    answerRecord11.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord11.setAnswerChoice( "Jackon Pollack." );
    answerRecord11.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord11 );

    QuizImportRecord answerRecord12 = new QuizImportRecord();
    answerRecord12.setActionType( "A" );
    answerRecord12.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord12.setAnswerChoice( "Michael Jackson." );
    answerRecord12.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord12 );

    file.setQuizImportRecords( records );
    return file;
  }

  public static ImportFile buildBadQuizImportFileStructureProblems()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.QUIZ ) );
    LinkedHashSet records = new LinkedHashSet( 11 );

    QuizImportRecord headerRecord = new QuizImportRecord();
    headerRecord.setActionType( "H" );
    headerRecord.setRecordType( QuizImportRecord.HEADER_RECORD );
    headerRecord.setQuizName( "The test quiz with bad data" );
    headerRecord.setQuizDescription( "This quiz tests your knowledge of stuff." );
    headerRecord.setQuizType( QuizType.lookup( QuizType.RANDOM ).toString() );
    headerRecord.setQuizPassingScore( new Integer( 2 ) );
    headerRecord.setQuizNumberOfQuestionsAsked( new Integer( 3 ) );
    records.add( headerRecord );

    // Missing Question Record

    QuizImportRecord answerRecord1 = new QuizImportRecord();
    answerRecord1.setActionType( "A" );
    answerRecord1.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord1.setAnswerChoice( "Black-eyed Susan" );
    answerRecord1.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord1 );

    QuizImportRecord answerRecord2 = new QuizImportRecord();
    answerRecord2.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord2.setActionType( "A" );
    answerRecord2.setAnswerChoice( "Lady Slipper" );
    answerRecord2.setAnswerCorrect( new Boolean( true ) );
    answerRecord2.setAnswerChoiceExplanation( "Lady Slipper is the state flower of Minnesota." );
    records.add( answerRecord2 );

    QuizImportRecord answerRecord3 = new QuizImportRecord();
    answerRecord3.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord3.setActionType( "A" );
    answerRecord3.setAnswerChoice( "Rugosa Rose" );
    answerRecord3.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord3 );

    QuizImportRecord answerRecord4 = new QuizImportRecord();
    answerRecord4.setRecordType( QuizImportRecord.ANSWER_RECORD );
    answerRecord4.setActionType( "A" );
    answerRecord4.setAnswerChoice( "Balloon Flower" );
    answerRecord4.setAnswerCorrect( new Boolean( false ) );
    records.add( answerRecord4 );

    QuizImportRecord questionRecord2 = new QuizImportRecord();
    questionRecord2.setActionType( "Q" );
    questionRecord2.setRecordType( QuizImportRecord.QUESTION_RECORD );
    questionRecord2.setQuestion( "How many days are there in a non-leap year?" );
    questionRecord2.setQuestionRequired( new Boolean( true ) );
    questionRecord2.setQuestionStatusType( QuizQuestionStatusType.lookup( QuizQuestionStatusType.ACTIVE ).getCode() );
    records.add( questionRecord2 );

    // Missing Answer Records

    file.setQuizImportRecords( records );

    return file;
  }

  public static void addErrors( ImportFile file )
  {
    Set errors = new HashSet( 1 );
    errors.add( new ImportRecordError() );
    for ( Iterator iter = file.getProductImportRecords().iterator(); iter.hasNext(); )
    {
      ProductImportRecord record = (ProductImportRecord)iter.next();
      record.setImportRecordErrors( errors );
    }
  }
}