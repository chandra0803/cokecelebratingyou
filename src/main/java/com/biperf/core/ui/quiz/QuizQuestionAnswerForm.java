/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/quiz/QuizQuestionAnswerForm.java,v $
 */

package com.biperf.core.ui.quiz;

import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.ui.BaseForm;

/*
 * QuizQuestionAnswerForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Oct
 * 28, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class QuizQuestionAnswerForm extends BaseForm
{
  private String method;
  private Long quizQuestionAnswerId;
  private String quizQuestionAnswerText;
  private String quizQuestionAnswerExplanation;
  private String quizQuestionAnswerCmAssetCode;
  private String quizQuestionAnswerCmKey;
  private String quizQuestionAnswerExplanationCmKey;
  private boolean quizQuestionAnswerCorrect;

  private Long quizQuestionId;
  private String quizQuestionText;
  private boolean quizQuestionRequired;
  private String quizQuestionStatus;
  private String quizQuestionStatusText;

  private String quizName;
  private boolean quizLive;

  /**
   * Load the form.
   * 
   * @param answer
   */
  public void load( QuizQuestionAnswer answer )
  {
    this.quizName = answer.getQuizQuestion().getQuiz().getName();
    this.quizLive = answer.getQuizQuestion().getQuiz().isAssigned();

    this.quizQuestionId = answer.getQuizQuestion().getId();
    this.quizQuestionText = answer.getQuizQuestion().getQuestionText();
    this.quizQuestionRequired = answer.getQuizQuestion().isRequired();
    this.quizQuestionStatus = answer.getQuizQuestion().getStatusType().getCode();
    this.quizQuestionStatusText = answer.getQuizQuestion().getStatusType().getName();

    this.quizQuestionAnswerId = answer.getId();
    this.quizQuestionAnswerText = answer.getQuestionAnswerText();
    this.quizQuestionAnswerExplanation = answer.getQuestionAnswerExplanation();
    this.quizQuestionAnswerCmAssetCode = answer.getCmAssetCode();
    this.quizQuestionAnswerCmKey = answer.getAnswerCmKey();
    this.quizQuestionAnswerExplanationCmKey = answer.getExplanationCmKey();
    this.quizQuestionAnswerCorrect = answer.isCorrect();
  }

  /**
   * Creates a detatched QuizQuestionAnswer Domain Object that will later be synchronized with a
   * looked up promotion object in the service
   * 
   * @return QuizQuestionAnswer
   */
  public QuizQuestionAnswer toDomainObject()
  {
    QuizQuestion quizQuestion = new QuizQuestion();
    quizQuestion.setId( getQuizQuestionId() );

    QuizQuestionAnswer quizQuestionAnswer = new QuizQuestionAnswer();
    quizQuestionAnswer.setId( getQuizQuestionAnswerId() );
    quizQuestionAnswer.setQuizQuestion( quizQuestion );
    quizQuestionAnswer.setCorrect( isQuizQuestionAnswerCorrect() );
    quizQuestionAnswer.setCmAssetCode( getQuizQuestionAnswerCmAssetCode() );
    quizQuestionAnswer.setAnswerCmKey( getQuizQuestionAnswerCmKey() );
    quizQuestionAnswer.setExplanationCmKey( getQuizQuestionAnswerExplanationCmKey() );

    return quizQuestionAnswer;
  }

  /**
   * Clear out the editable fields so this form can be re-used to populate a new entry form upon
   * "Save and Add Another."
   */
  public void clearForNewQuestionAnswer()
  {
    setQuizQuestionAnswerText( "" );
    setQuizQuestionAnswerExplanation( "" );
    setQuizQuestionAnswerCorrect( false );
    setQuizQuestionAnswerId( null );
  }

  /**
   * @return value of method property
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * @param method value of method property
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return value of quizQuestionAnswerId property
   */
  public Long getQuizQuestionAnswerId()
  {
    return quizQuestionAnswerId;
  }

  /**
   * @param quizQuestionAnswerId value of quizQuestionAnswerId property
   */
  public void setQuizQuestionAnswerId( Long quizQuestionAnswerId )
  {
    this.quizQuestionAnswerId = quizQuestionAnswerId;
  }

  /**
   * @return value of quizQuestionAnswerText
   */
  public String getQuizQuestionAnswerText()
  {
    return quizQuestionAnswerText;
  }

  /**
   * @param quizQuestionAnswerText value of quizQuestionAnswerText property
   */
  public void setQuizQuestionAnswerText( String quizQuestionAnswerText )
  {
    this.quizQuestionAnswerText = quizQuestionAnswerText;
  }

  /**
   * @return value of quizQuestionAnswerExplanation
   */
  public String getQuizQuestionAnswerExplanation()
  {
    return quizQuestionAnswerExplanation;
  }

  /**
   * @param quizQuestionAnswerExplanation value of quizQuestionAnswerExplanation property
   */
  public void setQuizQuestionAnswerExplanation( String quizQuestionAnswerExplanation )
  {
    this.quizQuestionAnswerExplanation = quizQuestionAnswerExplanation;
  }

  /**
   * @return value of quizQuestionAnswerCmKey
   */
  public String getQuizQuestionAnswerCmKey()
  {
    return quizQuestionAnswerCmKey;
  }

  /**
   * @param quizQuestionAnswerCmKey value of quizQuestionAnswerCmKey property
   */
  public void setQuizQuestionAnswerCmKey( String quizQuestionAnswerCmKey )
  {
    this.quizQuestionAnswerCmKey = quizQuestionAnswerCmKey;
  }

  /**
   * @return value of quizQuestionAnswerExplanationCmKey
   */
  public String getQuizQuestionAnswerExplanationCmKey()
  {
    return quizQuestionAnswerExplanationCmKey;
  }

  /**
   * @param quizQuestionAnswerExplanationCmKey value of quizQuestionAnswerExplanationCmKey property
   */
  public void setQuizQuestionAnswerExplanationCmKey( String quizQuestionAnswerExplanationCmKey )
  {
    this.quizQuestionAnswerExplanationCmKey = quizQuestionAnswerExplanationCmKey;
  }

  /**
   * @return value of quizQuestionAnswerCorrect property
   */
  public boolean isQuizQuestionAnswerCorrect()
  {
    return quizQuestionAnswerCorrect;
  }

  /**
   * @param quizQuestionAnswerCorrect value of quizQuestionAnswerCorrect property
   */
  public void setQuizQuestionAnswerCorrect( boolean quizQuestionAnswerCorrect )
  {
    this.quizQuestionAnswerCorrect = quizQuestionAnswerCorrect;
  }

  /**
   * @return value of quizQuestionCmAssetKeyText property
   */
  public String getQuizQuestionText()
  {
    return quizQuestionText;
  }

  /**
   * @param quizQuestionText value of quizQuestionText property
   */
  public void setQuizQuestionText( String quizQuestionText )
  {
    this.quizQuestionText = quizQuestionText;
  }

  /**
   * @return value of quizQuestionAnswerCmAssetCode property
   */
  public String getQuizQuestionAnswerCmAssetCode()
  {
    return quizQuestionAnswerCmAssetCode;
  }

  /**
   * @param quizQuestionAnswerCmAssetCode value of quizQuestionAnswerCmAssetCode property
   */
  public void setQuizQuestionAnswerCmAssetCode( String quizQuestionAnswerCmAssetCode )
  {
    this.quizQuestionAnswerCmAssetCode = quizQuestionAnswerCmAssetCode;
  }

  /**
   * @return value of quizQuestionId property
   */
  public Long getQuizQuestionId()
  {
    return quizQuestionId;
  }

  /**
   * @param quizQuestionId value of quizQuestionId property
   */
  public void setQuizQuestionId( Long quizQuestionId )
  {
    this.quizQuestionId = quizQuestionId;
  }

  /**
   * @return value of quizQuestionRequired property
   */
  public boolean isQuizQuestionRequired()
  {
    return quizQuestionRequired;
  }

  /**
   * @param quizQuestionRequired value of quizQuestionRequired property
   */
  public void setQuizQuestionRequired( boolean quizQuestionRequired )
  {
    this.quizQuestionRequired = quizQuestionRequired;
  }

  /**
   * @return value of quizQuestionStatus property
   */
  public String getQuizQuestionStatus()
  {
    return quizQuestionStatus;
  }

  /**
   * @param quizQuestionStatus value of quizQuestionStatus property
   */
  public void setQuizQuestionStatus( String quizQuestionStatus )
  {
    this.quizQuestionStatus = quizQuestionStatus;
  }

  /**
   * @return value of quizName property
   */
  public String getQuizName()
  {
    return quizName;
  }

  /**
   * @param quizName value of quizName property
   */
  public void setQuizName( String quizName )
  {
    this.quizName = quizName;
  }

  /**
   * @return value of quizLive property
   */
  public boolean isQuizLive()
  {
    return quizLive;
  }

  /**
   * @param quizLive value of quizLive property
   */
  public void setQuizLive( boolean quizLive )
  {
    this.quizLive = quizLive;
  }

  public String getQuizQuestionStatusText()
  {
    return quizQuestionStatusText;
  }

  public void setQuizQuestionStatusText( String quizQuestionStatusText )
  {
    this.quizQuestionStatusText = quizQuestionStatusText;
  }

}
