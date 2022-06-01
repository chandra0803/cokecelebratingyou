/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/quiz/QuizQuestionForm.java,v $
 */

package com.biperf.core.ui.quiz;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.QuizQuestionStatusType;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.StringUtil;

/**
 * QuizQuestionForm.
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
 * <td>sedey</td>
 * <td>Oct 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizQuestionForm extends BaseForm
{
  private String method;
  private Long quizFormId;
  private String quizName;
  private String quizType;
  private Long quizQuestionId;
  private String quizQuestionCmAssetName;
  private String quizQuestionText;
  private boolean quizQuestionRequired;
  private String quizQuestionStatus;
  private String quizQuestionStatusText;
  private String[] deletedAnswers;
  private String newAnswerSequenceNum;
  private Long quizQuestionAnswerId;
  private List answers;
  private int questionCount;
  private int numberOfQuestionsAsked;
  private int activeQuestionCount;

  /**
   * Load the form
   * 
   * @param question
   */
  public void load( QuizQuestion question )
  {
    this.quizFormId = question.getQuiz().getId();
    this.quizName = question.getQuiz().getName();
    this.quizType = question.getQuiz().getQuizType().getCode();
    this.quizQuestionId = question.getId();
    this.quizQuestionText = StringUtil.convertLineBreaks( question.getQuestionText() );
    this.quizQuestionCmAssetName = question.getCmAssetName();
    if ( quizType.equals( "fixed" ) )
    {
      this.quizQuestionRequired = true;
    }
    else
    {
      this.quizQuestionRequired = question.isRequired();
    }
    this.quizQuestionStatus = question.getStatusType().getCode();
    this.quizQuestionStatusText = question.getStatusType().getName();
    this.questionCount = question.getQuiz().getQuizQuestions().size();
    this.activeQuestionCount = question.getQuiz().getRequiredActiveQuestions().size();
    this.numberOfQuestionsAsked = question.getQuiz().getNumberOfQuestionsAsked();
    this.answers = question.getQuizQuestionAnswers();
  }

  public void load( Quiz quiz )
  {
    this.quizFormId = quiz.getId();
    this.quizName = quiz.getName();
    this.quizType = quiz.getQuizType().getCode();
    if ( quizType.equals( "fixed" ) )
    {
      this.quizQuestionRequired = true;
    }
    this.questionCount = quiz.getQuizQuestions().size();
    this.activeQuestionCount = quiz.getRequiredActiveQuestions().size();
    this.numberOfQuestionsAsked = quiz.getNumberOfQuestionsAsked();
  }

  /**
   * Creates a detatched QuizQuestion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return Quiz
   */
  public QuizQuestion toDomainObject()
  {
    QuizQuestion quizQuestion = new QuizQuestion();
    quizQuestion.setId( this.quizQuestionId );
    quizQuestion.setCmAssetName( this.quizQuestionCmAssetName );
    quizQuestion.setRequired( this.quizQuestionRequired );
    quizQuestion.setStatusType( QuizQuestionStatusType.lookup( this.quizQuestionStatus ) );

    return quizQuestion;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( quizType.equals( "random" ) )
    {
      if ( isQuizQuestionRequired() && activeQuestionCount == numberOfQuestionsAsked && !quizQuestionStatus.equals( QuizQuestionStatusType.INACTIVE ) && quizQuestionCmAssetName.length() == 0 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.errors.RANDOM_REQUIRED_QUESTION" ) );
      }
    }

    return actionErrors;
  }

  public String[] getDeletedAnswers()
  {
    return deletedAnswers;
  }

  public void setDeletedAnswers( String[] deletedAnswers )
  {
    this.deletedAnswers = deletedAnswers;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getQuizFormId()
  {
    return quizFormId;
  }

  public void setQuizFormId( Long quizId )
  {
    this.quizFormId = quizId;
  }

  public String getQuizName()
  {
    return quizName;
  }

  public void setQuizName( String quizName )
  {
    this.quizName = quizName;
  }

  public String getQuizQuestionCmAssetName()
  {
    return quizQuestionCmAssetName;
  }

  public void setQuizQuestionCmAssetName( String quizQuestionCmAssetName )
  {
    this.quizQuestionCmAssetName = quizQuestionCmAssetName;
  }

  public Long getQuizQuestionId()
  {
    return quizQuestionId;
  }

  public void setQuizQuestionId( Long quizQuestionId )
  {
    this.quizQuestionId = quizQuestionId;
  }

  public boolean isQuizQuestionRequired()
  {
    return quizQuestionRequired;
  }

  public void setQuizQuestionRequired( boolean quizQuestionRequired )
  {
    this.quizQuestionRequired = quizQuestionRequired;
  }

  public String getQuizQuestionText()
  {
    return quizQuestionText;
  }

  public void setQuizQuestionText( String quizQuestionText )
  {
    this.quizQuestionText = quizQuestionText;
  }

  public String getNewAnswerSequenceNum()
  {
    return newAnswerSequenceNum;
  }

  public void setNewAnswerSequenceNum( String newAnswerSequenceNum )
  {
    this.newAnswerSequenceNum = newAnswerSequenceNum;
  }

  public Long getQuizQuestionAnswerId()
  {
    return quizQuestionAnswerId;
  }

  public void setQuizQuestionAnswerId( Long quizQuestionAnswerId )
  {
    this.quizQuestionAnswerId = quizQuestionAnswerId;
  }

  public void setAnswers( List answers )
  {
    this.answers = answers;
  }

  public List getAnswers()
  {
    return answers;
  }

  public int getAnswersSize()
  {
    int size = 0;
    if ( answers != null )
    {
      size = answers.size();
    }
    return size;
  }

  public String getQuizQuestionStatus()
  {
    return quizQuestionStatus;
  }

  public void setQuizQuestionStatus( String quizQuestionStatus )
  {
    this.quizQuestionStatus = quizQuestionStatus;
  }

  public String getQuizType()
  {
    return quizType;
  }

  public void setQuizType( String quizTypeCode )
  {
    this.quizType = quizTypeCode;
  }

  public String getQuizQuestionStatusText()
  {
    return quizQuestionStatusText;
  }

  public void setQuizQuestionStatusText( String quizQuestionStatusText )
  {
    this.quizQuestionStatusText = quizQuestionStatusText;
  }

  public int getQuestionCount()
  {
    return questionCount;
  }

  public void setQuestionCount( int questionCount )
  {
    this.questionCount = questionCount;
  }

  public int getNumberOfQuestionsAsked()
  {
    return numberOfQuestionsAsked;
  }

  public void setNumberOfQuestionsAsked( int numberOfQuestionsAsked )
  {
    this.numberOfQuestionsAsked = numberOfQuestionsAsked;
  }

  public int getActiveQuestionCount()
  {
    return activeQuestionCount;
  }

  public void setActiveQuestionCount( int activeQuestionCount )
  {
    this.activeQuestionCount = activeQuestionCount;
  }

}
