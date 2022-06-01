/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.quiz;

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
 * <td>potosky</td>
 * <td>Oct 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizQuestionAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public QuizQuestionAssociationRequest( int hydrateLevel )
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
    QuizQuestion quizQuestion = (QuizQuestion)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateQuizQuestionAnswers( quizQuestion );
        hydrateQuizQuestionQuiz( quizQuestion );
        break;

      default:
        break;
    } // switch
  }

  /**
   * Hydrate the quiz question answer list on each quiz question
   * 
   * @param quizQuestion
   */
  private void hydrateQuizQuestionAnswers( QuizQuestion quizQuestion )
  {
    initialize( quizQuestion.getQuizQuestionAnswers() );
  }

  /**
   * Hydrate the quiz question answer list on each quiz question
   * 
   * @param quizQuestion
   */
  private void hydrateQuizQuestionQuiz( QuizQuestion quizQuestion )
  {
    initialize( quizQuestion.getQuiz().getQuizQuestions() );
  }
}
