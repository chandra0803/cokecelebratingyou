/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/quiz/QuizQuestion.java,v $
 */

package com.biperf.core.domain.quiz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.QuizQuestionStatusType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * QuizQuestion.
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
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizQuestion extends BaseDomain
{

  private Quiz quiz;
  private boolean required = false;
  private QuizQuestionStatusType statusType;
  private String cmAssetName = "";
  private int sequenceNum;
  private String text;
  private List<QuizQuestionAnswer> quizQuestionAnswers = new ArrayList<QuizQuestionAnswer>();
  
  public QuizQuestion()
  {
    super();
  }

  /**
   * Does a deep copy of the QuizQuestion and its children if specified. This is a customized
   * implementation of java.lang.Object#clone()
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @return Object
   * @throws ServiceErrorException 
   */
  public Object deepCopy( boolean cloneWithChildren ) throws ServiceErrorException
  {
    QuizQuestion question = new QuizQuestion();
    question.setRequired( this.isRequired() );
    question.setStatusType( this.getStatusType() );
    question.setSequenceNum( this.getSequenceNum() );

    CMAssetService cmAssetService = getCMAssetService();

    String newCmAssetCode = cmAssetService.getUniqueAssetCode( Quiz.CM_QUESTION_ASSET_PREFIX );
    question.setCmAssetName( newCmAssetCode );
    cmAssetService.copyCMAsset( this.getCmAssetName(), question.getCmAssetName(), Quiz.CM_QUESTION_NAME_KEY_DESC, null, false, null );

    if ( cloneWithChildren )
    {
      Iterator iter = this.getQuizQuestionAnswers().iterator();
      while ( iter.hasNext() )
      {
        QuizQuestionAnswer answerToCopy = (QuizQuestionAnswer)iter.next();
        question.addQuizQuestionAnswer( (QuizQuestionAnswer)answerToCopy.deepCopy() );
      }
    }
    else
    {
      question.setQuizQuestionAnswers( new ArrayList() );
    }
    return question;
  }

  public List getCorrectQuizQuestionAnswers()
  {
    ArrayList correctAnswers = new ArrayList();

    for ( Iterator iter = quizQuestionAnswers.iterator(); iter.hasNext(); )
    {
      QuizQuestionAnswer quizQuestionAnswer = (QuizQuestionAnswer)iter.next();
      if ( quizQuestionAnswer.isCorrect() )
      {
        correctAnswers.add( quizQuestionAnswer );
      }

    }

    return correctAnswers;
  }

  public List<QuizQuestionAnswer> getQuizQuestionAnswers()
  {
    return this.quizQuestionAnswers;
  }

  public void setQuizQuestionAnswers( List<QuizQuestionAnswer> quizQuestionAnswers )
  {
    this.quizQuestionAnswers = quizQuestionAnswers;
  }

  public void addQuizQuestionAnswer( QuizQuestionAnswer quizQuestionAnswer )
  {
    quizQuestionAnswer.setQuizQuestion( this );
    this.quizQuestionAnswers.add( quizQuestionAnswer );
  }

  public String getCmAssetName()
  {
    return cmAssetName;
  }

  public void setCmAssetName( String cmAssetName )
  {
    this.cmAssetName = cmAssetName;
  }

  public Quiz getQuiz()
  {
    return quiz;
  }

  public void setQuiz( Quiz quiz )
  {
    this.quiz = quiz;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired( boolean required )
  {
    this.required = required;
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public QuizQuestionStatusType getStatusType()
  {
    return statusType;
  }

  public void setStatusType( QuizQuestionStatusType statusType )
  {
    this.statusType = statusType;
  }

  public String getQuestionText()
  {
    return CmsResourceBundle.getCmsBundle().getString( getCmAssetName(), Quiz.CM_QUESTION_NAME_KEY );
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public String getDisplayRequired()
  {
    return isRequired() ? CmsResourceBundle.getCmsBundle().getString( "quiz.form.question.list.TRUE" ) : CmsResourceBundle.getCmsBundle().getString( "quiz.form.question.list.FALSE" );
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

    if ( ! ( object instanceof QuizQuestion ) )
    {
      return false;
    }

    final QuizQuestion quizQuestion = (QuizQuestion)object;

    if ( quizQuestion.getCmAssetName() != null && !quizQuestion.getCmAssetName().equals( this.getCmAssetName() ) )
    {
      return false;
    }

    if ( quizQuestion.getQuiz() != null && !quizQuestion.getQuiz().equals( this.getQuiz() ) )
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

    result += this.getCmAssetName() != null ? this.getCmAssetName().hashCode() : 0;
    result += this.getQuiz() != null ? this.getQuiz().hashCode() * 13 : 0;

    return result;
  }

  /**
   * Builds a String representation of this.
   * 
   * @return String
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append( "[QUIZQUESTION {" );
    sb.append( "quiz.id - " + this.quiz.getId() + ", " );
    sb.append( "required - " + this.required + ", " );
    sb.append( "status - " + this.statusType.getCode() + ", " );
    sb.append( "cmAssetName - " + this.cmAssetName + ", " );
    sb.append( "sequenceNum - " + this.sequenceNum + ", " );
    sb.append( "}]" );
    return sb.toString();
  }

  public boolean isActive()
  {
    return statusType.getCode().equals( QuizQuestionStatusType.ACTIVE );
  }
  
  protected CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }

}
