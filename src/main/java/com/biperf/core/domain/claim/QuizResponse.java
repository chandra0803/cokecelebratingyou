/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;

/**
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
 * <td>wadzinsk</td>
 * <td>Oct 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizResponse extends BaseDomain
{

  private QuizQuestion quizQuestion;
  private QuizClaim claim;
  private QuizQuestionAnswer selectedQuizQuestionAnswer;
  private int sequenceNumber;

  /**
   * Represents the response correctness at the time the quiz was taken (actual answer correctness
   * could change), or null if question has not yet been answered.
   */
  private Boolean correct;

  /**
   * @return value of claim property
   */
  public QuizClaim getClaim()
  {
    return claim;
  }

  /**
   * @param claim value for claim property
   */
  public void setClaim( QuizClaim claim )
  {
    this.claim = claim;
  }

  /**
   * @return value of correct property
   */
  public Boolean getCorrect()
  {
    return correct;
  }

  /**
   * @param correct value for correct property
   */
  public void setCorrect( Boolean correct )
  {
    this.correct = correct;
  }

  /**
   * @return value of quizQuestion property
   */
  public QuizQuestion getQuizQuestion()
  {
    return quizQuestion;
  }

  /**
   * @param quizQuestion value for quizQuestion property
   */
  public void setQuizQuestion( QuizQuestion quizQuestion )
  {
    this.quizQuestion = quizQuestion;
  }

  /**
   * @return value of selectedQuizQuestionAnswer property
   */
  public QuizQuestionAnswer getSelectedQuizQuestionAnswer()
  {
    return selectedQuizQuestionAnswer;
  }

  /**
   * @param selectedQuizQuestionAnswer value for selectedQuizQuestionAnswer property
   */
  public void setSelectedQuizQuestionAnswer( QuizQuestionAnswer selectedQuizQuestionAnswer )
  {
    this.selectedQuizQuestionAnswer = selectedQuizQuestionAnswer;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof QuizResponse ) )
    {
      return false;
    }

    QuizResponse quizResponse = (QuizResponse)object;

    if ( quizQuestion != null ? !quizQuestion.equals( quizResponse.getQuizQuestion() ) : quizResponse.getQuizQuestion() != null )
    {
      return false;
    }

    if ( claim != null ? !claim.equals( quizResponse.getClaim() ) : quizResponse.getClaim() != null )
    {
      return false;
    }

    return true;

  } // end equals

  public int hashCode()
  {
    int result = 0;

    result += quizQuestion != null ? quizQuestion.hashCode() : 0;
    result += claim != null ? claim.hashCode() : 13;

    return result;
  }

  /**
   * @return value of sequenceNumber property
   */
  public int getSequenceNumber()
  {
    return sequenceNumber;
  }

  /**
   * @param sequenceNumber value for sequenceNumber property
   */
  public void setSequenceNumber( int sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

}
