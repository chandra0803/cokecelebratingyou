/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/QuizClaim.java,v $
 */

package com.biperf.core.domain.claim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.utils.GuidUtils;

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
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizClaim extends Claim
{
  /**
   * quiz responses sorted by sequence number
   */
  private Set quizResponses = new LinkedHashSet();

  private Boolean pass;
  private int score;
  private int questionCount;
  private int passingScore;
  private QuizQuestion currentQuizQuestion;
  private boolean managerAward = false;
  private Quiz quiz;

  private Set<QuizClaimItem> quizClaimItems = new LinkedHashSet<QuizClaimItem>();

  public int getQuestionsCompletedCount()
  {
    return quizResponses.size();
  }

  public boolean isQuizComplete()
  {
    return getQuestionsCompletedCount() >= getQuestionCount();
  }

  public QuizQuestion getLatestQuestionAnswered()
  {
    int latestIndex = quizResponses.size() - 1;

    if ( latestIndex < 0 )
    {
      // no questions yet answered.
      return null;
    }

    return ( (QuizResponse)getQuizResponseAsList().get( latestIndex ) ).getQuizQuestion();
  }

  public Boolean getLatestQuestionResponse()
  {
    int latestIndex = quizResponses.size() - 1;

    if ( latestIndex < 0 )
    {
      // no questions yet answered.
      return null;
    }

    return ( (QuizResponse)getQuizResponseAsList().get( latestIndex ) ).getCorrect();
  }

  public List getQuizResponseAsList()
  {
    return Collections.unmodifiableList( new ArrayList( quizResponses ) );
  }

  /**
   * @return value of pass property
   */
  public Boolean getPass()
  {
    return pass;
  }

  /**
   * @param pass value for pass property
   */
  public void setPass( Boolean pass )
  {
    this.pass = pass;
  }

  /**
   * @return value of passingScore property
   */
  public int getPassingScore()
  {
    return passingScore;
  }

  /**
   * @param passingScore value for passingScore property
   */
  public void setPassingScore( int passingScore )
  {
    this.passingScore = passingScore;
  }

  /**
   * @return value of questionCount property
   */
  public int getQuestionCount()
  {
    return questionCount;
  }

  /**
   * @param questionCount value for questionCount property
   */
  public void setQuestionCount( int questionCount )
  {
    this.questionCount = questionCount;
  }

  /**
   * @return value of quizResponses property
   */
  public Set getQuizResponses()
  {
    return quizResponses;
  }

  /**
   * @param quizResponses value for quizResponses property
   */
  public void setQuizResponses( Set quizResponses )
  {
    this.quizResponses = quizResponses;
  }

  /**
   * @return value of score property
   */
  public int getScore()
  {
    return score;
  }

  /**
   * @param score value for score property
   */
  public void setScore( int score )
  {
    this.score = score;
  }

  public void addQuizResponse( QuizResponse quizResponse )
  {
    quizResponse.setClaim( this );
    quizResponses.add( quizResponse );
  }

  /**
   * @return value of currentQuizQuestion property
   */
  public QuizQuestion getCurrentQuizQuestion()
  {
    return currentQuizQuestion;
  }

  /**
   * @param currentQuizQuestion value for currentQuizQuestion property
   */
  public void setCurrentQuizQuestion( QuizQuestion currentQuizQuestion )
  {
    this.currentQuizQuestion = currentQuizQuestion;
  }

  public List getQuizResponsesAsQuestions()
  {
    ArrayList questions = new ArrayList();
    for ( Iterator iter = quizResponses.iterator(); iter.hasNext(); )
    {
      QuizResponse quizResponse = (QuizResponse)iter.next();
      questions.add( quizResponse.getQuizQuestion() );

    }

    return questions;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.claim.Claim#isApprovableClaimType()
   */
  public boolean isApprovableClaimType()
  {
    return false;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.claim.Claim#getApprovableItems()
   */
  public Set getApprovableItems()
  {
    return Collections.EMPTY_SET;
  }

  public void addQuizClaimItem( QuizClaimItem quizClaimItem )
  {
    if ( quizClaimItem.getSerialId() == null || quizClaimItem.getSerialId().equals( "" ) )
    {
      quizClaimItem.setSerialId( GuidUtils.generateGuid() );
    }

    quizClaimItem.setClaim( this );
    quizClaimItems.add( quizClaimItem );
  }

  public boolean isManagerAward()
  {
    return managerAward;
  }

  public void setManagerAward( boolean managerAward )
  {
    this.managerAward = managerAward;
  }

  public void setQuizClaimItems( Set<QuizClaimItem> quizClaimItems )
  {
    this.quizClaimItems = quizClaimItems;
  }

  public Set<QuizClaimItem> getQuizClaimItems()
  {
    return quizClaimItems;
  }

  public Quiz getQuiz()
  {
    return quiz;
  }

  public void setQuiz( Quiz quiz )
  {
    this.quiz = quiz;
  }
}
