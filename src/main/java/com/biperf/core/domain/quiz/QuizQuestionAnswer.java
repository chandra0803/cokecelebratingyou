/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/quiz/QuizQuestionAnswer.java,v $
 */

package com.biperf.core.domain.quiz;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * QuizQuestionAnswer.
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
public class QuizQuestionAnswer extends BaseDomain
{
  private QuizQuestion quizQuestion = null;
  private boolean correct = false;
  private String cmAssetCode = "";
  private String answerCmKey = "";
  private String explanationCmKey = "";
  private String text;
  private int sequenceNum;

  public QuizQuestionAnswer()
  {
    super();
  }

  /**
   * Does a deep copy of the QuizQuestionAnswer. This is a customized implementation of
   * java.lang.Object#clone()
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws ServiceErrorException 
   */
  public Object deepCopy() throws ServiceErrorException
  {
    QuizQuestionAnswer answer = new QuizQuestionAnswer();
    answer.setCorrect( this.isCorrect() );
    answer.setAnswerCmKey( this.getAnswerCmKey() );
    answer.setExplanationCmKey( this.getExplanationCmKey() );

    CMAssetService cmAssetService = getCMAssetService();

    String newCmAssetCode = cmAssetService.getUniqueAssetCode( Quiz.CM_QUESTION_ANSWER_ASSET_PREFIX );
    answer.setCmAssetCode( newCmAssetCode );
    cmAssetService.copyCMAsset( this.getCmAssetCode(), answer.getCmAssetCode(), Quiz.CM_QUESTION_ANSWER_KEY_DESC, null, false, null );

    return answer;
  }

  public String getExplanationCmKey()
  {
    return explanationCmKey;
  }

  public void setExplanationCmKey( String explanationCmKey )
  {
    this.explanationCmKey = explanationCmKey;
  }

  public String getAnswerCmKey()
  {
    return answerCmKey;
  }

  public void setAnswerCmKey( String answerCmKey )
  {
    this.answerCmKey = answerCmKey;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public String getQuestionAnswerText()
  {
    return CmsResourceBundle.getCmsBundle().getString( getCmAssetCode(), Quiz.CM_QUESTION_ANSWER_KEY );
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public String getQuestionAnswerExplanation()
  {
    String explanation = CmsResourceBundle.getCmsBundle().getString( getCmAssetCode(), Quiz.CM_QUESTION_ANSWER_EXPLANATION_KEY );
    if ( explanation.startsWith( "???" ) )
    {
      explanation = "";
    }
    return explanation;
  }

  public boolean isCorrect()
  {
    return correct;
  }

  public void setCorrect( boolean correct )
  {
    this.correct = correct;
  }

  public QuizQuestion getQuizQuestion()
  {
    return quizQuestion;
  }

  public void setQuizQuestion( QuizQuestion quizQuestion )
  {
    this.quizQuestion = quizQuestion;
  }

  public String getDisplayCorrect()
  {
    return isCorrect() ? CmsResourceBundle.getCmsBundle().getString( "quiz.question.TRUE" ) : CmsResourceBundle.getCmsBundle().getString( "quiz.question.FALSE" );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {

    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof QuizQuestionAnswer ) )
    {
      return false;
    }

    final QuizQuestionAnswer quizQuestionAnswer = (QuizQuestionAnswer)object;

    if ( quizQuestionAnswer.getCmAssetCode() != null && !quizQuestionAnswer.getCmAssetCode().equals( this.getCmAssetCode() ) )
    {
      return false;
    }

    if ( quizQuestionAnswer.getAnswerCmKey() != null && !quizQuestionAnswer.getAnswerCmKey().equals( this.getAnswerCmKey() ) )
    {
      return false;
    }

    if ( quizQuestionAnswer.getExplanationCmKey() != null && !quizQuestionAnswer.getExplanationCmKey().equals( this.getExplanationCmKey() ) )
    {
      return false;
    }

    if ( quizQuestionAnswer.getQuizQuestion() != null && !quizQuestionAnswer.getQuizQuestion().equals( this.getQuizQuestion() ) )
    {
      return false;
    }

    return true;

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {

    int result = 0;

    result += this.getCmAssetCode() != null ? this.getCmAssetCode().hashCode() : 0;
    result += this.getAnswerCmKey() != null ? this.getAnswerCmKey().hashCode() : 0;
    result += this.getExplanationCmKey() != null ? this.getExplanationCmKey().hashCode() : 0;
    result += this.getQuizQuestion() != null ? this.getQuizQuestion().hashCode() * 13 : 0;

    return result;

  }

  /**
   * Builds and returns a String representation of this. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {

    StringBuffer sb = new StringBuffer();

    sb.append( "[QUIZQUESTIONANSWER {" );
    sb.append( "id - " + this.getId() + ", " );
    sb.append( "quizQuestion.id - " + this.quizQuestion.getId() + ", " );
    sb.append( "correct - " + this.correct + ", " );
    sb.append( "cmAssetCode - " + this.cmAssetCode + ", " );
    sb.append( "answerCmKey - " + this.answerCmKey + ", " );
    sb.append( "explanationCmKey - " + this.explanationCmKey );
    sb.append( "}]" );

    return sb.toString();

  }
  
  protected CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }

}
