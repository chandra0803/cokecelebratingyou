/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/fileload/QuizImportRecord.java,v $
 */

package com.biperf.core.domain.fileload;

/**
 * QuizImportRecord.
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
 * <td>Tammy Cheng</td>
 * <td>Jun 20, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizImportRecord extends ImportRecord
{
  public static final String HEADER_RECORD = "H";
  public static final String QUESTION_RECORD = "Q";
  public static final String ANSWER_RECORD = "A";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // -----------------------
  // Quiz
  // -----------------------
  /**
   * The type of quiz import record
   * 'H' Header, 'Q' Question, 'A' Answer
   */
  private String recordType; // required

  /**
   * The Name of the quiz
   */
  private String quizName;

  /**
   * The Description of the quiz
   */
  private String quizDescription;

  /**
   * The Display Type of the quiz (fixed or random)
   */
  private String quizType;

  /**
   * The score necessary to pass the quiz
   */
  private Integer quizPassingScore;

  /**
   * The number of questions to ask in a Random type quiz
   */
  private Integer quizNumberOfQuestionsAsked; // - used when quizType is random

  // -----------------------
  // Quiz Question
  // ------------------------
  /**
   * The Status Type of the Quiz Question (will be defaulted to "active" by the staging stored procedure if not specified in the import file)
   */
  private String questionStatusType;

  /**
   * Whether or not the Quiz Question is required for the Quiz (will be defaulted to false if not specified in the import file)
   */
  private Boolean questionRequired;

  /**
   * The text of the Quiz Question
   */
  private String question;

  // -----------------------
  // Quiz Answer
  // -----------------------
  /**
  * Whether or not this is the correct answer (Multiple Choice - only 1 answer is correct for a given question)
  */
  private Boolean answerCorrect;

  /**
   * The text of the Quiz Answer
   */
  private String answerChoice;

  /**
   * The test of the explanation to the Quiz Answer, if used
   */
  private String answerChoiceExplanation;

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the given object and this object are equal; returns false otherwise.
   *
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object the object to compare to this object.
   * @return true if the given object and this object are equal; false otherwise.
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof QuizImportRecord ) )
    {
      return false;
    }

    QuizImportRecord importRecord = (QuizImportRecord)object;

    if ( this.getId() != null && this.getId().equals( importRecord.getId() ) )
    {
      return true;
    }

    return false;
  }

  /**
   * Returns the hashcode for this object.
   *
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return the hashcode for this object.
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * Returns a string representation of this object.
   *
   * @see java.lang.Object#toString()
   * @return a string representation of this object.
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "QuizImportRecord [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getQuizName()
  {
    return quizName;
  }

  public void setQuizName( String quizName )
  {
    this.quizName = quizName;
  }

  public String getQuizDescription()
  {
    return quizDescription;
  }

  public void setQuizDescription( String quizDescription )
  {
    this.quizDescription = quizDescription;
  }

  public String getQuizType()
  {
    return quizType;
  }

  public void setQuizType( String quizType )
  {
    this.quizType = quizType;
  }

  public Integer getQuizPassingScore()
  {
    return quizPassingScore;
  }

  public void setQuizPassingScore( Integer quizPassingScore )
  {
    this.quizPassingScore = quizPassingScore;
  }

  public Integer getQuizNumberOfQuestionsAsked()
  {
    return quizNumberOfQuestionsAsked;
  }

  public void setQuizNumberOfQuestionsAsked( Integer quizNumberOfQuestionsAsked )
  {
    this.quizNumberOfQuestionsAsked = quizNumberOfQuestionsAsked;
  }

  public Boolean getQuestionRequired()
  {
    return questionRequired;
  }

  public void setQuestionRequired( Boolean questionRequired )
  {
    this.questionRequired = questionRequired;
  }

  public String getQuestionRequiredDisplay()
  {
    String value = "";
    if ( questionRequired != null )
    {
      if ( questionRequired.booleanValue() )
      {
        value = "Y";
      }
      else
      {
        value = "N";
      }
    }
    return value;
  }

  public String getQuestionStatusType()
  {
    return questionStatusType;
  }

  public String getQuestionStatusTypeDisplay()
  {
    String value = "";
    if ( questionStatusType != null )
    {
      if ( questionStatusType.equals( "active" ) )
      {
        value = "Y";
      }
      else
      {
        value = "N";
      }
    }
    return value;
  }

  public void setQuestionStatusType( String questionStatusType )
  {
    this.questionStatusType = questionStatusType;
  }

  public Boolean getAnswerCorrect()
  {
    return answerCorrect;
  }

  public String getAnswerCorrectDisplay()
  {
    String value = "";
    if ( answerCorrect != null )
    {
      if ( answerCorrect.booleanValue() )
      {
        value = "Y";
      }
      else
      {
        value = "N";
      }
    }
    return value;
  }

  public void setAnswerCorrect( Boolean answerCorrect )
  {
    this.answerCorrect = answerCorrect;
  }

  public String getAnswerChoice()
  {
    return answerChoice;
  }

  public void setAnswerChoice( String answerChoice )
  {
    this.answerChoice = answerChoice;
  }

  public String getAnswerChoiceExplanation()
  {
    return answerChoiceExplanation;
  }

  public void setAnswerChoiceExplanation( String answerChoiceExplanation )
  {
    this.answerChoiceExplanation = answerChoiceExplanation;
  }

  public String getQuestion()
  {
    return question;
  }

  public void setQuestion( String question )
  {
    this.question = question;
  }

  public String getRecordType()
  {
    return recordType;
  }

  public void setRecordType( String recordType )
  {
    this.recordType = recordType;
  }

}
