/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/quiz/QuizAssociationRequest.java,v $
 */

package com.biperf.core.service.quiz;

import java.util.Iterator;

import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * QuizAssociationRequest.
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
 * <td>jenniget</td>
 * <td>Oct 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Hyrate Level: QUIZ_QUESTION
   */
  public static final int QUIZ_QUESTION = 2;

  /**
   * Hyrate Level: QUIZ_QUESTION_ANSWER
   */
  public static final int QUIZ_QUESTION_ANSWER = 3;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public QuizAssociationRequest( int hydrateLevel )
  {
    super();
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    Quiz quiz = (Quiz)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateQuizQuestions( quiz );
        hydrateQuizQuestionAnswers( quiz );
        break;

      case QUIZ_QUESTION:
        hydrateQuizQuestions( quiz );
        break;

      case QUIZ_QUESTION_ANSWER:
        hydrateQuizQuestions( quiz );
        hydrateQuizQuestionAnswers( quiz );
        break;

      default:
        break;
    } // switch
  }

  /**
   * Loads the quiz question list attached on this quiz
   * 
   * @param quiz
   */
  private void hydrateQuizQuestions( Quiz quiz )
  {
    initialize( quiz.getQuizQuestions() );
  }

  /**
   * Hydrate the quiz question answer list on each quiz question in the list attached to this quiz.
   * 
   * @param quiz
   */
  private void hydrateQuizQuestionAnswers( Quiz quiz )
  {
    if ( quiz.getQuizQuestions() != null )
    {
      for ( Iterator iter = quiz.getQuizQuestions().iterator(); iter.hasNext(); )
      {
        QuizQuestion question = (QuizQuestion)iter.next();
        if ( question != null )
        {
          initialize( question.getQuizQuestionAnswers() );
        }
      }
    }
  }
}
